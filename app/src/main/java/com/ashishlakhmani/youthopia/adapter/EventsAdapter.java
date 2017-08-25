package com.ashishlakhmani.youthopia.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ashishlakhmani.youthopia.R;
import com.ashishlakhmani.youthopia.activity.Home;
import com.ashishlakhmani.youthopia.fragment.EventCommonFragment;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class EventsAdapter extends RecyclerView.Adapter {
    private Context context;
    private JSONArray jsonArray;
    private ArrayList<String> list;


    public EventsAdapter(Context context, JSONArray jsonArray, ArrayList<String> list) {
        this.context = context;
        this.jsonArray = jsonArray;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_cardview_layout, parent, false);
        return new EventsAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        try {
            JSONObject jasonObject = jsonArray.getJSONObject(position);
            //initialize all components..
            final String eventName = jasonObject.getString("eventname");
            final String eventSubheading = jasonObject.getString("subheading");
            String picLink = jasonObject.getString("piclink");
            final String detailsLink = jasonObject.getString("detailslink");

            Picasso.with(context).load(picLink).
                    placeholder(R.drawable.placeholder_album).
                    into(((MyViewHolder) holder).imageView);

            ((MyViewHolder) holder).heading.setText(eventName.toUpperCase());
            ((MyViewHolder) holder).subheading.setText(eventSubheading.toUpperCase());

            ((MyViewHolder) holder).cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventCommonFragment eventCommonFragment = new EventCommonFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("eventName", eventName);
                    bundle.putString("detailsLink", detailsLink);
                    eventCommonFragment.setArguments(bundle);
                    ((Home) context).loadFragment(eventCommonFragment, eventName);
                }
            });

            //Check whether the event is registered or not..
            for (String s : list) {
                if (s.toLowerCase().equals(eventName.toLowerCase())) {
                    ((MyViewHolder) holder).registeredPic.setVisibility(View.VISIBLE);
                }
            }

        } catch (JSONException e) {
            Toast.makeText(context, "Sorry..Something went wrong..!!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return jsonArray.length();
    }


    private class MyViewHolder extends RecyclerView.ViewHolder {
        // initialize the item view's

        ImageView imageView;
        ImageView registeredPic;
        //ToggleButton toggle;
        TextView heading;
        TextView subheading;
        CardView cardView;

        private MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            imageView = itemView.findViewById(R.id.card_pic);
            registeredPic = itemView.findViewById(R.id.registered);
            heading = itemView.findViewById(R.id.card_heading);
            subheading = itemView.findViewById(R.id.card_subheading);
            cardView = itemView.findViewById(R.id.event_cardView);
        }
    }

}
