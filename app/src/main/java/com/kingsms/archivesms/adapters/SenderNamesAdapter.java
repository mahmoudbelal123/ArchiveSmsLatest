package com.kingsms.archivesms.adapters;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kingsms.archivesms.R;
import com.kingsms.archivesms.helper.OnItemClickDeleteHomeListener;
import com.kingsms.archivesms.helper.OnItemClickListener;

import java.util.List;

public class SenderNamesAdapter extends RecyclerView.Adapter<SenderNamesAdapter.MyViewHolder> {

    private List<String> senderNamesList;
    Context mContext;
    OnItemClickListener onItemClickListener ;
    OnItemClickDeleteHomeListener onItemClickDeleteHomeListener ;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtSenderName;
        public ImageView deleteAllNotificationsImage;

        public MyViewHolder(View view) {
            super(view);
            txtSenderName = (TextView) view.findViewById(R.id.text_sender_name);
            deleteAllNotificationsImage = view.findViewById(R.id.image_delete_all_sender);

        }
    }

    public SenderNamesAdapter(List<String> senderNamesList, Context context , OnItemClickListener onItemClickListener,OnItemClickDeleteHomeListener onItemClickDeleteHomeListener ) {
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
        holder.txtSenderName.setText(senderNamesList.get(position).toString());
        holder.txtSenderName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(senderNamesList.get(position).toString());
            }
        });

        holder.deleteAllNotificationsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickDeleteHomeListener.onItemClick(senderNamesList.get(position));
            }
        });


    }

    @Override
    public int getItemCount() {
        return senderNamesList.size();
    }





}