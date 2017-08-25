package com.ashishlakhmani.youthopia.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.NotificationCompat;

import com.ashishlakhmani.youthopia.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.squareup.picasso.Picasso;


public class FcmMessagingService extends FirebaseMessagingService {

    private static final int uniqueID = (int)(10000*Math.random());

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String title = remoteMessage.getData().get("title");
        String message = remoteMessage.getData().get("message");
        String picture = remoteMessage.getData().get("picture");

        final NotificationCompat.Builder notification = new NotificationCompat.Builder(this);
        notification.setAutoCancel(true);
        notification.setSmallIcon(R.drawable.notification);
        notification.setWhen(System.currentTimeMillis());
        notification.setContentTitle(title);
        notification.setContentText(message);
        notification.setDefaults(NotificationCompat.DEFAULT_ALL);

        Bitmap largeIcon;
        try {
            largeIcon = Picasso.with(this).load(picture).get();
        } catch (Exception e) {
            largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.logo2);
        }
        NotificationCompat.BigPictureStyle style = new NotificationCompat.BigPictureStyle();
        style.bigPicture(largeIcon);
        notification.setStyle(style);

        //Intent intent = new Intent(context,SplashScreen.class);
        //Another way to get launch intent i.e in this case SplashScreen
        Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
        PendingIntent pi = PendingIntent.getActivity(FcmMessagingService.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(pi);

        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(uniqueID, notification.build());
    }
}