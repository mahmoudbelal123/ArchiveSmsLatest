package com.kingsms.archivesms;

import android.database.Cursor;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.kingsms.archivesms.local_db.MyDatabaseAdapter;
import com.kingsms.archivesms.view.HomeActivity.HomeSenderNamesActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import me.leolin.shortcutbadger.ShortcutBadger;

public class handle_notification extends FirebaseMessagingService {

    private void addToStatusList(Cursor c) {
        status.add((c.getInt(1)));


    }
    List<Integer> status = new ArrayList<>() ;
    private  void  getStatus()
    {

        status= new ArrayList<>();
        final MyDatabaseAdapter db = new MyDatabaseAdapter(this);
        db.open();
        Cursor c = db.getAllUnread();
        if(c != null)
            if (c.moveToNext()) {
                //senderNames = new ArrayList<>();
                do {
                    addToStatusList(c);
                } while (c.moveToNext());
            }
    }
    @Override
    public void onCreate() {
        super.onCreate();

       // getStatus();
        //if(status != null)
        //ShortcutBadger.applyCount(handle_notification.this,status.size() );

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            try {
                Map<String , String> map =  remoteMessage.getData();
                sendPushNotification(map);


            } catch (Exception e) {

                e.getMessage();
            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {

        }

    }
    @Override
    public void onNewToken(String token) {
    }


    private void sendPushNotification(Map map) {
        String notificationId = (String) map.get("notification_id");
        String title = (String) map.get("title");
        String time = (String) map.get("time");
        String sender_name = (String) map.get("sender_name");
        String content = (String) map.get("content");

        MyNotificationManager mNotificationManager = new MyNotificationManager(getBaseContext());
        Date currentTime = Calendar.getInstance().getTime();

        if(time == null)
             time = currentTime.toString();
        mNotificationManager.showNewNotification(notificationId ,time ,sender_name ,  title , content);


    }


}

