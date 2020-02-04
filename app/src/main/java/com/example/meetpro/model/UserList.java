package com.example.meetpro.model;

import java.util.ArrayList;

public class UserList {
    // The list of Users
    private ArrayList<User> list;

    /**
     * Adds a new User to the list
     *
     * @param user The User to add
     */
    public void addUser(User user) {
        this.list.add(user);
    }

    /**
     * Returns the list of Users
     *
     * @return The list of Users
     */
    public ArrayList<User> getList() {
        return list;
    }
}
