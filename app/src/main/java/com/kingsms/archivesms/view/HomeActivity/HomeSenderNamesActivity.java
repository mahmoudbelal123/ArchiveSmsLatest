package com.kingsms.archivesms.view.HomeActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kingsms.archivesms.R;
import com.kingsms.archivesms.adapters.SenderNamesAdapter;
import com.kingsms.archivesms.apiClient.ApiClient;
import com.kingsms.archivesms.apiClient.ApiInterface;
import com.kingsms.archivesms.helper.OnItemClickDeleteHomeListener;
import com.kingsms.archivesms.helper.OnItemClickListener;
import com.kingsms.archivesms.helper.Utilities;
import com.kingsms.archivesms.local_db.MyDatabaseAdapter;
import com.kingsms.archivesms.model.MapNotificationStatus;
import com.kingsms.archivesms.model.NotificationModel;
import com.kingsms.archivesms.model.NotificationStatus;
import com.kingsms.archivesms.model.activation_code.ActivationResponse;
import com.kingsms.archivesms.model.confirm_message_delivery.ConfirmMessageDeliveryResponse;
import com.kingsms.archivesms.model.confirm_message_delivery.ConfirmMessageRequest;
import com.kingsms.archivesms.view.my_profile.MyProfile;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeSenderNamesActivity extends AppCompatActivity {

    public List<NotificationStatus> senderNames;
    public List<MapNotificationStatus> mapSenderNames;
    public SenderNamesAdapter senderNamesAdapter;
    List<String> notificationIds;
    RecyclerView recyclerViewSenderNames;
    ProgressBar progressBarGetNotifications;
    TextView text_not_found;
    ImageView imageEmptyInbox;
    List<Integer> status = new ArrayList<>();
    List<String> contactsList = new ArrayList<>();
    List<Integer> listIds = new ArrayList<>();
    List<NotificationModel> notificationModelList;
    NotificationModel notificationModel;
    int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBarGetNotifications = findViewById(R.id.progress_getNotifications);
        text_not_found = findViewById(R.id.text_not_found);
        imageEmptyInbox = findViewById(R.id.image_empty);
        recyclerViewSenderNames = findViewById(R.id.recycle_sender_names);

        // navigationView =findViewById(R.id.nav_view);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerViewSenderNames.setLayoutManager(mLayoutManager);
        recyclerViewSenderNames.setItemAnimator(new DefaultItemAnimator());

        senderNames = new ArrayList<>();
        notificationIds = new ArrayList<>();
        mapSenderNames = new ArrayList<>();

        getNotReceivedNotifications();
        getAllNotificationIds();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        //TODO get all notifications Ids
        // getAllNotificationIds();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.action_settings:
                Intent intent = new Intent(HomeSenderNamesActivity.this, MyProfile.class);
                startActivity(intent);

                break;
            default:
                break;
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // getNotReceivedNotifications();


        //getAllSenderNamesOfNotification();

        /*getNotReceivedNotifications();
        getAllSenderNamesOfNotification();

        if(senderNames != null)
        {
            SenderNamesAdapter senderNamesAdapter = new SenderNamesAdapter(senderNames, this, new OnItemClickListener() {
                @Override
                public void onItemClick(String item) {

                    Intent intent = new Intent(HomeSenderNamesActivity.this , HomeSenderDetailsNotificationsActivity.class);
                    intent.putExtra("sender_name",item);
                    startActivity(intent);
                }
            });

            recyclerViewSenderNames.setAdapter(senderNamesAdapter);
            senderNamesAdapter.notifyDataSetChanged();
        }*/

    }

    private void getNotReceivedNotifications() {
        progressBarGetNotifications.setVisibility(View.VISIBLE);
        List<Integer> list = new ArrayList<>();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        ActivationResponse activationResponse = Utilities.retrieveUserInfo(this);
        String token = "Bearer " + activationResponse.getAccess_token();
        ConfirmMessageRequest confirmMessageRequest = new ConfirmMessageRequest();
        confirmMessageRequest.setConfirmed_ids(list);
        Call<ConfirmMessageDeliveryResponse> call1 = apiInterface.confirmMessageDelivery(token, confirmMessageRequest);
        call1.enqueue(new Callback<ConfirmMessageDeliveryResponse>() {
            @Override
            public void onResponse(Call<ConfirmMessageDeliveryResponse> call, Response<ConfirmMessageDeliveryResponse> response) {
                response.body();
                progressBarGetNotifications.setVisibility(View.GONE);
                if (response.code() == 200) {
                    for (int i = 0; i < response.body().getNot_send_messages().size(); i++) {
                        saveNotificationToLocalDatabase("" + response.body().getNot_send_messages().get(i).getNotification_id(),
                                response.body().getNot_send_messages().get(i).getTime()
                                , response.body().getNot_send_messages().get(i).getSender_name(),
                                response.body().getNot_send_messages().get(i).getTitle()
                                , response.body().getNot_send_messages().get(i).getContent(), true);
                    }
                } else {

                    // Toast.makeText(getApplicationContext(), "some thing error" + response.body().getCode(), Toast.LENGTH_SHORT).show();
                }
                getAllSenderNamesOfNotification();
            }

            @Override
            public void onFailure(Call<ConfirmMessageDeliveryResponse> call, Throwable t) {

                call.cancel();
                progressBarGetNotifications.setVisibility(View.GONE);
                getAllSenderNamesOfNotification();


            }
        });


    }

    private void getStatus(String senderName) {
        status = new ArrayList<>();
        final MyDatabaseAdapter db = new MyDatabaseAdapter(this);
        db.open();
        Cursor c = db.getStatusOfSenderName(senderName);
        if (c != null)
            if (c.moveToNext()) {
                //senderNames = new ArrayList<>();
                do {
                    addToStatusList(c);
                } while (c.moveToNext());
            }
    }

    private void addToStatusList(Cursor c) {
        status.add((c.getInt(1)));
    }

    private List<String> getContact(String contactPhone) {
        contactsList = new ArrayList<>();
        final MyDatabaseAdapter db = new MyDatabaseAdapter(this);
        db.open();
        Cursor c = db.getContact(contactPhone);
        if (c != null)
            if (c.moveToNext()) {
                //senderNames = new ArrayList<>();
                do {
                    addToContactList(c);
                } while (c.moveToNext());
            }

        return contactsList;
    }

    private void addToContactList(Cursor c) {
        contactsList.add((c.getString(1)));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    private void saveNotificationToLocalDatabase(String notificationId, String time, String senderName, String title, String content, boolean updateStatus) {
        if (time == null)
            time = "" + android.text.format.DateFormat.format("yyyy-MM-dd hh:mm:ss", new java.util.Date());
        contactsList = getContact("" + senderName);
        Date date = null;
        if (contactsList.size() != 0)
            senderName = contactsList.get(0);

        long timeMilli = 0;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US);
            date = sdf.parse(time);
            if (date == null)
                timeMilli = Calendar.getInstance().getTimeInMillis();
            else
                timeMilli = date.getTime();
        } catch (Exception e) {
            if (date != null)
                timeMilli = date.getTime();
        }
        MyDatabaseAdapter db = new MyDatabaseAdapter(this);
        db.open();

        long id = db.insertNotification(notificationId, senderName, String.valueOf(timeMilli), content, title);
        db.insertSenderNames(senderName, 0, timeMilli);
        if (id > -1) {
            getStatus(senderName);
            db.updateStatusOfNotification(senderName, (status.get(0)) + 1, timeMilli);
        }
    }

    public void getAllSenderNamesOfNotification() {
        counter = 0;
        senderNames = new ArrayList<>();
        final MyDatabaseAdapter db = new MyDatabaseAdapter(this);
        db.open();
        Cursor c = db.getAllSenderNames();
        if (c != null)
            if (c.moveToNext()) {
                do {
                    addToSenderNameList(c);
                } while (c.moveToNext());
            }

        if (senderNames.size() == 0) {
            text_not_found.setVisibility(View.VISIBLE);
            imageEmptyInbox.setVisibility(View.VISIBLE);

        } else {
            text_not_found.setVisibility(View.GONE);
            imageEmptyInbox.setVisibility(View.GONE);


        }
        if (senderNames != null) {
             getFinalSenderNames(senderNames);
             if(senderNames == null) return;
            senderNamesAdapter = new SenderNamesAdapter(senderNames, this, new OnItemClickListener() {
                @Override
                public void onItemClick(String item, int position) {

                    if (!item.equals(null))
                        db.updateStatusOfNotificationForClickToDetails(item, 0);

                    Intent intent = new Intent(HomeSenderNamesActivity.this, HomeSenderDetailsNotificationsActivity.class);
                    intent.putExtra("sender_name", item);
                    startActivity(intent);
                }
            }, new OnItemClickDeleteHomeListener() {
                @Override
                public void onItemClick(final String item) {

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(HomeSenderNamesActivity.this);
                    alertDialog.setMessage(getString(R.string.confirm_delete));
                    alertDialog.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteAllSenderNames(item);
                            deleteAllSenderNamesNotifications(item);
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

            recyclerViewSenderNames.setAdapter(senderNamesAdapter);
            senderNamesAdapter.notifyDataSetChanged();
        }


    }

    private void getFinalSenderNames(List<NotificationStatus> senderNames) {
        for (int i = 0 ; i < senderNames.size(); i++){
            getAllNotificationsOfSenderName(senderNames.get(i).getSenderName());
            if(notificationModelList.size() == 0){
                deleteAllSenderNames(senderNames.get(i).getSenderName());
                counter = 1;
            }
        }
        if(counter == 1)
        getAllSenderNamesOfNotification();
    }

    private void getAllNotificationsOfSenderName(String senderName) {
        notificationModelList = new ArrayList<>();

        MyDatabaseAdapter db = new MyDatabaseAdapter(this);
        db.open();
        Cursor c = db.getNotificationsBySenderName(senderName);

        if (c.moveToNext()) {
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


    private void deleteAllSenderNames(String senderName) {
        MyDatabaseAdapter myDatabaseAdapter = new MyDatabaseAdapter(this);
        myDatabaseAdapter.open();
        boolean isDeleted = myDatabaseAdapter.deleteSenderName(senderName);

        if (isDeleted)
            getAllSenderNamesOfNotification();
    }

    private void deleteAllSenderNamesNotifications(String senderName) {
        MyDatabaseAdapter myDatabaseAdapter = new MyDatabaseAdapter(this);
        myDatabaseAdapter.open();
        myDatabaseAdapter.deleteSenderNameOfAll(senderName);

        // getAllSenderNamesOfNotification();

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
                response.body();
                if (response.code() == 200) {
                    for (int i = 0; i < response.body().getNot_send_messages().size(); i++) {
                        saveNotificationToLocalDatabase("" + response.body().getNot_send_messages().get(i).getNotification_id(),
                                response.body().getNot_send_messages().get(i).getTime()
                                , response.body().getNot_send_messages().get(i).getSender_name(),
                                response.body().getNot_send_messages().get(i).getTitle()
                                , response.body().getNot_send_messages().get(i).getContent(), false);
                    }
                } else {

                    //Toast.makeText(getApplicationContext(), "some thing error" + response.body().getCode(), Toast.LENGTH_SHORT).show();
                }
                getAllSenderNamesOfNotification();
            }

            @Override
            public void onFailure(Call<ConfirmMessageDeliveryResponse> call, Throwable t) {

                call.cancel();
            }
        });


    }

    private void addToSenderNameList(Cursor c) {
        senderNames.add(new NotificationStatus(c.getString(1), c.getInt(2), c.getLong(3)));


    }

    private void addToNotificationIdsList(Cursor c) {
        notificationIds.add(c.getString(1));
    }
}
