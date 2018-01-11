package com.ashishlakhmani.youthopia.fragment;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ashishlakhmani.youthopia.R;
import com.ashishlakhmani.youthopia.activity.Home;
import com.ashishlakhmani.youthopia.activity.SplashScreen;
import com.ashishlakhmani.youthopia.classes.ListRegisteredEvents;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class EventTypeCommonFragment extends Fragment {

    JSONArray jsonArray;
    TextView headingTextView;
    TextView quoteTextView;

    SwipeRefreshLayout swipeRefreshLayout;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_eventtypecommon, container, false);
        ((Home) getContext()).changeConstraintLayout(getArguments().getString("toolbarHeading"), true);
        headingTextView = (TextView) view.findViewById(R.id.eventtype_heading);
        quoteTextView = (TextView) view.findViewById(R.id.eventtype_quote);

        progressBar = (ProgressBar) view.findViewById(R.id.progressbar_eventLoading);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                loadEvents(view);
            }
        });

        headingTextView.setText(getArguments().getString("heading"));
        quoteTextView.setText(getArguments().getString("quote"));


        new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                headingTextView.animate()
                        .translationY(0)
                        .alpha(0.0f)
                        .setDuration(700)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                headingTextView.setVisibility(View.GONE);
                            }
                        });

                quoteTextView.animate()
                        .translationY(0)
                        .alpha(0.0f)
                        .setDuration(700)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                quoteTextView.setVisibility(View.GONE);
                            }
                        });

            }
        }.start();

        try {
            JSONObject jsonObject = new JSONObject(SplashScreen.jsonString);
            jsonArray = jsonObject.getJSONArray(getArguments().getString("toolbarHeading").toLowerCase());
        } catch (JSONException e) {
            Toast.makeText(getContext(), "Sorry..Something went wrong..!!", Toast.LENGTH_SHORT).show();
        }

        loadEvents(view);
        return view;
    }

    private void loadEvents(View view) {
        progressBar.setVisibility(View.VISIBLE);
        RecyclerView recyclerView = view.findViewById(R.id.eventtypeRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        ListRegisteredEvents listRegisteredEvents = new ListRegisteredEvents(getContext(), recyclerView, jsonArray, progressBar);
        SharedPreferences sp = getContext().getSharedPreferences("login", Context.MODE_PRIVATE);
        listRegisteredEvents.execute(sp.getString("email", null));
    }

}
