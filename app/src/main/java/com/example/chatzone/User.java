package com.example.chatzone;

public class User {
    String User_Name,status,image_url,id;

    public User() {
    }

    public User(String user_Name, String status, String image_url, String id) {
        User_Name = user_Name;
        this.status = status;
        this.image_url = image_url;
        this.id = id;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_Name() {
        return User_Name;
    }

    public void setUser_Name(String user_Name) {
        User_Name = user_Name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
