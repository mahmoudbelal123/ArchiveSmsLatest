package com.kingsms.archivesms.adapters;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kingsms.archivesms.R;
import com.kingsms.archivesms.helper.OnItemClickDeleteHomeListener;
import com.kingsms.archivesms.helper.OnItemClickListener;
import com.kingsms.archivesms.model.NotificationStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class SenderNamesAdapter extends RecyclerView.Adapter<SenderNamesAdapter.MyViewHolder> {

    private List<NotificationStatus> senderNamesList;
    Context mContext;
    OnItemClickListener onItemClickListener ;
    OnItemClickDeleteHomeListener onItemClickDeleteHomeListener ;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtSenderName , text_unread_messages;
        public ImageView deleteAllNotificationsImage;
        public LinearLayout linear_container ;

        public MyViewHolder(View view) {
            super(view);
            txtSenderName = (TextView) view.findViewById(R.id.text_sender_name);
            deleteAllNotificationsImage = view.findViewById(R.id.image_delete_all_sender);
            text_unread_messages = (TextView) view.findViewById(R.id.text_unread_messages);
            linear_container = (LinearLayout) view.findViewById(R.id.linear_container);

        }
    }

    public SenderNamesAdapter(List<NotificationStatus> senderNamesList, Context context , OnItemClickListener onItemClickListener,OnItemClickDeleteHomeListener onItemClickDeleteHomeListener ) {
        this.senderNamesList = senderNamesList;
        this.mContext = context ;
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
        holder.txtSenderName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(senderNamesList.get(position).getSenderName().toString());
            }
        });

        holder.linear_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(senderNamesList.get(position).getSenderName().toString());
            }
        });

        if(senderNamesList.get(position).getStatus() == 0)
        {
            holder.text_unread_messages.setVisibility(View.GONE);

        }
        else
        { holder.text_unread_messages.setVisibility(View.VISIBLE);
            holder.text_unread_messages.setText(mContext.getString(R.string.there_is) +senderNamesList.get(position).getStatus() + mContext.getString(R.string.new_messages));

        }
        holder.deleteAllNotificationsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickDeleteHomeListener.onItemClick(senderNamesList.get(position).getSenderName());
            }
        });


    }

    @Override
    public int getItemCount() {
        return senderNamesList.size();
    }





}