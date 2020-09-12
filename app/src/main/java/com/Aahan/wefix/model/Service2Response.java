package com.Aahan.wefix.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Service2Response {

    @SerializedName("error")
    @Expose
    boolean error;

    @SerializedName("message")
    @Expose
    String message;

    @SerializedName("service")
    @Expose
    Service service;

    public Service2Response(boolean error, String message, Service service) {
        this.error = error;
        this.message = message;
        this.service = service;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }
}
