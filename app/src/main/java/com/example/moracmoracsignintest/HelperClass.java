package com.example.moracmoracsignintest;

public class HelperClass {

    String ceoname, phonenum, storename, htpay;

    public String getCeoname() {
        return ceoname;
    }

    public void setCeoname(String ceoname) {
        this.ceoname = ceoname;
    }

    public String getPhonenum() {
        return phonenum;
    }

    public void setPhonenum(String phonenum) {
        this.phonenum = phonenum;
    }

    public String getStorename() {
        return storename;
    }

    public void setStorename(String storename) {
        this.storename = storename;
    }

    public String getHtpay() {
        return htpay;
    }

    public void setHtpay(String htpay) {
        this.htpay = htpay;
    }

    public HelperClass(String ceoname, String phonenum, String storename, String htpay) {
        this.ceoname = ceoname;
        this.phonenum = phonenum;
        this.storename = storename;
        this.htpay = htpay;
    }

    public HelperClass() {
    }
}
