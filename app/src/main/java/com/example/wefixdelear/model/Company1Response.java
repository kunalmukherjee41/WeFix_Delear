package com.example.wefixdelear.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Company1Response {

    @SerializedName("error")
    @Expose
    boolean error;

    @SerializedName("company")
    @Expose
    Company company;

    public Company1Response(boolean error, Company company) {
        this.error = error;
        this.company = company;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}
