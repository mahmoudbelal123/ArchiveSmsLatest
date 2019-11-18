package com.kingsms.archivesms.model.confirm_message_delivery;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ConfirmMessageDeliveryResponse {

    @SerializedName("not_send_messages")
    private List<Messages> not_send_messages;

    @SerializedName("code")
    private int code;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<Messages> getNot_send_messages() {
        return not_send_messages;
    }

    public void setNot_send_messages(List<Messages> not_send_messages) {
        this.not_send_messages = not_send_messages;
    }

   public class Messages{
       @SerializedName("title")
       private String title;

       @SerializedName("time")
       private String time;

       public String getTime() {
           return time;
       }

       public void setTime(String time) {
           this.time = time;
       }

       @SerializedName("sender_name")
       private String sender_name;


       @SerializedName("content")
       private String content;

       public String getTitle() {
           return title;
       }

       public void setTitle(String title) {
           this.title = title;
       }

       public String getSender_name() {
           return sender_name;
       }

       public void setSender_name(String sender_name) {
           this.sender_name = sender_name;
       }

       public String getContent() {
           return content;
       }

       public void setContent(String content) {
           this.content = content;
       }

       public int getNotification_id() {
           return notification_id;
       }

       public void setNotification_id(int notification_id) {
           this.notification_id = notification_id;
       }

       @SerializedName("notification_id")
       private int notification_id;

   }

}
