package com.kingsms.archivesms.model.confirm_message_delivery;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ConfirmMessageRequest {
    @SerializedName("confirmed_ids")
    private List<Integer> confirmed_ids;

    public List<Integer> getConfirmed_ids() {
        return confirmed_ids;
    }

    public void setConfirmed_ids(List<Integer> confirmed_ids) {
        this.confirmed_ids = confirmed_ids;
    }
}
