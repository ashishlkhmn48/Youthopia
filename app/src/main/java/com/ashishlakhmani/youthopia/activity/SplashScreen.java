package com.ashishlakhmani.youthopia.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ashishlakhmani.youthopia.R;
import com.ashishlakhmani.youthopia.classes.InsertToDatabase;
import com.ashishlakhmani.youthopia.classes.LoadJson;
import com.ashishlakhmani.youthopia.fragment.NetworkError;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;

public class SplashScreen extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

    GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 13;
    public SignInButton signInButton;
    public ProgressBar progressBar;

    public static String jsonString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        signInButton = (SignInButton) findViewById(R.id.signInButton);
        signInButton.setOnClickListener(this);

        //Check if logged already logged in
        SharedPreferences login = getSharedPreferences("login", Context.MODE_PRIVATE);
        if (login.contains("email")) {
            signInButton.setVisibility(View.INVISIBLE);
            loginTask();
        }

        SharedPreferences checkFirstTime = getSharedPreferences("check", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = checkFirstTime.edit();
        editor.putBoolean("isFirstTime", true);
        editor.apply();

        // [START config_signin]
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(new Scope("https://www.googleapis.com/auth/contacts.readonly"))
                .build();
        // [END config_signin]

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.signInButton)
            signIn();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void signIn() {
        if (!isNetworkAvailable())
            Toast.makeText(this, "Connect to Internet..", Toast.LENGTH_SHORT).show();
        else {
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            startActivityForResult(signInIntent, RC_SIGN_IN);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful
                progressBar.setVisibility(View.VISIBLE);
                handleResult(result);
            } else
                Toast.makeText(this, "Error Inside", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleResult(GoogleSignInResult result) {
        GoogleSignInAccount signInAccount = result.getSignInAccount();
        SharedPreferences login = getSharedPreferences("login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = login.edit();

        editor.putString("email", signInAccount.getEmail());
        editor.putString("name", signInAccount.getDisplayName());
        editor.putBoolean("isFirstTimeLogin", true);
        editor.putBoolean("notificationFlag", true);
        editor.apply();
        signInButton.setVisibility(View.INVISIBLE);
        loginTask();
    }

    private void loginTask() {
        if (!isNetworkAvailable())
            updateUI("network_error");
        else
            performTaskAhead();
    }


    private void performTaskAhead() {
        SharedPreferences notificationSP = getSharedPreferences(getString(R.string.FCM_PREF), Context.MODE_PRIVATE);
        String token = notificationSP.getString(getString(R.string.FCM_TOKEN), "");

        SharedPreferences login = getSharedPreferences("login", Context.MODE_PRIVATE);
        String email = login.getString("email", "");

        LoadJson loadJson = new LoadJson(this, this);
        loadJson.execute();
        InsertToDatabase insertToDatabase = new InsertToDatabase(this);
        insertToDatabase.execute(token, email);

    }

    private boolean isNetworkAvailable() {
        //Check if internet is ON or not..(Both Wifi and MobileData)
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
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
        }
        return (haveConnectedMobile || haveConnectedWifi);
    }

    public void updateUI(String TAG) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_UNSET);
        fragmentTransaction.replace(R.id.network_error_layout, new NetworkError(), TAG);
        fragmentTransaction.commit(); // save the changes
    }

}