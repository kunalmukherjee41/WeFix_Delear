package com.Aahan.wefix.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DelearResponse {

    @SerializedName("error")
    @Expose
    public boolean error;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("delear")
    @Expose
    public Delear delear;

    public DelearResponse(Boolean error, String message, Delear delear) {
        this.error = error;
        this.message = message;
        this.delear = delear;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Delear getDelear() {
        return delear;
    }

    public void setDelear(Delear delear) {
        this.delear = delear;
    }
}
