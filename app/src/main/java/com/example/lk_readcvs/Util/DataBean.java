package com.example.lk_readcvs.Util;

public class DataBean {
    public String date;
    public String time;
    public String offDirectCurrent;
    public String offDirectVoltage;
    public String offACCurrent;
    public String offACVoltage;
    public String onDirectCurrent;
    public String onDirectVoltage;
    public String onACCurrent;
    public String onACVoltage;

    public DataBean(String date, String time, String offDirectCurrent, String offDirectVoltage,
                    String offACCurrent, String offACVoltage, String onDirectCurrent,
                    String onDirectVoltage, String onACCurrent, String onACVoltage) {
        this.date = date;
        this.time = time;
        this.offDirectCurrent = offDirectCurrent;
        this.offDirectVoltage = offDirectVoltage;
        this.offACCurrent = offACCurrent;
        this.offACVoltage = offACVoltage;
        this.onDirectCurrent = onDirectCurrent;
        this.onDirectVoltage = onDirectVoltage;
        this.onACCurrent = onACCurrent;
        this.onACVoltage = onACVoltage;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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
