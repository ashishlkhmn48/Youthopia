package com.ashishlakhmani.youthopia.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ashishlakhmani.youthopia.R;
import com.ashishlakhmani.youthopia.activity.SplashScreen;
import com.ashishlakhmani.youthopia.adapter.SuggestionAdapter;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


public class FragmentList extends Fragment {

    public static HashMap<String, String> eventDetails = new HashMap<>();
    public static ArrayList<String> suggestions = new ArrayList<String>();
    SuggestionAdapter suggestionAdapter;
    static boolean isTypedOnce = false;
    MaterialSearchView searchView;
    String[] eventCategories = {"cultural", "technical", "gaming", "informal", "literary"};


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        if (eventDetails.isEmpty()) {
            try {
                JSONObject jsonObject = new JSONObject(SplashScreen.jsonString);
                for (int i = 0; i < eventCategories.length; i++) {
                    JSONArray jsonArray = jsonObject.getJSONArray(eventCategories[i]);
                    for (int j = 0; j < jsonArray.length(); j++) {
                        JSONObject innerJsonOject = jsonArray.getJSONObject(j);
                        eventDetails.put(innerJsonOject.getString("eventname"), innerJsonOject.getString("detailslink"));
                        suggestions.add(innerJsonOject.getString("eventname"));
                        Collections.sort(suggestions);
                    }
                }
            } catch (Exception e) {}
        }

        searchView = (MaterialSearchView) getActivity().findViewById(R.id.search_view);

        //Recycler View initialization
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        suggestionAdapter = new SuggestionAdapter(getContext(), eventDetails, suggestions, searchView);
        recyclerView.setAdapter(suggestionAdapter);

        return view;
    }


    public boolean filterTask(String newText) {
        if (!newText.isEmpty()) {
            isTypedOnce = true;
            newText = newText.toLowerCase();
            ArrayList<String> newList = new ArrayList<>();
            for (String sl : eventDetails.keySet()) {
                String name = sl.toLowerCase();
                if (name.contains(newText)) {
                    newList.add(sl);
                }
            }
            Collections.sort(newList);
            suggestionAdapter.setFilter(newList);
            return true;
        } else {
            if (!isTypedOnce)
                return false;
            else {
                ArrayList<String> newList = new ArrayList<String>();
                suggestionAdapter.setFilter(newList);
                return true;
            }
        }
    }
}
