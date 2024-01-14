package com.rula.cloudlib.obj;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {
    public String name;
    public String id;

    public User(){}
    public User(String name, String id) {
        this.name = name;
        this.id = id;
    }
}
