package com.rakilahmed.models.user;

public abstract class User {
    private int userId;
    private String username;
    private String password;

    /**
     * Default constructor for User class.
     */
    public User() {
        this.userId = 0;
        this.username = "";
        this.password = "";
    }

    /**
     * Parameterized constructor for User class.
     * 
     * @param userId
     * @param username
     * @param password
     */
    public User(int userId, String username, String password) {
        this.userId = userId;
        this.username = username;
        this.password = password;
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
}
