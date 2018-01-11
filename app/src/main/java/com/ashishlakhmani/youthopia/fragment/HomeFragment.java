package com.ashishlakhmani.youthopia.fragment;


import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ashishlakhmani.youthopia.R;
import com.ashishlakhmani.youthopia.activity.Home;
import com.ashishlakhmani.youthopia.activity.SplashScreen;
import com.ashishlakhmani.youthopia.adapter.HomePhotosPager;

import org.json.JSONArray;
import org.json.JSONObject;


public class HomeFragment extends Fragment {

    ViewPager viewPager;
    int numberofpics;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //Very Important..Concept to do any task while moving from one fragment to another.
        getFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                AppBarLayout appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.appBarLayout);
                if (getFragmentManager().getBackStackEntryCount() > 0) {
                    ViewCompat.setElevation(appBarLayout, 15);
                    Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
                    toolbar.setNavigationIcon(R.drawable.ic_action_navigation_arrow_back);

                    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getFragmentManager().popBackStack();
                        }
                    });
                } else {
                    ViewCompat.setElevation(appBarLayout, 0);
                    ((Home) getActivity()).navigationDrawerTask();
                    ((Home) getActivity()).changeConstraintLayout(null, false);
                }
            }
        });

        try {
            JSONObject jsonObject = new JSONObject(SplashScreen.jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("number");
            JSONObject innerJsonOject = jsonArray.getJSONObject(0);
            numberofpics = innerJsonOject.getInt("numberofgallerypics"); //Setting up number of pics from json data
        } catch (Exception e) {
        }

        ViewPager viewPager = (ViewPager)view.findViewById(R.id.homeViewPager);
        HomePhotosPager galleryPager = new HomePhotosPager(numberofpics, getContext());
        viewPager.setPageMargin(30);
        viewPager.setAdapter(galleryPager);
        return view;
    }

}

