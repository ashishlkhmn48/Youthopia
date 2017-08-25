package com.ashishlakhmani.youthopia.classes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ashishlakhmani.youthopia.R;
import com.ashishlakhmani.youthopia.activity.Home;
import com.ashishlakhmani.youthopia.activity.SplashScreen;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoadJson extends AsyncTask<Void, Void, String> {
    private Context context;
    private Activity activity;

    public LoadJson(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        ProgressBar progressBar = (ProgressBar) activity.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected String doInBackground(Void... params) {
        String login_url = "https://ashishlakhmani.000webhostapp.com/json_file/details.json";

        try {
            URL url = new URL(login_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            StringBuilder sb = new StringBuilder("");
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();

            return sb.toString();

        } catch (Exception e) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(String result) {
        SplashScreen.jsonString = result;
        if (SplashScreen.jsonString != null) {
            Intent mainIntent = new Intent(context, Home.class);
            activity.finish();
            context.startActivity(mainIntent);
        } else {
            Toast.makeText(context, "Some Network Issues.Please restart the App.", Toast.LENGTH_SHORT).show();
            Intent mainIntent = new Intent(context, SplashScreen.class);
            activity.finish();
            context.startActivity(mainIntent);
        }
        ProgressBar progressBar = (ProgressBar) activity.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
    }
}
