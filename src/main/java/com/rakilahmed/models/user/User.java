package com.rakilahmed.models.user;

public abstract class User {
    private int userId;
    private String username;
    private String password;
    private String fullName;
    private String email;

    /**
     * Default constructor for User class.
     */
    public User() {
        this.userId = 0;
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
     * @param fullName
     * @param email
     */
    public User(String username, String password, String fullName, String email) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
    }

    /**
     * Getter for userId.
     * 
     * @return userId
     */
    public int getUserId() {
        return userId;
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
     * Setter for userId.
     * 
     * @param userId
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Setter for username.
     * 
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Setter for password.
     * 
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Setter for fullName.
     * 
     * @param fullName
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * Setter for email.
     * 
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }
}
