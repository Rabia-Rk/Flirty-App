package com.datting_package.Flirty_Datting_App.Models;

public class MatchModel {
    String U_id;
    String Picture;
    String Username;


    public MatchModel(String u_id, String picture, String username) {
        U_id = u_id;
        Picture = picture;
        Username = username;
    }

    public String getU_id() {
        return U_id;
    }

    public void setU_id(String u_id) {
        U_id = u_id;
    }

    public String getPicture() {
        return Picture;
    }

    public void setPicture(String picture) {
        Picture = picture;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }
}
