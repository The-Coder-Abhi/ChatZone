package com.example.chatzone;

public class User01 {
    String User_Name,image_url,First_Name,Last_Name;

    public User01() {
    }

    public User01(String user_Name, String image_url, String first_Name, String last_Name) {
        User_Name = user_Name;
        this.image_url = image_url;
        First_Name = first_Name;
        Last_Name = last_Name;
    }

    public String getUser_Name() {
        return User_Name;
    }

    public void setUser_Name(String user_Name) {
        User_Name = user_Name;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getFirst_Name() {
        return First_Name;
    }

    public void setFirst_Name(String first_Name) {
        First_Name = first_Name;
    }

    public String getLast_Name() {
        return Last_Name;
    }

    public void setLast_Name(String last_Name) {
        Last_Name = last_Name;
    }
}
