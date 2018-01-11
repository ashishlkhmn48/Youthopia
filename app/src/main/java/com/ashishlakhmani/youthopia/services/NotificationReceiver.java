package com.ashishlakhmani.youthopia.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import com.ashishlakhmani.youthopia.R;
import com.ashishlakhmani.youthopia.activity.Home;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.Random;

import static android.content.Context.NOTIFICATION_SERVICE;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String heading = intent.getStringExtra("heading");
        String picture = intent.getStringExtra("picture_link");
        createNotification(context, heading, picture);
    }

    private void createNotification(final Context context, String heading, String picture) {

        Random random = new Random();
        int num = random.nextInt(999999999);

        Bitmap largeIcon;
        try {
            largeIcon = getBitmapFromURL(picture);
        } catch (Exception e) {
            largeIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.logo2);
        }

        final NotificationCompat.Builder notification = new NotificationCompat.Builder(context);
        notification.setAutoCancel(true);
        notification.setSmallIcon(R.drawable.notification);
        notification.setWhen(System.currentTimeMillis());
        notification.setContentTitle("Youthopia Event");
        notification.setContentText("Get Ready for " + heading);
        notification.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(largeIcon));
        notification.setDefaults(NotificationCompat.DEFAULT_ALL);

        Intent intent = new Intent(context, Home.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pi = PendingIntent.getActivity(context, num, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(pi);
        NotificationManager nm = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        if (nm != null) {
            nm.notify(num, notification.build());
        }

        SharedPreferences sharedPreferences = context.getSharedPreferences("notification", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(heading.trim());
        editor.apply();

    }

    public Bitmap getBitmapFromURL(String src) throws IOException {
        java.net.URL url = new java.net.URL(src);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoInput(true);
        connection.connect();
        InputStream input = connection.getInputStream();
        return BitmapFactory.decodeStream(input);
    }
}

