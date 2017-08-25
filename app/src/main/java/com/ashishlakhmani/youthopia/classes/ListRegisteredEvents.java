package com.ashishlakhmani.youthopia.classes;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ashishlakhmani.youthopia.adapter.EventsAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;


public class ListRegisteredEvents extends AsyncTask<String, Void, ArrayList<String>> {

    private Context context;
    private RecyclerView recyclerView;
    private JSONArray jsonArray;
    private ProgressBar progressBar;

    public ListRegisteredEvents(Context context, RecyclerView recyclerView, JSONArray jsonArray, ProgressBar progressBar) {
        this.context = context;
        this.recyclerView = recyclerView;
        this.jsonArray = jsonArray;
        this.progressBar = progressBar;
    }

    @Override
    protected ArrayList<String> doInBackground(String... params) {
        String login_url = "https://ashishlakhmani.000webhostapp.com/php_files/list.php";
        ArrayList<String> list = new ArrayList<String>();
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

            try {
                JSONArray jsonArray = new JSONArray(sb.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    list.add(jsonObject.getString("event"));
                }
            } catch (Exception e) {
            }

            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            return list;

        } catch (Exception e) {
            list.add("error");
            return list;
        }
    }

    @Override
    protected void onPostExecute(ArrayList<String> list) {
        progressBar.setVisibility(View.GONE);
        if (!list.isEmpty()) {
            if (list.get(0).equals("error")) {
                Toast.makeText(context, "Please Connect to the Internet and Restart the App!!", Toast.LENGTH_LONG).show();
            } else {
                EventsAdapter eventsAdapter = new EventsAdapter(context, jsonArray, list);
                recyclerView.setAdapter(eventsAdapter);
            }
        } else {
            EventsAdapter eventsAdapter = new EventsAdapter(context, jsonArray, list);
            recyclerView.setAdapter(eventsAdapter);
        }
    }


}
