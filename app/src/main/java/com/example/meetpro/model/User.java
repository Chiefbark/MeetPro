package com.example.meetpro.model;

public class User {
    // The id of the User
    private int id;
    // The name of the User
    private String name;
    // The surname of the User
    private String surname;
    // The phone of the User
    private int phone;
    // The mail of the User
    private String mail;
    // The password of the User
    private String password;
    // The description about the User
    private String description;

    /**
     * Constructor of the User
     */
    public User() {
    }

    /**
     * Constructor of the User
     *
     * @param id          The id of the User
     * @param name        The name of the User
     * @param surname     The surname of the User
     * @param phone       The phone of the User
     * @param mail        The mail of the User
     * @param password    The password of the User
     * @param description The description about the User
     */
    public User(int id, String name, String surname, int phone, String mail, String password, String description) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.mail = mail;
        this.password = password;
        this.description = description;
    }

    /**
     * Returns the id of the User
     *
     * @return The id of the User
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the id of the User
     *
     * @param id The new id of the User
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the name of the User
     *
     * @return The name of the User
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name to the User
     *
     * @param name The new name of the User
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the surname of the User
     *
     * @return The surname of the User
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Sets the surname to the User
     *
     * @param surname The new surname of the User
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * Returns the phone of the User
     *
     * @return The phone of the User
     */
    public int getPhone() {
        return phone;
    }

    /**
     * Sets the phone of the User
     *
     * @param phone The new phone of the User
     */
    public void setPhone(int phone) {
        this.phone = phone;
    }

    /**
     * Returns the mail of the User
     *
     * @return The mail of the User
     */
    public String getMail() {
        return mail;
    }

    /**
     * Sets the mail of the User
     *
     * @param mail The new mail of the User
     */
    public void setMail(String mail) {
        this.mail = mail;
    }

    /**
     * Returns the password of the User
     *
     * @return The password of the User
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of the User
     *
     * @param password The new password of the User
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns the description of the User
     *
     * @return The description of the User
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the User
     *
     * @param description The new description of the User
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
