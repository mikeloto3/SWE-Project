package com.example.akoni.conread;

/**
 * Created by Akoni on 12/21/2017.
 */

public class Water {
    private double total;
    private String month;
    private String date;
    private String year;

    public Water(){
        this.total = 0;
        this.month = "";
        this.date = "";
        this.year = "";
    }

    public Water(double total, String month, String date, String year){
        this.total = total;
        this.month = month;
        this.date = date;
        this.year = year;
    }

    public void setTotal(double val){
        this.total = val;
    }

    public double getTotal(){
        return this.total;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public String getDate() {
        return date;
    }

    public String getYear() {
        return year;
    }
}
