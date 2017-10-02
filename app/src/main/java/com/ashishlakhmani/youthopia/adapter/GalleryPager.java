package com.ashishlakhmani.youthopia.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ashishlakhmani.youthopia.R;
import com.ashishlakhmani.youthopia.classes.TouchImageView;
import com.squareup.picasso.Picasso;


public class GalleryPager extends PagerAdapter {
    private int numberofpics;
    private Context context;
    private static final String URL = "https://ashishlakhmani.000webhostapp.com/pics_large/";

    public GalleryPager(int numberofpics, Context context) {
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
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.gallery_layout, container, false);


        TouchImageView imageView = (TouchImageView) view.findViewById(R.id.pic_view_gallery);
        Picasso.with(context)
                .load(URL + (position + 1) + ".jpg")
                .placeholder(R.drawable.placeholder_album)
                .into(imageView);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.invalidate();
    }


}
