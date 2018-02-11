package com.example.akoni.conread;

import android.graphics.Bitmap;

/**
 * Created by Akoni on 12/12/2017.
 */

public class User {
    private String first_name;
    private String last_name;
    private String email;
    private String number;
    private String password;
    private String username;
    private String address;
    //private Bitmap e_Image, w_Image;

    public User(){
        first_name = "";
        last_name = "";
        email = "";
        number = "";
        password = "";
        username = "";
        address = "";
    }

    public User(String first_name, String last_name, String email, String number, String password, String username, String address){
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.number = number;
        this.password = password;
        this.username = username;
        this.address = address;
    }

    public void setFirst_name(String first_name){
        this.first_name = first_name;
    }

    public void setLast_name(String last_name){
        this.last_name = last_name;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public void setNumber(String number){
        this.number = number;
    }

    /*public void setE_Image(Bitmap image){
        this.e_Image = image;
    }

    public void setW_Image(Bitmap image){
        this.w_Image = image;
    }*/

    public void setPassword(String password){
        this.password = password;
    }

    public void setUsername(String username){this.username = username; }

    public void setAddress(String address) {this.address = address; }

    public String getUsername() { return this.username; }

    public String getFirst_name(){
        return this.first_name;
    }

    public String getLast_name(){
        return this.last_name;
    }

    public String getEmail(){
        return this.email;
    }

    public String getNumber(){
        return this.number;
    }

    public String getPassword(){
        return this.password;
    }

    /*public Bitmap getE_Image(){
        return this.e_Image;
    }

    public Bitmap getW_Image(){
        return this.w_Image;
    }*/

    public String getAddress() {return this.address; }
}
