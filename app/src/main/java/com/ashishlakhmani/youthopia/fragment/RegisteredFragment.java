package com.ashishlakhmani.youthopia.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.ashishlakhmani.youthopia.classes.RegisteredEvents;
import com.ashishlakhmani.youthopia.R;


public class RegisteredFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_registered, container, false);

        ProgressBar progressBar = (ProgressBar)view.findViewById(R.id.registered_progressbar);
        progressBar.setVisibility(View.VISIBLE);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.favoriteRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        SharedPreferences sp = getContext().getSharedPreferences("login", Context.MODE_PRIVATE);
        RegisteredEvents registeredEvents = new RegisteredEvents(getContext(),progressBar,recyclerView);
        registeredEvents.execute(sp.getString("email",null));

        return view;
    }

}