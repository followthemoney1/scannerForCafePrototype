package com.leaditteam.qrscanner.helpers;

public class TimeParser {
    String YEAR;
    int DAY;
    int MONTH;
    int HOUR;
    int MIN;

    public void setYEAR(String YEAR) {
        this.YEAR = YEAR;
    }

    public void setDAY(int DAY) {
        this.DAY = DAY;
    }

    public void setMONTH(int MONTH) {
        this.MONTH = MONTH;
    }

    public void setHOUR(int HOUR) {
        this.HOUR = HOUR;
    }

    public void setMIN(int MIN) {
        this.MIN = MIN;
    }

    public String getYEAR() {

        return YEAR;
    }

    public int getDAY() {
        return DAY;
    }

    public int getMONTH() {
        return MONTH;
    }

    public int getHOUR() {
        return HOUR;
    }

    public int getMIN() {
        return MIN;
    }

    public TimeParser(String YEAR, int DAY, int MONTH, int HOUR, int MIN) {

        this.YEAR = YEAR;
        this.DAY = DAY;
        this.MONTH = MONTH;
        this.HOUR = HOUR;
        this.MIN = MIN;
    }
}
