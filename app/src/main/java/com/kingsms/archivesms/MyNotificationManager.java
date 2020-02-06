package com.kingsms.archivesms;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RemoteViews;
import android.widget.Toast;


import com.kingsms.archivesms.local_db.MyDatabaseAdapter;
import com.kingsms.archivesms.model.NotificationStatus;
import com.kingsms.archivesms.view.HomeActivity.HomeSenderNamesActivity;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.NOTIFICATION_SERVICE;


public class MyNotificationManager {

    public static final int ID_SMALL_NOTIFICATION = 235;
    private Context mCtx;

    public MyNotificationManager(Context mCtx) {
        this.mCtx = mCtx;
    }


    public void showOldNotification(String notificationId , String time , String senderName, String title, String content) {


        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mCtx);
        Notification notification;
        notification = mBuilder.setSmallIcon(R.drawable.logo).setTicker(title).setWhen(0)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setSmallIcon(R.drawable.logo)
                .setLargeIcon(BitmapFactory.decodeResource(mCtx.getResources(), R.drawable.logo))
                .setContentText(content)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(title))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(content).setSummaryText(""))
                .build();

        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notification.flags |= Notification.FLAG_AUTO_CANCEL;


        NotificationManager notificationManager = (NotificationManager) mCtx.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(ID_SMALL_NOTIFICATION, notification);



    }

    NotificationCompat.Builder builder;
    public void showNewNotification(String notificationId , String time , String senderName, String title, String content)
    {
        Intent intent = new Intent(mCtx, HomeSenderNamesActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(mCtx, 0, intent, PendingIntent.FLAG_ONE_SHOT);
//        PendingIntent pendingIntent=  PendingIntent.getActivity(mCtx, 0,
//                intent,PendingIntent.FLAG_CANCEL_CURRENT);
        String channelId = "Default";
        builder = new  NotificationCompat.Builder(mCtx, channelId)
                .setSmallIcon(R.drawable.sms_logo)
                .setContentTitle(title)
                .setContentText(content).setAutoCancel(true).setContentIntent(pendingIntent);;
        NotificationManager manager = (NotificationManager) mCtx.getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }

        builder.setDefaults(Notification.DEFAULT_SOUND);
        builder.setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        //RemoteViews notificationLayoutExpanded = new RemoteViews(mCtx.getPackageName(), R.layout.item_notification);

       // builder.setCustomContentView(notificationLayoutExpanded);
        manager.notify(0, builder.build());

        //popup



        saveNotificationToLocalDatabase(notificationId,time,senderName,title,content);


    }


    private void addToStatusList(Cursor c) {
        status.add((c.getInt(1)));


    }
    List<Integer>  status = new ArrayList<>() ;
    private  void  getStatus(String senderName)
    {

          status= new ArrayList<>();
        final MyDatabaseAdapter db = new MyDatabaseAdapter(mCtx);
        db.open();
        Cursor c = db.getStatusOfSenderName(senderName);
        if(c != null)
            if (c.moveToNext()) {
                //senderNames = new ArrayList<>();
                do {
                    addToStatusList(c);
                } while (c.moveToNext());
            }
    }
    private  void saveNotificationToLocalDatabase(String notificationId , String time , String senderName, String title, String content)
    {
       /* MyDatabaseAdapter db = new MyDatabaseAdapter(mCtx);
        db.open();
        long id = db.insertNotification(notificationId, senderName, time, content, title);

        getStatus(senderName);
        long idSenderNamesInserted = db.insertSenderNames(senderName,(status.get(0))+1);

        if (id > -1) {
           // Toast.makeText(mCtx, "Notification All Content Inserted Done ", Toast.LENGTH_SHORT).show();
            //TODO send Request to change the status on the server side - by using notification_id -


        } else {

           // Toast.makeText(mCtx, "Notification All Content Not Inserted", Toast.LENGTH_SHORT).show();
        }
        if (idSenderNamesInserted > -1) {
           // Toast.makeText(mCtx, "Sender Name Inserted Done ", Toast.LENGTH_SHORT).show();
            //TODO send Request to change the status on the server side - by using notification_id -

        } else {

                    //db.updateStatusOfNotification(senderName , status.get(0)+1);
           // Toast.makeText(mCtx, "Sender Name Not Inserted", Toast.LENGTH_SHORT).show();
        }
*/

        Intent intent = new Intent(mCtx, HomeSenderNamesActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        mCtx.startActivity(intent);


    }


}

