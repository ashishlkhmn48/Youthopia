package com.ashishlakhmani.youthopia.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ashishlakhmani.youthopia.classes.ListRegisteredEvents;
import com.ashishlakhmani.youthopia.R;
import com.ashishlakhmani.youthopia.activity.Home;
import com.ashishlakhmani.youthopia.activity.SplashScreen;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class EventTypeCommonFragment extends Fragment {

    JSONArray jsonArray;
    TextView headingTextView;
    TextView quoteTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_eventtypecommon, container, false);
        ((Home) getContext()).changeConstraintLayout(getArguments().getString("toolbarHeading"), true);
        headingTextView = (TextView) view.findViewById(R.id.eventtype_heading);
        quoteTextView = (TextView) view.findViewById(R.id.eventtype_quote);

        ProgressBar progressBar = (ProgressBar)view.findViewById(R.id.progressbar_eventLoading);
        progressBar.setVisibility(View.VISIBLE);

        headingTextView.setText(getArguments().getString("heading"));
        quoteTextView.setText(getArguments().getString("quote"));

        try {
            JSONObject jsonObject = new JSONObject(SplashScreen.jsonString);
            jsonArray = jsonObject.getJSONArray(getArguments().getString("toolbarHeading").toLowerCase());
        } catch (JSONException e) {
            Toast.makeText(getContext(), "Sorry..Something went wrong..!!", Toast.LENGTH_SHORT).show();
        }

        RecyclerView recyclerView = view.findViewById(R.id.eventtypeRecyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        ListRegisteredEvents listRegisteredEvents = new ListRegisteredEvents(getContext(), recyclerView, jsonArray, progressBar);
        SharedPreferences sp = getContext().getSharedPreferences("login", Context.MODE_PRIVATE);
        listRegisteredEvents.execute(sp.getString("email", null));

        return view;
    }

}
