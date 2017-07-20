package com.roy.movieview.utils.common;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import android.widget.RemoteViews;

import com.roy.movieview.R;
import com.roy.movieview.ui.activity.MainActivity;
import com.roy.movieview.ui.activity.user.DialogActivity;

/**
 * Created by Administrator on 2017/6/30.
 */

public class NotificationHelper {

    public static void createNotification(Context context){
        NotificationManager nm = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notifyIntent = new Intent(context, DialogActivity.class);

        // Sets the Activity to start in a new, empty task
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent notifyPendingIntent =
                PendingIntent.getActivity(context, 0, notifyIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle("MvAPP警告")
                .setContentText("您的账号在其他地方登录,如非本人操作，请重新登录并修改密码")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                .setAutoCancel(true)
                .setContentIntent(notifyPendingIntent);

        nm.notify(100, builder.build());
    }

    public static Notification createProgressNotification(Context context){
        NotificationManager nm = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notifyIntent = new Intent(context, MainActivity.class);

        // Sets the Activity to start in a new, empty task
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent notifyPendingIntent =
                PendingIntent.getActivity(context, 0, notifyIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        RemoteViews views=new RemoteViews(context.getPackageName(),R.layout.view_notification_progress);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle("正在更新")
                .setContent(views)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(notifyPendingIntent);
        Notification notification = builder.build();

        nm.notify(101, notification);
        return notification;
    }

}
