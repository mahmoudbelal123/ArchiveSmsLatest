package com.kingsms.archivesms.model;

public class NotificationStatus {

    private  String senderName;
    private int status;

    public  NotificationStatus(String senderName , int status)
    {
        this.senderName = senderName;
        this.status = status;
    }
    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
