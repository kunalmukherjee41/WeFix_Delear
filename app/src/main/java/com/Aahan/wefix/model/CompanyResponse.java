package com.Aahan.wefix.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CompanyResponse {

    @SerializedName("error")
    @Expose
    boolean error;

    @SerializedName("company")
    @Expose
    List<Company> company;

    public CompanyResponse(boolean error, List<Company> company) {
        this.error = error;
        this.company = company;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<Company> getCompany() {
        return company;
    }

    public void setCompany(List<Company> company) {
        this.company = company;
    }
}
