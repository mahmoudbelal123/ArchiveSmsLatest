package com.kingsms.archivesms.adapters;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kingsms.archivesms.R;
import com.kingsms.archivesms.helper.OnItemClickListener;
import com.kingsms.archivesms.helper.SpaceAdjust;
import com.kingsms.archivesms.model.NotificationModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.MyViewHolder> {

    Context mContext;
    OnItemClickListener onItemClickListener;
    private List<NotificationModel> notificationList;
    private String[] startAndEndIndex= new String[]{"0","0",""};


    public NotificationsAdapter(List<NotificationModel> notificationList, Context context, OnItemClickListener onItemClickListener) {
        this.notificationList = notificationList;
        this.mContext = context;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification_details, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        if(notificationList.get(position).getContent() != null) {
            String message = notificationList.get(position).getContent();
            startAndEndIndex = getLinkFromMessage(message);
            if(startAndEndIndex[0].equals(startAndEndIndex[1]) && TextUtils.isEmpty(startAndEndIndex[2])) {
                holder.txtNotificationDetails.setText(message);
            }else {
                SpannableString spannableString = new SpannableString(message);
                spannableString.setSpan(new ForegroundColorSpan(mContext.getResources()
                        .getColor(R.color.blue)), Integer.parseInt(startAndEndIndex[0]), Integer.parseInt(startAndEndIndex[1]), 0);// i am applying for Register alone. so starting count is 23 and end count is 31.
                ClickableSpan clickableSpan = new SpaceAdjust(message) {
                    @Override
                    public void onClick(View textView) {
                        String url = startAndEndIndex[2] ;
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        mContext.startActivity(i);
                    }
                };
                spannableString.setSpan(clickableSpan, Integer.parseInt(startAndEndIndex[0]), Integer.parseInt(startAndEndIndex[1]),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.txtNotificationDetails.setLinksClickable(true);
                holder.txtNotificationDetails.setText(spannableString);
                holder.txtNotificationDetails.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String url = startAndEndIndex[2] ;
                        if(url == null || TextUtils.isEmpty(url)) return;
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        mContext.startActivity(i);
                    }
                });
            }
        }
        if (notificationList.get(position) != null) {
            String time = ConvertMilliSecondsToFormattedDate(String.valueOf(notificationList.get(position).getTime()));
            if (time != null)
                holder.txtTime.setText(time);
        }
        holder.imgDeleteMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(notificationList.get(position).getNotificationId(), position);
            }
        });


        holder.txtNotificationDetails.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(holder.txtNotificationDetails.getText());
                Toast.makeText(mContext, "" + mContext.getString(R.string.Message_is_Copied), Toast.LENGTH_LONG).show();
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public String ConvertMilliSecondsToFormattedDate(String milliSeconds) {
        if (milliSeconds == null) return null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(milliSeconds));
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm aa");
        String dateTime = dateFormat.format(calendar.getTime());
         return finalDate(dateTime);
    }

    public String finalDate(String SentDateFormatted){
        String currentDateTime = null;
        String finalDateFormat = null;
        try {
            long currentDateMilli = Calendar.getInstance().getTimeInMillis();
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Long.parseLong(String.valueOf(currentDateMilli)));
            DateFormat currentDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm aa");
            currentDateTime = currentDateFormat.format(calendar.getTime());
            if(currentDateTime.substring(0,10).equals(SentDateFormatted.substring(0,10))){
                finalDateFormat = SentDateFormatted.substring(11 , SentDateFormatted.length()) + ", " +mContext.getString(R.string.today);
                return finalDateFormat;
            }else{

                return SentDateFormatted;
            }

        }catch (Exception e){

        }
        return finalDateFormat;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtNotificationDetails, txtTime;
        public ImageView imgDeleteMsg;

        public MyViewHolder(View view) {
            super(view);
            txtNotificationDetails = (TextView) view.findViewById(R.id.text_notifications_details);
            txtTime = (TextView) view.findViewById(R.id.text_time_notification);
            imgDeleteMsg = view.findViewById(R.id.image_delete_one_message);

        }
    }

    private String[] getLinkFromMessage(String message){
        String[] startAndEnd= new String[]{"0","0",""};
        if(message == null || TextUtils.isEmpty(message)) return startAndEnd;
        int startIndex = -1;
        int endIndex = -1;
        String link = null;
        String messageStartingWithTheLink= null;
        if(!message.contains("http")) return startAndEnd;
        if(message.contains("http")){
            startIndex = message.indexOf("http");
        }
        if(startIndex != -1){
            messageStartingWithTheLink = message.substring(startIndex,message.length());
        }
        if(messageStartingWithTheLink == null || TextUtils.isEmpty(messageStartingWithTheLink))
            return startAndEnd;

        if(messageStartingWithTheLink.contains(" "))
            link = messageStartingWithTheLink.substring(0, messageStartingWithTheLink.indexOf(" "));
        else
            link = messageStartingWithTheLink.substring(0, messageStartingWithTheLink.length());

        if(TextUtils.isEmpty(link)) return startAndEnd;
        startAndEnd[0] = String.valueOf(startIndex);
        endIndex = startIndex + link.length();
        startAndEnd[1] = String.valueOf(endIndex);
        startAndEnd[2] = link;

        return startAndEnd;
    }
}