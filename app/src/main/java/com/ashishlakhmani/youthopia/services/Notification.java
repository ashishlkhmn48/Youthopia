package com.ashishlakhmani.youthopia.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.NotificationCompat;

import com.ashishlakhmani.youthopia.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;


public class Notification extends BroadcastReceiver {

    private static final int uniqueID = (int) (10000 * Math.random());

    @Override
    public void onReceive(Context context, Intent intent) {
        createNotification(context);
    }

    private void createNotification(final Context context) {
        final NotificationCompat.Builder notification = new NotificationCompat.Builder(context);
        notification.setAutoCancel(true);
        notification.setSmallIcon(R.drawable.notification);
        notification.setWhen(System.currentTimeMillis());
        notification.setContentTitle("You have Registered for Event/s.");
        notification.setContentText("Kindly have a look on Details and Schedule.");
        notification.setDefaults(NotificationCompat.DEFAULT_ALL);


        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                NotificationCompat.BigPictureStyle style = new NotificationCompat.BigPictureStyle();
                style.bigPicture(bitmap);
                notification.setStyle(style);
                //Intent intent = new Intent(context,SplashScreen.class);
                //Another way to get launch intent i.e in this case SplashScreen
                Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());

                PendingIntent pi = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                notification.setContentIntent(pi);

                NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                if (nm != null)
                    nm.notify(uniqueID, notification.build());
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                errorDrawable = context.getResources().getDrawable(R.drawable.logo2, null);
                Bitmap bitmap = ((BitmapDrawable) errorDrawable).getBitmap();
                NotificationCompat.BigPictureStyle style = new NotificationCompat.BigPictureStyle();
                style.bigPicture(bitmap);
                notification.setStyle(style);
                //Intent intent = new Intent(context,SplashScreen.class);
                //Another way to get launch intent i.e in this case SplashScreen
                Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());

                PendingIntent pi = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                notification.setContentIntent(pi);

                NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                if (nm != null)
                    nm.notify(uniqueID, notification.build());
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        Picasso.with(context).load("https://ashishlakhmani.000webhostapp.com/notification_pics/logo3.jpg").into(target);
    }

}
