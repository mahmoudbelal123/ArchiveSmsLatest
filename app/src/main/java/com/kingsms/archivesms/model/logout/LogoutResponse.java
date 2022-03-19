package com.kingsms.archivesms.model.logout;

import com.google.gson.annotations.SerializedName;

public class LogoutResponse {
    @SerializedName("code")
    private int phone;
    @SerializedName("message")
    private String message;

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
