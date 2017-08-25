package com.ashishlakhmani.youthopia.classes;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.ashishlakhmani.youthopia.activity.Home;
import com.ashishlakhmani.youthopia.fragment.CommonRegistrationFragment;
import com.ashishlakhmani.youthopia.fragment.SuccessFragment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


public class CheckParticipation extends AsyncTask<String, Void, String> {

    private Context context;
    private String eventName;
    private ProgressDialog progressDialog;

    public CheckParticipation(Context context, String eventName) {
        this.context = context;
        this.eventName = eventName;
    }

    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Youthopia2k16");
        progressDialog.setMessage("Please Wait..");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... params) {
        String login_url = "https://ashishlakhmani.000webhostapp.com/php_files/check.php";
        try {
            String email = params[0];

            URL url = new URL(login_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);

            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

            String post_data =
                    URLEncoder.encode("event_name", "UTF-8") + "=" + URLEncoder.encode(eventName, "UTF-8") + "&" +
                            URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");

            bufferedWriter.write(post_data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "ISO-8859-1"));
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
            return "Connection Problem!!!";
        }
    }

    @Override
    protected void onPostExecute(String s) {
        progressDialog.dismiss();
        if (!s.equals("Connection Problem!!!")) {
            if (s.equals("registered")) {
                SuccessFragment successFragment = new SuccessFragment();
                ((Home) context).loadFragmentBottomNavigation(successFragment, "register");
            } else {
                CommonRegistrationFragment commonRegistrationFragment = new CommonRegistrationFragment();
                Bundle bundle = new Bundle();
                bundle.putString("eventName",eventName);
                commonRegistrationFragment.setArguments(bundle);
                ((Home) context).loadFragmentBottomNavigation(commonRegistrationFragment, "register");
            }
        }
        else {
            Toast.makeText(context, "Some Network Issues.Please restart the App.", Toast.LENGTH_SHORT).show();
        }
    }
}
