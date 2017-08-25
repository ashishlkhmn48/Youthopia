package com.ashishlakhmani.youthopia.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ashishlakhmani.youthopia.R;
import com.ashishlakhmani.youthopia.activity.Home;

public class AllEventsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((Home) getContext()).changeConstraintLayout("EVENTS", true);
        return inflater.inflate(R.layout.fragment_allevents, container, false);
    }
}
