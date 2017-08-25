package com.ashishlakhmani.youthopia.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ashishlakhmani.youthopia.R;
import com.ashishlakhmani.youthopia.activity.HomePicViewActivity;
import com.squareup.picasso.Picasso;


public class HomePhotosAdapter extends RecyclerView.Adapter {
    private Context context;
    private int numberofpics;

    private String URL = "https://ashishlakhmani.000webhostapp.com/pics_large/";

    public HomePhotosAdapter(Context context, int numberofpics) {
        this.context = context;
        this.numberofpics = numberofpics;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.photos_layout, parent, false);
        return new MyViewHolder(view); // pass the view to View Holder
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        Picasso.with(context)
                .load(URL + (position+1) + ".jpg")
                .placeholder(R.drawable.placeholder_album)
                .into(((MyViewHolder) holder).image);

        ((MyViewHolder) holder).image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, HomePicViewActivity.class);
                intent.putExtra("data",position);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return numberofpics;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        // init the item view's
        ImageView image;

        public MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            image = (ImageView) itemView.findViewById(R.id.homePic);
        }
    }
}
