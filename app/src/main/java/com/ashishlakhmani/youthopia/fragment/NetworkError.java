package com.ashishlakhmani.youthopia.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ashishlakhmani.youthopia.classes.InsertToDatabase;
import com.ashishlakhmani.youthopia.classes.LoadJsonNetworkError;
import com.ashishlakhmani.youthopia.R;

public class NetworkError extends Fragment {

    Button retryButton;
    ProgressBar networkErrorProgressbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_network_error, container, false);

        networkErrorProgressbar = (ProgressBar)view.findViewById(R.id.network_error_progressbar);
        retryButton = (Button)view.findViewById(R.id.retry_button);

        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginTask(view);
            }
        });

        return view;
    }


    private void loginTask(View view) {
        if (!isNetworkAvailable())
            Toast.makeText(getContext(),"Please connect to internet..!!",Toast.LENGTH_SHORT).show();
        else
            performTaskAhead(view);
    }

    private void performTaskAhead(View view) {
        SharedPreferences notificationSP = getContext().getSharedPreferences(getString(R.string.FCM_PREF), Context.MODE_PRIVATE);
        String token = notificationSP.getString(getString(R.string.FCM_TOKEN), "");

        SharedPreferences login = getContext().getSharedPreferences("login", Context.MODE_PRIVATE);
        String email = login.getString("email","");

        LoadJsonNetworkError loadJsonNetworkError = new LoadJsonNetworkError(getContext(), getActivity(),view);
        loadJsonNetworkError.execute();

        InsertToDatabase insertToDatabase = new InsertToDatabase(getContext());
        insertToDatabase.execute(token,email);
    }

    private boolean isNetworkAvailable() {
        //Check if internet is ON or not..(Both Wifi and MobileData)
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                // connected to wifi
                haveConnectedWifi = true;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                // connected to the mobile provider's data plan
                haveConnectedMobile = true;
            }
        }
        return (haveConnectedMobile || haveConnectedWifi);
    }

}
