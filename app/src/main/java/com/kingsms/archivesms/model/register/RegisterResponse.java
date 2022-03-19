package com.kingsms.archivesms.model.register;

import com.google.gson.annotations.SerializedName;

public class RegisterResponse {

    @SerializedName("message")
    private String message;

    @SerializedName("code")
    private int code;


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
