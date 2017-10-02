package com.ashishlakhmani.youthopia.activity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.ashishlakhmani.youthopia.R;
import com.ashishlakhmani.youthopia.adapter.GalleryPager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HomePicViewActivity extends AppCompatActivity { //implements HomePhotosAdapter.OnClickInAdapter{

    private int numberofpics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Change to full screen
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                        View.SYSTEM_UI_FLAG_FULLSCREEN);

        //Change to landscape mode
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        setContentView(R.layout.activity_home_pic_view);

        Bundle bundle = getIntent().getExtras();
        recyclerViewTask(bundle);

    }

    private void recyclerViewTask(Bundle bundle) {

        try {
            JSONObject jsonObject = new JSONObject(SplashScreen.jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("number");
            JSONObject innerJsonOject = jsonArray.getJSONObject(0);
            numberofpics = innerJsonOject.getInt("numberofgallerypics"); //Setting up number of pics from json data

        } catch (JSONException e) {
            Toast.makeText(this, "Sorry..Something went wrong..!!", Toast.LENGTH_SHORT).show();
        }

        ViewPager viewPager = (ViewPager) findViewById(R.id.homePicViewPager);
        int position = bundle.getInt("data");
        GalleryPager galleryPager = new GalleryPager(numberofpics, this);
        viewPager.setAdapter(galleryPager);
        viewPager.setCurrentItem(position, false);
    }
}
