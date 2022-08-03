package com.rakilahmed.models.user;

public abstract class User {
    private int id;
    private String username;
    private String password;
    private String fullName;
    private String email;

    /**
     * Default constructor for User class.
     */
    public User() {
        this.id = 0;
        this.username = "";
        this.password = "";
        this.fullName = "";
        this.email = "";
    }

    /**
     * Parameterized constructor for User class.
     * 
     * @param username
     * @param password
     */
    public User(String username, String password) {
        this.id = 0;
        this.username = username;
        this.password = password;
        this.fullName = "";
        this.email = "";
    }

    /**
     * Parameterized constructor for User class.
     * 
     * @param username The username of the user.
     * @param password The password of the user.
     * @param fullName The full name of the user.
     * @param email    The email of the user.
     */
    public User(String username, String password, String fullName, String email) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
    }

    /**
     * Parameterized constructor for User class.
     * 
     * @param id       The user's id.
     * @param username The user's username.
     * @param password The user's password.
     * @param fullName The user's full name.
     * @param email    The user's email.
     */
    public User(int id, String username, String password, String fullName, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
    }

    /**
     * Getter for id.
     * 
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Getter for username.
     * 
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Getter for password.
     * 
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Getter for fullName.
     * 
     * @return fullName
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Getter for email.
     * 
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Setter for id.
     * 
     * @param id The user's id.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Setter for username.
     * 
     * @param username The user's username.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Setter for password.
     * 
     * @param password The user's password.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Setter for fullName.
     * 
     * @param fullName The user's full name.
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * Setter for email.
     * 
     * @param email The user's email.
     */
    public void setEmail(String email) {
        this.email = email;
    }
}
