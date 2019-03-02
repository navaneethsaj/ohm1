package com.blazingapps.asus.ohm;

public class Pump_List {


    private String name;
    private String count;
    private String waiting;
    private String rate;

    private String longitude;
    private String latitude;

    public Pump_List(String name, String count, String waiting, String rate, String longitude, String latitude) {
        this.name = name;
        this.count = count;
        this.waiting = waiting;
        this.rate = rate;
        this.longitude = longitude;
        this.latitude = latitude;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getWaiting() {
        return waiting;
    }

    public void setWaiting(String waiting) {
        this.waiting = waiting;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }



}
