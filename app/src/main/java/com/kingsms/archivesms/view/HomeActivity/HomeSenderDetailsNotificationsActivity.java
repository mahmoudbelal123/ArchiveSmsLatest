package com.kingsms.archivesms.view.HomeActivity;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.kingsms.archivesms.R;
import com.kingsms.archivesms.adapters.NotificationsAdapter;
import com.kingsms.archivesms.helper.OnItemClickListener;
import com.kingsms.archivesms.local_db.MyDatabaseAdapter;
import com.kingsms.archivesms.model.NotificationModel;

import java.util.ArrayList;
import java.util.List;

public class HomeSenderDetailsNotificationsActivity extends AppCompatActivity {

    List<NotificationModel> notificationModelList;
    NotificationModel notificationModel;


    RecyclerView recyclerViewNotifications;
    NotificationsAdapter notificationsAdapter ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_sender_details_notifications);


        recyclerViewNotifications = findViewById(R.id.recycle_notifications_details);

        notificationModel  = new NotificationModel();
        notificationModelList = new ArrayList<>();


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewNotifications.setLayoutManager(mLayoutManager);
        recyclerViewNotifications.setItemAnimator(new DefaultItemAnimator());

        if(getIntent().getStringExtra("sender_name") != null) {
            getAllNotificationsOfSenderName(getIntent().getStringExtra("sender_name"));
             notificationsAdapter = new NotificationsAdapter(notificationModelList, this, new OnItemClickListener() {
                @Override
                public void onItemClick(String item) {
                    deleteOneNotificationById(item);
                }
            });
            recyclerViewNotifications.setAdapter(notificationsAdapter);
        }
    }

    private void deleteOneNotificationById(String notificationId){
        MyDatabaseAdapter myDatabaseAdapter = new MyDatabaseAdapter(this);
        myDatabaseAdapter.open();
         boolean isDeleted =   myDatabaseAdapter.deleteSpecificNotificationById(notificationId);
        getAllNotificationsOfSenderName(getIntent().getStringExtra("sender_name"));

        notificationsAdapter = new NotificationsAdapter(notificationModelList, this, new OnItemClickListener() {
            @Override
            public void onItemClick(String item) {
                deleteOneNotificationById(item);
            }
        });
        recyclerViewNotifications.setAdapter(notificationsAdapter);
    }


    private void getAllNotificationsOfSenderName(String senderName)
    {
        MyDatabaseAdapter db = new MyDatabaseAdapter(this);
        db.open();
        Cursor c = db.getNotificationsBySenderName(senderName);

        if (c.moveToFirst()) {
//            notificationModel  = new NotificationModel();
            notificationModelList = new ArrayList<>();

            do {
                addToNotificationList(c);
            } while (c.moveToNext());

        }
    }

    private void addToNotificationList(Cursor c) {

        notificationModel  = new NotificationModel();

        notificationModel.setTitle(c.getString(1));
        notificationModel.setTime(c.getString(2));
        notificationModel.setContent(c.getString(3));
        notificationModel.setNotificationId(c.getString(4));
        notificationModelList.add(notificationModel);
    }

}
