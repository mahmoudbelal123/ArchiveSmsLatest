package com.kingsms.archivesms.model.activation_code;

import com.google.gson.annotations.SerializedName;

public class ActivationRequest {
    @SerializedName("phone")
    private String phone;


    public String getFirebase_token() {
        return firebase_token;
    }

    public void setFirebase_token(String firebase_token) {
        this.firebase_token = firebase_token;
    }

    @SerializedName("firebase_token")
    private String firebase_token;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @SerializedName("code")
    private String code;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
