package com.kingsms.archivesms.background_services;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.widget.Toast;

import com.kingsms.archivesms.apiClient.ApiClient;
import com.kingsms.archivesms.apiClient.ApiInterface;
import com.kingsms.archivesms.helper.Utilities;
import com.kingsms.archivesms.local_db.MyDatabaseAdapter;
import com.kingsms.archivesms.model.confirm_message_delivery.ConfirmMessageDeliveryResponse;
import com.kingsms.archivesms.model.login.LoginResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetNotificationsService extends JobService {


    List<String> list  = new ArrayList<>();
    ApiInterface apiInterface ;
    @Override
    public boolean onStartJob(JobParameters params) {
        String arr[] = params.getExtras().getStringArray("ids");
        for (int i = 0 ; i  < arr.length ; i++)
        {
            list.add(arr[i]);
        }

        //connectWithServer(list);
       // Toast.makeText(this, "started  . . .  . . .", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {

        //Toast.makeText(this, "stopped . . . ", Toast.LENGTH_SHORT).show();
        return false;
    }


//    private  void connectWithServer(List<String> messageIdsList)
//    {
//
//      try {
//          apiInterface = ApiClient.getClient().create(ApiInterface.class);
//          List<Integer> list = new ArrayList<>();
//
//          for (int i = 0; i < messageIdsList.size(); i++) {
//              list.add(Integer.parseInt(messageIdsList.get(i)));
//          }
//
//
//          LoginResponse loginResponse = Utilities.retrieveUserInfo(this);
//          String token = "Bearer " + loginResponse.getAccess_token();
//          Call<ConfirmMessageDeliveryResponse> call1 = apiInterface.confirmMessageDelivery(token, list);
//          call1.enqueue(new Callback<ConfirmMessageDeliveryResponse>() {
//              @Override
//              public void onResponse(Call<ConfirmMessageDeliveryResponse> call, Response<ConfirmMessageDeliveryResponse> response) {
//                  response.body();
//
//
//                  if (response.code() == 200) {
//                      for (int i = 0; i < response.body().getNot_send_messages().size(); i++) {
//                          saveNotificationToLocalDatabase("" + response.body().getNot_send_messages().get(i).getNotification_id(),
//                                  response.body().getNot_send_messages().get(i).getTitle()
//                                  , response.body().getNot_send_messages().get(i).getSender_name(),
//                                  response.body().getNot_send_messages().get(i).getTitle()
//                                  , response.body().getNot_send_messages().get(i).getContent());
//                      }
//                  }
//                  else {
//
//                  }
//
//
//              }
//
//              @Override
//
//              public void onFailure(Call<ConfirmMessageDeliveryResponse> call, Throwable t) {
//
//                  call.cancel();
//
//
//              }
//          });
//
//      }catch (Exception e)
//      {
//
//      }
//    }


    private  void saveNotificationToLocalDatabase(String notificationId , String time , String senderName, String title, String content)
    {
        MyDatabaseAdapter db = new MyDatabaseAdapter(this);
        db.open();
        long id = db.insertNotification(notificationId, senderName, time, content, title);
        long idSenderNamesInserted = db.insertSenderNames(senderName);

        if (id > -1) {
           // Toast.makeText(this, "Notification All Content Inserted Done ", Toast.LENGTH_SHORT).show();
            //TODO send Request to change the status on the server side - by using notification_id -


        } else {

          //  Toast.makeText(this, "Notification All Content Not Inserted", Toast.LENGTH_SHORT).show();
        }
        if (idSenderNamesInserted > -1) {
           // Toast.makeText(this, "Sender Name Inserted Done ", Toast.LENGTH_SHORT).show();
            //TODO send Request to change the status on the server side - by using notification_id -


        } else {

          // Toast.makeText(this, "Sender Name Not Inserted", Toast.LENGTH_SHORT).show();
        }


    }

}
