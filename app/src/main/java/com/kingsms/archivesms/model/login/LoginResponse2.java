package com.kingsms.archivesms.model.login;

import com.google.gson.annotations.SerializedName;

public class LoginResponse2 {

    @SerializedName("message")
    private String message;

    @SerializedName("code")
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
