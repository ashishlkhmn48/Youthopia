package com.ashishlakhmani.youthopia.classes;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.widget.Toast;

import com.ashishlakhmani.youthopia.activity.Home;
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


public class FormValidationBackground extends AsyncTask<String, Void, String> {

    private Context context;
    private ProgressDialog progressDialog;

    public FormValidationBackground(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Youthopia2k16");
        progressDialog.setMessage("Submitting");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... params) {
        String login_url = "https://ashishlakhmani.000webhostapp.com/php_files/insert.php";
        try {
            String eventName = params[0];
            String name = params[1];
            String gender = params[2];
            String numOfMembers = params[3];
            String email = params[4];
            String phno = params[5];
            String collegeName = params[6];

            URL url = new URL(login_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);

            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

            String post_data =
                    URLEncoder.encode("event_name", "UTF-8") + "=" + URLEncoder.encode(eventName, "UTF-8") + "&" +
                            URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8") + "&" +
                            URLEncoder.encode("gender", "UTF-8") + "=" + URLEncoder.encode(gender, "UTF-8") + "&" +
                            URLEncoder.encode("number_of_members", "UTF-8") + "=" + URLEncoder.encode(numOfMembers, "UTF-8") + "&" +
                            URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") + "&" +
                            URLEncoder.encode("phone_number", "UTF-8") + "=" + URLEncoder.encode(phno, "UTF-8") + "&" +
                            URLEncoder.encode("college_name", "UTF-8") + "=" + URLEncoder.encode(collegeName, "UTF-8");

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
            SuccessFragment successFragment = new SuccessFragment();
            ((Home) context).loadFragmentBottomNavigation(successFragment, "register");
            Toast.makeText(context, "Congratulations..! You have been Registered.", Toast.LENGTH_LONG).show();
            SharedPreferences sp = context.getSharedPreferences("login",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.remove("notificationFlag");
            editor.putBoolean("notificationFlag",true);
            editor.apply();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(context, Home.class);
                    context.startActivity(intent);
                    ((Home) context).finish();
                }
            }, 500);
        }
    }
}
