package com.kingsms.archivesms.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kingsms.archivesms.R;
import com.kingsms.archivesms.helper.OnItemClickListener;
import com.kingsms.archivesms.model.NotificationModel;

import java.util.List;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.MyViewHolder> {

    private List<NotificationModel> notificationList;
    Context mContext;
    OnItemClickListener onItemClickListener ;



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtNotificationDetails , txtTime;
        public ImageView imgDeleteMsg;

        public MyViewHolder(View view) {
            super(view);
            txtNotificationDetails = (TextView) view.findViewById(R.id.text_notifications_details);
            txtTime = (TextView) view.findViewById(R.id.text_time_notification);
            imgDeleteMsg = view.findViewById(R.id.image_delete_one_message);

        }
    }

    public NotificationsAdapter(List<NotificationModel> notificationList, Context context,    OnItemClickListener onItemClickListener) {
        this.notificationList = notificationList;
        this.mContext = context ;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification_details, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.txtNotificationDetails.setText(notificationList.get(position).getContent());
        holder.txtTime.setText(notificationList.get(position).getTime());
        holder.imgDeleteMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(notificationList.get(position).getNotificationId());
            }
        });

    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }





}