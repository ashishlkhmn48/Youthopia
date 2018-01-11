package com.ashishlakhmani.youthopia.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.ashishlakhmani.youthopia.classes.CheckParticipation;
import com.ashishlakhmani.youthopia.R;
import com.ashishlakhmani.youthopia.activity.Home;


public class EventCommonFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_eventcommon, container, false);
        ((Home) getActivity()).changeConstraintLayout(getArguments().getString("eventName"), true);

        DetailsFragment detailsFragment = new DetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("detailsLink", getArguments().getString("detailsLink"));
        detailsFragment.setArguments(bundle);
        loadFragmentBottomNavigation(detailsFragment, "details");

        BottomNavigationView navigation = (BottomNavigationView) view.findViewById(R.id.bottomNavigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.details:
                        DetailsFragment detailsFragment = new DetailsFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("detailsLink", getArguments().getString("detailsLink"));
                        detailsFragment.setArguments(bundle);
                        loadFragmentBottomNavigation(detailsFragment, "details");
                        break;

                    case R.id.register:
                        SharedPreferences sp = getContext().getSharedPreferences("login", Context.MODE_PRIVATE);
                        String email = sp.getString("email", null);
                        CheckParticipation checkParticipation = new CheckParticipation(getContext(), getArguments().getString("eventName"));
                        checkParticipation.execute(email);
                }
                return true;
            }
        });

        navigation.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
            }
        });
        return view;
    }

    public void loadFragmentBottomNavigation(Fragment fragment, String TAG) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_UNSET);
        fragmentTransaction.replace(R.id.content, fragment, TAG);
        fragmentTransaction.commit();
    }
}
