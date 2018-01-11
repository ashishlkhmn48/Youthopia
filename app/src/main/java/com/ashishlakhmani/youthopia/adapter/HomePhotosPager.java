package com.ashishlakhmani.youthopia.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ashishlakhmani.youthopia.R;
import com.ashishlakhmani.youthopia.activity.HomePicViewActivity;
import com.squareup.picasso.Picasso;

public class HomePhotosPager extends PagerAdapter {
    private int numberofpics;
    private Context context;
    private static final String URL = "https://ashishlakhmani.000webhostapp.com/pics_small/";

    public HomePhotosPager(int numberofpics, Context context) {
        this.numberofpics = numberofpics;
        this.context = context;
    }

    @Override
    public int getCount() {
        return numberofpics;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.photos_layout, container, false);


        ImageView imageView = (ImageView) view.findViewById(R.id.homePic);
        Picasso.with(context)
                .load(URL + (position + 1) + ".jpg")
                .placeholder(R.drawable.placeholder_album)
                .into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, HomePicViewActivity.class);
                intent.putExtra("data", position);
                context.startActivity(intent);
            }
        });

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }
}
