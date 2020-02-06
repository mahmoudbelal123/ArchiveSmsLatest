package com.kingsms.archivesms.model.activation_code;

import com.google.gson.annotations.SerializedName;

public class ActivationResponse {

    @SerializedName("access_token")
    private String access_token;

    @SerializedName("token_type")
    private String token_type;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @SerializedName("code")
    private int code;

}
