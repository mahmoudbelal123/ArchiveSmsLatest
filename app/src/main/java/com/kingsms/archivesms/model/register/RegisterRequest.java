package com.kingsms.archivesms.model.register;

import com.google.gson.annotations.SerializedName;

public class RegisterRequest {

    @SerializedName("full_name")
    private String full_name;

    @SerializedName("password")
    private String password;

    @SerializedName("phone")
    private String phone;

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


}
