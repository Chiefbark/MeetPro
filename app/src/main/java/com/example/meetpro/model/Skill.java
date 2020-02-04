package com.example.meetpro.model;

public class Skill {

    // The id of the Skill
    private int id;
    // The name of the Skill
    private String name;

    /**
     * Constructor of the Skill
     */
    public Skill() {
    }

    /**
     * Constructor of the Skill
     *
     * @param id   The id of the Skill
     * @param name The name of the Skill
     */
    public Skill(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Returns the id of the Skill
     *
     * @return The id of the Skill
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the id of the Skill
     *
     * @param id The new id of the Skill
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the name of the Skill
     *
     * @return The name of the Skill
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the Skill
     *
     * @param name The new name of the Skill
     */
    public void setName(String name) {
        this.name = name;
    }
}
