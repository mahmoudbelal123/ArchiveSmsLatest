package com.kingsms.archivesms.view.HomeActivity;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.kingsms.archivesms.R;
import com.kingsms.archivesms.adapters.SenderNamesAdapter;
import com.kingsms.archivesms.background_services.GetNotificationsService;
import com.kingsms.archivesms.dagger.DaggerApplication;
import com.kingsms.archivesms.helper.OnItemClickListener;
import com.kingsms.archivesms.helper.Utilities;
import com.kingsms.archivesms.local_db.MyDatabaseAdapter;
import com.kingsms.archivesms.model.NotificationModel;
import com.kingsms.archivesms.model.confirm_message_delivery.ConfirmMessageDeliveryResponse;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class HomeSenderNamesActivity extends AppCompatActivity implements HomeSenderNamesView {

    List<String> senderNames;
    List<String> notificationIds;


    RecyclerView recyclerViewSenderNames;

    @Inject
     HomeSenderNamesPresenter homeSenderNamesPresenter ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((DaggerApplication) getApplication()).getAppComponent().inject(this);
        homeSenderNamesPresenter.onAttach(this);


        recyclerViewSenderNames = findViewById(R.id.recycle_sender_names);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewSenderNames.setLayoutManager(mLayoutManager);
        recyclerViewSenderNames.setItemAnimator(new DefaultItemAnimator());

        senderNames = new ArrayList<>();
        notificationIds = new ArrayList<>();


        startServiceOfGetNotReceivedNotifications();
        getAllSenderNamesOfNotification();
//        getAllNotificationIds();

       /* if (getIntent() != null) {
           // String content = getIntent().getStringExtra("content");
            //String senderName = getIntent().getStringExtra("sender_name");

            Toast.makeText(this, "content : " + content, Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "senderName : " + senderName, Toast.LENGTH_SHORT).show();

        }*/


      //  homeSenderNamesPresenter.confirmPresenter("Bearer "+Utilities.retrieveUserInfo(this).getAccess_token() , notificationIds);




        if(senderNames.size() != 0)
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
        }

    }


    private void getAllSenderNamesOfNotification() {
        MyDatabaseAdapter db = new MyDatabaseAdapter(this);
        db.open();
        Cursor c = db.getAllSenderNames();

        if(c != null)
        if (c.moveToFirst()) {
            senderNames = new ArrayList<>();
            do {
                addToSenderNameList(c);
            } while (c.moveToNext());

        }
    }

    private void getAllNotificationIds() {
        MyDatabaseAdapter db = new MyDatabaseAdapter(this);
        db.open();
        Cursor c = db.getAllNotificationIds();

        if(c != null)
            if (c.moveToFirst()) {
                notificationIds = new ArrayList<>();
                do {
                    addToNotificationIdsList(c);
                } while (c.moveToNext());

            }
    }

    private void addToSenderNameList(Cursor c) {
        senderNames.add(c.getString(1));
    }
    private void addToNotificationIdsList(Cursor c) {
        notificationIds.add(c.getString(1));
    }




    private  void startServiceOfGetNotReceivedNotifications(){

        getAllNotificationIds();

        JobScheduler jobScheduler = (JobScheduler) getApplicationContext()
                .getSystemService(JOB_SCHEDULER_SERVICE);
        if(notificationIds != null) {
            String arr[] = new String[notificationIds.size()];
            for (int i = 0; i < notificationIds.size(); i++) {
                arr[i] = notificationIds.get(i);
            }

            PersistableBundle bundle = new PersistableBundle();
            bundle.putStringArray("ids", arr);

            ComponentName componentName = new ComponentName(getApplicationContext(),
                    GetNotificationsService.class);

            JobInfo jobInfo = new JobInfo.Builder(1, componentName)
                    .setPeriodic(10000).setRequiredNetworkType(
                            JobInfo.NETWORK_TYPE_NOT_ROAMING)
                    .setExtras(bundle)
                    .setPersisted(true).build();

            jobScheduler.schedule(jobInfo);
        }

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showResponse(ConfirmMessageDeliveryResponse confirmMessageDeliveryResponse) {

        //TODO  - save received messages in local db , show them to the user -

      for(int i = 0 ; i < confirmMessageDeliveryResponse.getNot_send_messages().size() ; i++)
      {
          String name = confirmMessageDeliveryResponse.getNot_send_messages().get(i).getSender_name();
          senderNames.add(name);

      }

        if(senderNames.size() != 0)
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
        }


    }

    @Override
    public void onAttache() {

    }

    @Override
    public void onDetach() {

    }


}
