package com.kingsms.archivesms.view.HomeActivity;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kingsms.archivesms.R;
import com.kingsms.archivesms.adapters.NotificationsAdapter;
import com.kingsms.archivesms.apiClient.ApiClient;
import com.kingsms.archivesms.apiClient.ApiInterface;
import com.kingsms.archivesms.helper.OnItemClickListener;
import com.kingsms.archivesms.helper.Utilities;
import com.kingsms.archivesms.local_db.MyDatabaseAdapter;
import com.kingsms.archivesms.model.NotificationModel;
import com.kingsms.archivesms.model.activation_code.ActivationResponse;
import com.kingsms.archivesms.model.confirm_message_delivery.ConfirmMessageDeliveryResponse;
import com.kingsms.archivesms.model.confirm_message_delivery.ConfirmMessageRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeSenderDetailsNotificationsActivity extends AppCompatActivity {

    List<NotificationModel> notificationModelList;
    NotificationModel notificationModel;

    List<Integer> listIds = new ArrayList<>();
    List<String> notificationIds;

    RecyclerView recyclerViewNotifications;
    NotificationsAdapter notificationsAdapter;
    TextView txtNotFound;
    ImageView imageEmptyInbox;
    int i = 0;

    public static void clearNotifications(Context context) {
        try {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancelAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_sender_details_notifications);


        txtNotFound = findViewById(R.id.text_not_found);
        imageEmptyInbox = findViewById(R.id.image_empty);
        recyclerViewNotifications = findViewById(R.id.recycle_notifications_details);

        notificationModel = new NotificationModel();
        notificationModelList = new ArrayList<>();


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerViewNotifications.setLayoutManager(mLayoutManager);
        recyclerViewNotifications.setItemAnimator(new DefaultItemAnimator());

        if (getIntent().getStringExtra("sender_name") != null) {
            getAllNotificationsOfSenderName(getIntent().getStringExtra("sender_name"));
            Collections.reverse(notificationModelList);
            notificationsAdapter = new NotificationsAdapter(notificationModelList, this, new OnItemClickListener() {
                @Override
                public void onItemClick(final String item, int position) {
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

        if (notificationModelList.size() == 0) {
            txtNotFound.setVisibility(View.VISIBLE);
            imageEmptyInbox.setVisibility(View.VISIBLE);
            if (getIntent().getStringExtra("sender_name") != null){
               String senderNAme =  getIntent().getStringExtra("sender_name");
               MyDatabaseAdapter myDatabaseAdapter = new MyDatabaseAdapter(this);
               myDatabaseAdapter.open();
               myDatabaseAdapter.deleteSenderName(senderNAme);

            }

        } else {
            txtNotFound.setVisibility(View.GONE);
            imageEmptyInbox.setVisibility(View.GONE);

        }


        getAllNotificationIds();
    }
    private void getAllNotificationIds() {
        MyDatabaseAdapter db = new MyDatabaseAdapter(this);
        db.open();
        Cursor c = db.getAllNotificationIds();

        if (c != null)
            if (c.moveToFirst()) {
                notificationIds = new ArrayList<>();
                do {
                    addToNotificationIdsList(c);
                } while (c.moveToNext());


            }
        for (int i = 0; i < notificationIds.size(); i++) {
            listIds.add(Integer.parseInt(notificationIds.get(i)));

        }
        if (listIds.size() != 0)
            confirmReceivedMessages(listIds);
    }
    private void confirmReceivedMessages(List<Integer> listIds) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        ConfirmMessageRequest confirmMessageRequest = new ConfirmMessageRequest();
        confirmMessageRequest.setConfirmed_ids(listIds);
        ActivationResponse activationResponse = Utilities.retrieveUserInfo(this);
        String token = "Bearer " + activationResponse.getAccess_token();
        Call<ConfirmMessageDeliveryResponse> call1 = apiInterface.confirmMessageDelivery(token, confirmMessageRequest);
        call1.enqueue(new Callback<ConfirmMessageDeliveryResponse>() {
            @Override
            public void onResponse(Call<ConfirmMessageDeliveryResponse> call, Response<ConfirmMessageDeliveryResponse> response) {
            }

            @Override
            public void onFailure(Call<ConfirmMessageDeliveryResponse> call, Throwable t) {
                call.cancel();
            }
        });


    }
    private void addToNotificationIdsList(Cursor c) {
        notificationIds.add(c.getString(1));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(HomeSenderDetailsNotificationsActivity.this, HomeSenderNamesActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        clearNotifications(this);

    }

    private void deleteOneNotificationById(String notificationId) {
        MyDatabaseAdapter myDatabaseAdapter = new MyDatabaseAdapter(this);
        myDatabaseAdapter.open();
        boolean isDeleted = myDatabaseAdapter.deleteSpecificNotificationById(notificationId);
        if(isDeleted)
        myDatabaseAdapter.close();
        else if(i < 2){
            myDatabaseAdapter.deleteSpecificNotificationById(notificationId);
            i++;
        }
        getAllNotificationsOfSenderName(getIntent().getStringExtra("sender_name"));
        Collections.reverse(notificationModelList);
        notificationsAdapter = new NotificationsAdapter(notificationModelList, this, new OnItemClickListener() {
            @Override
            public void onItemClick(final String item, int position) {


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
        if (notificationModelList.size() == 0) {
            txtNotFound.setVisibility(View.VISIBLE);

        } else {
            txtNotFound.setVisibility(View.GONE);

        }
        recyclerViewNotifications.setAdapter(notificationsAdapter);

    }


    private void getAllNotificationsOfSenderName(String senderName) {
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

        notificationModel = new NotificationModel();

        notificationModel.setTitle(c.getString(1));
        notificationModel.setTime(c.getString(2));
        notificationModel.setContent(c.getString(3));
        notificationModel.setNotificationId(c.getString(4));
        notificationModelList.add(notificationModel);
    }

}
