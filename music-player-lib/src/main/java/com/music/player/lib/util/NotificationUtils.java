package com.music.player.lib.util;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v7.app.NotificationCompat;

/**
 * @time 2017/2/24 17:42
 * @des $通知工具类
 */
public class NotificationUtils {
    /**
     * 发送一条通知
     * @param context
     * @param ticker
     * @param title
     * @param bigTitle
     * @param minContent
     * @param content
     * @param bigContent
     * @param sumText
     * @param notionID
     * @param clazz
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)

    public static void sendNotification(Context context, int minLogo,int bigLogo,String ticker,String title,String bigTitle,
                  String minContent, String content, String bigContent, String sumText, int notionID,Class clazz) {

        NotificationManager mManager = (NotificationManager) context.getSystemService(Service.NOTIFICATION_SERVICE);
        Notification notification=null;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            notification = new Notification(bigLogo,minContent ,
                    System.currentTimeMillis());
            notification.defaults=Notification.DEFAULT_ALL;

        } else if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB&&Build.VERSION.SDK_INT<Build.VERSION_CODES.JELLY_BEAN){
            NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                    .setSmallIcon(bigLogo)
                    .setTicker(ticker)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setWhen(System.currentTimeMillis())
                    .setPriority(Notification.PRIORITY_MAX)//优先级
                    .setDefaults(Notification.DEFAULT_ALL);
            notification= builder.build();

        }else if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN){
            //设置基本参数
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            builder.setTicker(ticker);
            builder.setContentTitle(title);
            builder.setContentText(bigContent);
            builder.setSmallIcon(minLogo);
            builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), bigLogo));
            //设置大通知栏样式
            android.support.v4.app.NotificationCompat.BigTextStyle style = new android.support.v4.app.NotificationCompat.BigTextStyle();
            style.setBigContentTitle(bigTitle);
            style.bigText(bigContent);
            style.setSummaryText(sumText);
            builder.setStyle(style);

            builder.setPriority(Notification.PRIORITY_MAX);//优先级
            builder.setDefaults(NotificationCompat.DEFAULT_ALL);//默认使用闪光灯，铃声，震动

//            builder.setAutoCancel(true);//点击后消失
            //设置点击意图
            if(clazz!=null){
                Intent intent = new Intent(context,clazz);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent pIntent = PendingIntent.getActivity(context,1,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentIntent(pIntent);
            }

            notification = builder.build();
        }
        if(notification!=null){
            mManager.notify(notionID, notification);
        }
    }
}
