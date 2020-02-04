package com.example.meetpro.model;

import java.util.ArrayList;

public class SkillList {

    private ArrayList<Skill> list;

    public void addUser(Skill skill) {
        this.list.add(skill);
    }

    public ArrayList<Skill> getList() {
        return list;
    }
}
