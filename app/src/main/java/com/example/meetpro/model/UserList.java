package com.example.meetpro.model;

import java.util.ArrayList;

public class UserList {

    private ArrayList<User> list;

    public void addUser(User user) {
        this.list.add(user);
    }

    public ArrayList<User> getList() {
        return list;
    }
}
