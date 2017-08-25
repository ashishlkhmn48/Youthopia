package com.ashishlakhmani.youthopia.classes;


import android.content.Context;
import android.os.AsyncTask;

import com.ashishlakhmani.youthopia.activity.Home;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


public class CheckNotification extends AsyncTask<String, Void, String> {

    private Context context;

    public CheckNotification(Context context) {
        this.context = context;
    }


    @Override
    protected String doInBackground(String... params) {
        String login_url = "https://ashishlakhmani.000webhostapp.com/php_files/checkNotification.php";
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
        if (!s.equals("Connection Problem!!!")) {
            if (s.equals("registered")) {
                ((Home)context).setNotificationsActive();
            }
        }
    }
}
