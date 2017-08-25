package com.ashishlakhmani.youthopia.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ashishlakhmani.youthopia.R;
import com.ashishlakhmani.youthopia.activity.Home;
import com.ashishlakhmani.youthopia.activity.SplashScreen;
import com.ashishlakhmani.youthopia.adapter.SchedulePager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ScheduleFragment extends Fragment {

    ViewPager viewPager;
    int numberofpics;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_schedule, container, false);
        ((Home) getActivity()).changeConstraintLayout("SCHEDULE", true);

        try {
            JSONObject jsonObject = new JSONObject(SplashScreen.jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("number");
            JSONObject innerJsonOject = jsonArray.getJSONObject(0);
            numberofpics = innerJsonOject.getInt("numberofschedulepics"); //Setting up number of pics from json data

        } catch (JSONException e) {
            Toast.makeText(getContext(), "Sorry..Something went wrong..!!", Toast.LENGTH_SHORT).show();
        }

        viewPager = (ViewPager) view.findViewById(R.id.schedule_viewpager);
        SchedulePager schedulePager = new SchedulePager(numberofpics, getContext());
        viewPager.setAdapter(schedulePager);

        return view;
    }

}
