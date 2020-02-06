package com.kingsms.archivesms.view.HomeActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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
import com.kingsms.archivesms.model.NotificationStatus;
import com.kingsms.archivesms.model.activation_code.ActivationResponse;
import com.kingsms.archivesms.model.confirm_message_delivery.ConfirmMessageDeliveryResponse;
import com.kingsms.archivesms.model.confirm_message_delivery.ConfirmMessageRequest;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeSenderNamesActivity extends AppCompatActivity  {

    public   List<NotificationStatus> senderNames;
    List<String> notificationIds;


    RecyclerView recyclerViewSenderNames;
    ProgressBar progressBarGetNotifications;
    TextView text_not_found;

//    @Inject
//     HomeSenderNamesPresenter homeSenderNamesPresenter ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        ((DaggerApplication) getApplication()).getAppComponent().inject(this);
//        homeSenderNamesPresenter.onAttach(this);

        progressBarGetNotifications = findViewById(R.id.progress_getNotifications);
        text_not_found =findViewById(R.id.text_not_found);
        recyclerViewSenderNames = findViewById(R.id.recycle_sender_names);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerViewSenderNames.setLayoutManager(mLayoutManager);
        recyclerViewSenderNames.setItemAnimator(new DefaultItemAnimator());

        senderNames = new ArrayList<>();
        notificationIds = new ArrayList<>();

       //getNotReceivedNotifications();
        //getAllSenderNamesOfNotification();

//       if(senderNames != null)
//        {
//            SenderNamesAdapter senderNamesAdapter = new SenderNamesAdapter(senderNames, this, new OnItemClickListener() {
//                @Override
//                public void onItemClick(String item) {
//
//                    Intent intent = new Intent(HomeSenderNamesActivity.this , HomeSenderDetailsNotificationsActivity.class);
//                    intent.putExtra("sender_name",item);
//                    startActivity(intent);
//                }
//            });
//
//            recyclerViewSenderNames.setAdapter(senderNamesAdapter);
//        }

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



    private void getNotReceivedNotifications()
    {
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
                                , response.body().getNot_send_messages().get(i).getContent(),true);
                    }
                }
                else {

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
    private void addToStatusList(Cursor c) {
        status.add((c.getInt(1)));


    }
    List<Integer>  status = new ArrayList<>() ;
    private  void  getStatus(String senderName)
    {

        status= new ArrayList<>();
        final MyDatabaseAdapter db = new MyDatabaseAdapter(this);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    private  void saveNotificationToLocalDatabase(String notificationId , String time , String senderName, String title, String content, boolean updateStatus)
    {
        MyDatabaseAdapter db = new MyDatabaseAdapter(this);
        db.open();
        Date currentTime = Calendar.getInstance().getTime();


        if(time == null)
            time = ""+android.text.format.DateFormat.format("yyyy-MM-dd \nhh:mm:ss a", new java.util.Date());

        long id = db.insertNotification(notificationId, senderName, time, content, title);
         long idSenderNamesInserted ;
//        if(status.size()!=0 ) {
//            // idSenderNamesInserted = db.insertSenderNames(senderName ,status.get(0));
//            db.updateStatusOfNotification(senderName, (status.get(0)) + 1);
//        }else {
//            idSenderNamesInserted = db.insertSenderNames(senderName, 1);
//        }
        long timeMili= System.currentTimeMillis();

        idSenderNamesInserted = db.insertSenderNames(senderName, 0,timeMili);


        if (id > -1) {
            // Toast.makeText(this, "Notification All Content Inserted Done ", Toast.LENGTH_SHORT).show();
            //TODO send Request to change the status on the server side - by using notification_id -
            getStatus(senderName);
            db.updateStatusOfNotification(senderName, (status.get(0)) + 1 ,timeMili);


        } else {

             // Toast.makeText(this, "Notification All Content Not Inserted", Toast.LENGTH_SHORT).show();
        }
        if (idSenderNamesInserted > -1) {
            // Toast.makeText(this, "Sender Name Inserted Done ", Toast.LENGTH_SHORT).show();
            //TODO send Request to change the status on the server side - by using notification_id -


        } else {

            //if(updateStatus)
          //  db.updateStatusOfNotification(senderName , (status.get(0))+1);
           //  Toast.makeText(this, "Sender Name Not Inserted", Toast.LENGTH_SHORT).show();
        }


    }

    public  void getAllSenderNamesOfNotification() {
        senderNames = new ArrayList<>();
        final MyDatabaseAdapter db = new MyDatabaseAdapter(this);
        db.open();
        Cursor c = db.getAllSenderNames();
        if(c != null)
        if (c.moveToNext()) {
            //senderNames = new ArrayList<>();
            do {
                addToSenderNameList(c);
            } while (c.moveToNext());
        }

        if(senderNames.size() == 0)
        {
            text_not_found.setVisibility(View.VISIBLE);

        }
        else
        {
            text_not_found.setVisibility(View.GONE);

        }
        if(senderNames != null)
        {

//             List<String> stringList = new ArrayList<>();
//             List<Integer> statusList = new ArrayList<>();
//
//             for (int  i = 0 ; i < senderNames.size() ; i++)
//             {
//                 stringList.add(senderNames.get(i).getSenderName());
//                 statusList.add(senderNames.get(i).getStatus());
//             }
//            Set<String> s = new LinkedHashSet<>(stringList);
//             stringList = new ArrayList<>(s);
//            Collections.reverse(stringList);
//
//            Set<Integer> s2 = new LinkedHashSet<>(statusList);
//            statusList = new ArrayList<>(s2);
//            Collections.reverse(statusList);

            //final long timeMili= System.currentTimeMillis();

            senderNamesAdapter = new SenderNamesAdapter(senderNames,this, new OnItemClickListener() {
                @Override
                public void onItemClick(String item) {

                    if(!item.equals(null))
                    db.updateStatusOfNotificationForClickToDetails(item , 0);

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


    private  void showConfirmDialog()
    {




    }
    private void deleteAllSenderNames(String senderName)
    {
        MyDatabaseAdapter myDatabaseAdapter = new MyDatabaseAdapter(this);
        myDatabaseAdapter.open();
       boolean isDeleted =  myDatabaseAdapter.deleteSenderName(senderName);
//        senderNamesAdapter.notifyDataSetChanged();

       if(isDeleted)
        getAllSenderNamesOfNotification();
    }


    private void deleteAllSenderNamesNotifications(String senderName)
    {
        MyDatabaseAdapter myDatabaseAdapter = new MyDatabaseAdapter(this);
        myDatabaseAdapter.open();
        myDatabaseAdapter.deleteSenderNameOfAll(senderName);

       // getAllSenderNamesOfNotification();

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

        //TODO Call again get all no received Notifications

        for(int  i = 0 ; i < notificationIds.size() ; i++)
        {
            listIds.add(Integer.parseInt(notificationIds.get(i)));

        }
        if(listIds.size()!=0)
        confirmReceivedMessages(listIds);
    }

    List<Integer> listIds = new ArrayList<>();
    private void confirmReceivedMessages(List<Integer> listIds)
    {
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
                                , response.body().getNot_send_messages().get(i).getContent(),false);
                    }
                }
                else {

                    //Toast.makeText(getApplicationContext(), "some thing error" + response.body().getCode(), Toast.LENGTH_SHORT).show();
                }
                getAllSenderNamesOfNotification();
            }
            @Override
            public void onFailure(Call<ConfirmMessageDeliveryResponse> call, Throwable t) {

                call.cancel();
               // progressBarGetNotifications.setVisibility(View.GONE);
                //getAllSenderNamesOfNotification();



            }
        });


    }
    private void addToSenderNameList(Cursor c) {
        senderNames.add(new NotificationStatus(c.getString(1) ,c.getInt(2)));


    }
    private void addToNotificationIdsList(Cursor c) {
        notificationIds .add(c.getString(1));
    }




 /*   private  void startServiceOfGetNotReceivedNotifications(){

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

    }*/



    public     SenderNamesAdapter senderNamesAdapter ;
//    @Override
//    public void showResponse(ConfirmMessageDeliveryResponse confirmMessageDeliveryResponse) {
//
//        //TODO  - save received messages in local db , show them to the user -
//
//      for(int i = 0 ; i < confirmMessageDeliveryResponse.getNot_send_messages().size() ; i++)
//      {
//          String name = confirmMessageDeliveryResponse.getNot_send_messages().get(i).getSender_name();
//          senderNames.add(name);
//
//      }
//
//        if(senderNames !=null)
//        {
//             senderNamesAdapter = new SenderNamesAdapter(senderNames, this, new OnItemClickListener() {
//                @Override
//                public void onItemClick(String item) {
//
//                    Intent intent = new Intent(HomeSenderNamesActivity.this , HomeSenderDetailsNotificationsActivity.class);
//                    intent.putExtra("sender_name",item);
//                    startActivity(intent);
//                }
//            });
//
//            recyclerViewSenderNames.setAdapter(senderNamesAdapter);
//        }
//
//
//    }




}
