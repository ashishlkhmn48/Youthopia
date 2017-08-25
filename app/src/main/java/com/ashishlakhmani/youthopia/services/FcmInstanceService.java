package com.ashishlakhmani.youthopia.services;

import android.content.Context;
import android.content.SharedPreferences;

import com.ashishlakhmani.youthopia.R;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class FcmInstanceService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        String recent_token = FirebaseInstanceId.getInstance().getToken();
        SharedPreferences notificationSP = getSharedPreferences(getString(R.string.FCM_PREF), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = notificationSP.edit();
        editor.putString(getString(R.string.FCM_TOKEN),recent_token);
        editor.apply();
    }
}
