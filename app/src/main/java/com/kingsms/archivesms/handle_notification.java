package com.kingsms.archivesms;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class handle_notification extends FirebaseMessagingService {

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
        mNotificationManager.showNewNotification(notificationId ,time ,sender_name ,  title , content);
    }


}

