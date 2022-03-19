package com.kingsms.archivesms.model;

public class NotificationStatus {

    private String senderName;
    private int status;
    private long time;

    public NotificationStatus(String senderName, int status, long time) {
        this.senderName = senderName;
        this.status = status;
        this.time = time;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
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
