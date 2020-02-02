package com.example.meetpro.model;

public class User {

    private int id;
    private String name;
    private String surname;
    private int phone;
    private String mail;
    private String password;

    /**
     * Constructor of the User
     *
     * @param id      The id of the User
     * @param name    The name of the User
     * @param surname The surname of the User
     * @param phone   The phone of the User
     * @param mail    The mail of the User
     */
    public User(int id, String name, String surname, int phone, String mail) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.mail = mail;
    }

    /**
     * Constructor og the User
     *
     * @param id       The id of the User
     * @param name     The name of the User
     * @param surname  The surname of the User
     * @param phone    The phone of the User
     * @param mail     The mail of the User
     * @param password The password of the User
     */
    public User(int id, String name, String surname, int phone, String mail, String password) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.mail = mail;
        this.password = password;
    }

    /**
     * Constructor of the User
     *
     * @param name    The name of the User
     * @param surname The surname of the User
     * @param phone   The phone of the User
     * @param mail    The mail of the User
     */
    public User(String name, String surname, int phone, String mail) {
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.mail = mail;
    }

    /**
     * Constructor og the User
     *
     * @param name     The name of the User
     * @param surname  The surname of the User
     * @param phone    The phone of the User
     * @param mail     The mail of the User
     * @param password The password of the User
     */
    public User(String name, String surname, int phone, String mail, String password) {
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.mail = mail;
        this.password = password;
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
}
