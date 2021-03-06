package com.example.meetpro.model;

public class User {
    // The id of the User
    private String id;
    // The name of the User
    private String name;
    // The surname of the User
    private String surname;
    // The phone of the User
    private String phone;
    // The email of the User
    private String email;
    // The password of the User
    private String password;
    // The description about the User
    private String description;
    // The latitude stored about the User
    private String latitude;
    // The longitude stored about the User
    private String longitude;
    // Job stores about the User
    private String job;
    // Sector of stored about the User
    private String sector;
    // Image url
    private String profilepic;

    /**
     * Constructor of the User
     */
    public User() {
    }

    /**
     * @param name
     * @param surname
     * @param phone
     * @param email
     * @param description
     */
    public User(String name, String surname, String phone, String email, String description) {
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.email = email;
        this.description = description;
    }

    /**
     * @param name
     * @param surname
     * @param email
     * @param password
     */
    public User(String name, String surname, String email, String password) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLatitude() {
        return latitude;
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
    public String getPhone() {
        return phone;
    }

    /**
     * Sets the phone of the User
     *
     * @param phone The new phone of the User
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Returns the email of the User
     *
     * @return The email of the User
     */
    public String getemail() {
        return email;
    }

    /**
     * Sets the email of the User
     *
     * @param email The new email of the User
     */
    public void setemail(String email) {
        this.email = email;
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


    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }
}
