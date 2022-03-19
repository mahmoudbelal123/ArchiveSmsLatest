package com.kingsms.archivesms.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kingsms.archivesms.R;
import com.kingsms.archivesms.helper.OnItemClickDeleteHomeListener;
import com.kingsms.archivesms.helper.OnItemClickListener;
import com.kingsms.archivesms.local_db.MyDatabaseAdapter;
import com.kingsms.archivesms.model.NotificationModel;
import com.kingsms.archivesms.model.NotificationStatus;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import androidx.recyclerview.widget.RecyclerView;

public class SenderNamesAdapter extends RecyclerView.Adapter<SenderNamesAdapter.MyViewHolder> {

    Context mContext;
    OnItemClickListener onItemClickListener;
    OnItemClickDeleteHomeListener onItemClickDeleteHomeListener;
    List<NotificationModel> notificationModelList;
    NotificationModel notificationModel;
    private List<NotificationStatus> senderNamesList;

    public SenderNamesAdapter(List<NotificationStatus> senderNamesList, Context context, OnItemClickListener onItemClickListener, OnItemClickDeleteHomeListener onItemClickDeleteHomeListener) {
        this.senderNamesList = senderNamesList;
        this.mContext = context;
        this.onItemClickListener = onItemClickListener;
        this.onItemClickDeleteHomeListener = onItemClickDeleteHomeListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sender_name, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {


        holder.txtSenderName.setText(senderNamesList.get(position).getSenderName().toString());
        if (senderNamesList.get(position) != null) {
            String time = ConvertMilliSecondsToFormattedDate(String.valueOf(senderNamesList.get(position).getTime()));
            if (time != null)
                holder.txtTime.setText(time);
        }

        holder.txtSenderName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(senderNamesList.get(position).getSenderName().toString(), position);
            }
        });
        if (senderNamesList.get(position).getSenderName() != null) {
            getAllNotificationsOfSenderName(senderNamesList.get(position).getSenderName());
            if (notificationModelList != null && !notificationModelList.isEmpty() && notificationModelList.get(0).getContent() != null) {
                String completeLatestMessage = notificationModelList.get(notificationModelList.size() - 1).getContent();
                String subLatestMessage;
                if (completeLatestMessage != null && completeLatestMessage.length() > 18) {
                    subLatestMessage = completeLatestMessage.substring(0, 18);
                    holder.txtLatestMessage.setText(subLatestMessage + "...");
                } else {
                    holder.txtLatestMessage.setText(completeLatestMessage);
                }
            }else{
                holder.txtLatestMessage.setText(String.valueOf(mContext.getString(R.string.empty_inbox)));
            }
        }
        holder.linear_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(senderNamesList.get(position).getSenderName().toString(), position);
            }
        });

        if (senderNamesList.get(position).getStatus() == 0) {
            holder.txtSenderName.setTypeface(holder.txtSenderName.getTypeface(), Typeface.NORMAL);
            holder.text_unread_messages.setVisibility(View.GONE);
            holder.imageViewMessageStatus.setBackground(mContext.getDrawable(R.drawable.review));

        } else {
            holder.txtSenderName.setTypeface(holder.txtSenderName.getTypeface(), Typeface.BOLD);
            holder.text_unread_messages.setVisibility(View.VISIBLE);
            // holder.text_unread_messages.setText(mContext.getString(R.string.there_is) +" "+senderNamesList.get(position).getStatus()+" " + mContext.getString(R.string.new_messages));
            holder.text_unread_messages.setText("" + senderNamesList.get(position).getStatus());
            holder.imageViewMessageStatus.setBackground(mContext.getDrawable(R.drawable.review));

        }
        holder.imageDelete.setOnClickListener(v -> onItemClickDeleteHomeListener.onItemClick(senderNamesList.get(position).getSenderName()));


    }

    @Override
    public int getItemCount() {
        return senderNamesList.size();
    }

    public String ConvertMilliSecondsToFormattedDate(String milliSeconds) {
        if (milliSeconds == null) return null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(milliSeconds));
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm aa",Locale.US);
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
            DateFormat currentDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm aa",Locale.US);
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

    private void getAllNotificationsOfSenderName(String senderName) {
        notificationModelList = new ArrayList<>();

        MyDatabaseAdapter db = new MyDatabaseAdapter(mContext);
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

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtSenderName, text_unread_messages, txtTime;
        public TextView txtLatestMessage;
        public LinearLayout linear_container;
        public ImageView imageViewMessageStatus,imageDelete;

        public MyViewHolder(View view) {
            super(view);
            txtSenderName = (TextView) view.findViewById(R.id.text_sender_name);
            txtTime = (TextView) view.findViewById(R.id.text_home_time);
            text_unread_messages = (TextView) view.findViewById(R.id.text_unread_messages);
            txtLatestMessage = (TextView) view.findViewById(R.id.text_latest_message);
            linear_container = (LinearLayout) view.findViewById(R.id.linear_container);
            imageViewMessageStatus = view.findViewById(R.id.imageView_sender_name_item);
            imageDelete = view.findViewById(R.id.image_delete_one_message);


        }
    }

}