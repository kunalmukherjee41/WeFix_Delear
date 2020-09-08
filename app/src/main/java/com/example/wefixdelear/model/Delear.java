package com.example.wefixdelear.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Delear {
//tbl_delear_id,delear_name,address,pin,contact1,contact2,panno,gstin,email,website,status,username,password
    @SerializedName("tbl_delear_id")
    @Expose
    public int tblDelearId;

    @SerializedName("delear_name")
    @Expose
    public String delearName;

    @SerializedName("address")
    @Expose
    public String address;

    @SerializedName("pin")
    @Expose
    public String pin;

    @SerializedName("contact1")
    @Expose
    public String contact1;

    @SerializedName("contact2")
    @Expose
    public String contact2;

    @SerializedName("panno")
    @Expose
    public String panno;

    @SerializedName("gstin")
    @Expose
    public String gstin;

    @SerializedName("email")
    @Expose
    public String email;

    @SerializedName("website")
    @Expose
    public String website;

    @SerializedName("status")
    @Expose
    public String status;

    @SerializedName("username")
    @Expose
    public String username;

    @SerializedName("password")
    @Expose
    public String password;

    @SerializedName("plus_member")
    @Expose
    public String plusMunber;

    public Delear(int tblDelearId, String delearName, String address, String pin, String contact1, String contact2, String panno, String gstin, String email, String website, String status, String username, String password, String plusMunber) {
        this.tblDelearId = tblDelearId;
        this.delearName = delearName;
        this.address = address;
        this.pin = pin;
        this.contact1 = contact1;
        this.contact2 = contact2;
        this.panno = panno;
        this.gstin = gstin;
        this.email = email;
        this.website = website;
        this.status = status;
        this.username = username;
        this.password = password;
        this.plusMunber = plusMunber;
    }

    public int getTblDelearId() {
        return tblDelearId;
    }

    public void setTblDelearId(int tblDelearId) {
        this.tblDelearId = tblDelearId;
    }

    public String getDelearName() {
        return delearName;
    }

    public void setDelearName(String delearName) {
        this.delearName = delearName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getContact1() {
        return contact1;
    }

    public void setContact1(String contact1) {
        this.contact1 = contact1;
    }

    public String getContact2() {
        return contact2;
    }

    public void setContact2(String contact2) {
        this.contact2 = contact2;
    }

    public String getPanno() {
        return panno;
    }

    public void setPanno(String panno) {
        this.panno = panno;
    }

    public String getGstin() {
        return gstin;
    }

    public void setGstin(String gstin) {
        this.gstin = gstin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPlusMunber() {
        return plusMunber;
    }

    public void setPlusMunber(String plusMunber) {
        this.plusMunber = plusMunber;
    }
}
