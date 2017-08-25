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
import com.ashishlakhmani.youthopia.activity.SplashScreen;
import com.ashishlakhmani.youthopia.fragment.EventCommonFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class RegisteredAdapter extends RecyclerView.Adapter {

    private Context context;
    private ArrayList<String> registeredNameList;
    private static HashMap<String, String> eventDetails = new HashMap<>();

    public RegisteredAdapter(Context context, ArrayList<String> registeredNameList) {
        this.context = context;
        this.registeredNameList = registeredNameList;

        String[] eventCategories = {"cultural", "technical", "gaming", "informal", "literary"};
        if (eventDetails.isEmpty()) {
            try {
                JSONObject jsonObject = new JSONObject(SplashScreen.jsonString);
                for (int i = 0; i < eventCategories.length; i++) {
                    JSONArray jsonArray = jsonObject.getJSONArray(eventCategories[i]);
                    for (int j = 0; j < jsonArray.length(); j++) {
                        JSONObject innerJsonOject = jsonArray.getJSONObject(j);
                        eventDetails.put(innerJsonOject.getString("eventname"), innerJsonOject.getString("detailslink"));
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.registered_layout, parent, false);
        return new RegisteredAdapter.MyViewHolder(view); // pass the view to View Holder
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        ((MyViewHolder) holder).registeredTextView.setText(registeredNameList.get(position));


        ((MyViewHolder) holder).registeredCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventCommonFragment eventCommonFragment = new EventCommonFragment();
                Bundle bundle = new Bundle();
                bundle.putString("eventName", registeredNameList.get(position));
                bundle.putString("detailsLink", eventDetails.get(registeredNameList.get(position)));
                eventCommonFragment.setArguments(bundle);
                ((Home) context).loadFragment(eventCommonFragment, registeredNameList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return registeredNameList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        // initialize the item view's

        TextView registeredTextView;
        CardView registeredCardView;

        public MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            registeredTextView = itemView.findViewById(R.id.favoriteTextView);
            registeredCardView = itemView.findViewById(R.id.favoriteCardView);
        }
    }
}
