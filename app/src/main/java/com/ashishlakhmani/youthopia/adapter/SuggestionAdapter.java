package com.ashishlakhmani.youthopia.adapter;


import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ashishlakhmani.youthopia.R;
import com.ashishlakhmani.youthopia.activity.Home;
import com.ashishlakhmani.youthopia.fragment.EventCommonFragment;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class SuggestionAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<String> suggestions;
    private HashMap<String, String> eventDetails;
    private MaterialSearchView searchView;


    public SuggestionAdapter(Context context, HashMap<String, String> eventDetails, List<String> suggestions, MaterialSearchView searchView) {
        this.context = context;
        this.eventDetails = eventDetails;
        this.suggestions = suggestions;
        this.searchView = searchView;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.suggestion_layout, parent, false);
        return new MyViewHolder(view); // pass the view to View Holder
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        //Here each item of arraylist will go to the list view..
        final String eventName = suggestions.get(position);
        final String detailsLink = eventDetails.get(eventName);

        ((MyViewHolder) holder).name.setText(suggestions.get(position));

        ((MyViewHolder) holder).cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventCommonFragment eventCommonFragment = new EventCommonFragment();
                Bundle bundle = new Bundle();
                bundle.putString("eventName", eventName);
                bundle.putString("detailsLink", detailsLink);
                eventCommonFragment.setArguments(bundle);
                if (!((Home) context).isEventFragmentVisible(eventName)) {
                    searchView.closeSearch();
                    ((Home) context).loadFragment(eventCommonFragment, eventName);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return suggestions.size();
    }


    private class MyViewHolder extends RecyclerView.ViewHolder {
        // init the item view's
        TextView name;
        CardView cardView;

        private MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            name = itemView.findViewById(R.id.text);
            cardView = itemView.findViewById(R.id.suggestion_cardView);
        }
    }

    public void setFilter(ArrayList<String> newList) {
        suggestions = newList;
        notifyDataSetChanged();
    }

}

