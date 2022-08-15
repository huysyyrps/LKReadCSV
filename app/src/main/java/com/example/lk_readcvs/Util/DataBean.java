package com.example.lk_readcvs.Util;

public class DataBean {
    public String date;
    public String offDirectCurrent;
    public String offDirectVoltage;
    public String offACCurrent;
    public String offACVoltage;
    public String onDirectCurrent;
    public String onDirectVoltage;
    public String onACCurrent;
    public String onACVoltage;

    public String onPXFDirectTD;
    public String onCZDirectTD;
    public String onPXGACTD;
    public String onCZACTD;


    public String getOnPXFDirectTD() {
        return onPXFDirectTD;
    }

    public void setOnPXFDirectTD(String onPXFDirectTD) {
        this.onPXFDirectTD = onPXFDirectTD;
    }

    public String getOnCZDirectTD() {
        return onCZDirectTD;
    }

    public void setOnCZDirectTD(String onCZDirectTD) {
        this.onCZDirectTD = onCZDirectTD;
    }

    public String getOnPXGACTD() {
        return onPXGACTD;
    }

    public void setOnPXGACTD(String onPXGACTD) {
        this.onPXGACTD = onPXGACTD;
    }

    public String getOnCZACTD() {
        return onCZACTD;
    }

    public void setOnCZACTD(String onCZACTD) {
        this.onCZACTD = onCZACTD;
    }

    public DataBean(String date, String offDirectCurrent, String offDirectVoltage,
                    String offACCurrent, String offACVoltage, String onDirectCurrent,
                    String onDirectVoltage, String onACCurrent, String onACVoltage,
                    String onPXGDirectTD, String onCZDirectTD, String onPXGACTD, String onCZACTD) {
        this.date = date;
        this.offDirectCurrent = offDirectCurrent;
        this.offDirectVoltage = offDirectVoltage;
        this.offACCurrent = offACCurrent;
        this.offACVoltage = offACVoltage;
        this.onDirectCurrent = onDirectCurrent;
        this.onDirectVoltage = onDirectVoltage;
        this.onACCurrent = onACCurrent;
        this.onACVoltage = onACVoltage;

        this.onPXFDirectTD = onPXFDirectTD;
        this.onCZDirectTD = onCZDirectTD;
        this.onPXGACTD = onPXGACTD;
        this.onCZACTD = onCZACTD;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOffDirectCurrent() {
        return offDirectCurrent;
    }

    public void setOffDirectCurrent(String offDirectCurrent) {
        this.offDirectCurrent = offDirectCurrent;
    }

    public String getOffDirectVoltage() {
        return offDirectVoltage;
    }

    public void setOffDirectVoltage(String offDirectVoltage) {
        this.offDirectVoltage = offDirectVoltage;
    }

    public String getOffACCurrent() {
        return offACCurrent;
    }

    public void setOffACCurrent(String offACCurrent) {
        this.offACCurrent = offACCurrent;
    }

    public String getOffACVoltage() {
        return offACVoltage;
    }

    public void setOffACVoltage(String offACVoltage) {
        this.offACVoltage = offACVoltage;
    }

    public String getOnDirectCurrent() {
        return onDirectCurrent;
    }

    public void setOnDirectCurrent(String onDirectCurrent) {
        this.onDirectCurrent = onDirectCurrent;
    }

    public String getOnDirectVoltage() {
        return onDirectVoltage;
    }

    public void setOnDirectVoltage(String onDirectVoltage) {
        this.onDirectVoltage = onDirectVoltage;
    }

    public String getOnACCurrent() {
        return onACCurrent;
    }

    public void setOnACCurrent(String onACCurrent) {
        this.onACCurrent = onACCurrent;
    }

    public String getOnACVoltage() {
        return onACVoltage;
    }

    public void setOnACVoltage(String onACVoltage) {
        this.onACVoltage = onACVoltage;
    }
}
