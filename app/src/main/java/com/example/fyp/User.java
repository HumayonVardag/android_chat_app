package com.example.fyp;

import android.app.backup.SharedPreferencesBackupHelper;
import android.content.SharedPreferences;

public class User {
    int id;
    String name;
    String email;
    Boolean temp;

    public User(int id, String name) {
        this.id = id;
        this.name = name;
        temp = false;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTemp(Boolean temp){
        this.temp = temp;
    }

    public Boolean getTemp() {
        return temp;
    }
}
