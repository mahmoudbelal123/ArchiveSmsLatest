package com.kingsms.archivesms.view.HomeActivity;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.kingsms.archivesms.R;
import com.kingsms.archivesms.adapters.NotificationsAdapter;
import com.kingsms.archivesms.helper.OnItemClickListener;
import com.kingsms.archivesms.local_db.MyDatabaseAdapter;
import com.kingsms.archivesms.model.NotificationModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeSenderDetailsNotificationsActivity extends AppCompatActivity {

    List<NotificationModel> notificationModelList;
    NotificationModel notificationModel;


    RecyclerView recyclerViewNotifications;
    NotificationsAdapter notificationsAdapter ;
    TextView txtNotFound;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_sender_details_notifications);


        txtNotFound = findViewById(R.id.text_not_found);
        recyclerViewNotifications = findViewById(R.id.recycle_notifications_details);

        notificationModel  = new NotificationModel();
        notificationModelList = new ArrayList<>();


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerViewNotifications.setLayoutManager(mLayoutManager);
        recyclerViewNotifications.setItemAnimator(new DefaultItemAnimator());

        if(getIntent().getStringExtra("sender_name") != null) {
            getAllNotificationsOfSenderName(getIntent().getStringExtra("sender_name"));
            Collections.reverse(notificationModelList);
             notificationsAdapter = new NotificationsAdapter(notificationModelList, this, new OnItemClickListener() {
                @Override
                public void onItemClick(final String item) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(HomeSenderDetailsNotificationsActivity.this);
                    alertDialog.setMessage(getString(R.string.confirm_delete));
                    alertDialog.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteOneNotificationById(item);

                        }
                    });
                    alertDialog.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alertDialog.show();

                }
            });
            recyclerViewNotifications.setAdapter(notificationsAdapter);
        }

        if(notificationModelList.size() == 0)
        {
            txtNotFound.setVisibility(View.VISIBLE);

        }
        else
        {
            txtNotFound.setVisibility(View.GONE);

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(HomeSenderDetailsNotificationsActivity.this, HomeSenderNamesActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public static void clearNotifications(Context context) {
        try{
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancelAll();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        clearNotifications(this);

    }

    private void deleteOneNotificationById(String notificationId){
        MyDatabaseAdapter myDatabaseAdapter = new MyDatabaseAdapter(this);
        myDatabaseAdapter.open();
         boolean isDeleted =   myDatabaseAdapter.deleteSpecificNotificationById(notificationId);
        getAllNotificationsOfSenderName(getIntent().getStringExtra("sender_name"));

        Collections.reverse(notificationModelList);
        notificationsAdapter = new NotificationsAdapter(notificationModelList, this, new OnItemClickListener() {
            @Override
            public void onItemClick(final String item) {


                AlertDialog.Builder alertDialog = new AlertDialog.Builder(HomeSenderDetailsNotificationsActivity.this);
                alertDialog.setMessage(getString(R.string.confirm_delete));
                alertDialog.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteOneNotificationById(item);

                    }
                });
                alertDialog.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertDialog.show();

            }
        });
        if(notificationModelList.size() == 0)
        {
            txtNotFound.setVisibility(View.VISIBLE);

        }
        else
        {
            txtNotFound.setVisibility(View.GONE);

        }
        recyclerViewNotifications.setAdapter(notificationsAdapter);

    }


    private void getAllNotificationsOfSenderName(String senderName)
    {
        notificationModelList = new ArrayList<>();

        MyDatabaseAdapter db = new MyDatabaseAdapter(this);
        db.open();
        Cursor c = db.getNotificationsBySenderName(senderName);

        if (c.moveToNext()) {
//            notificationModel  = new NotificationModel();

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
