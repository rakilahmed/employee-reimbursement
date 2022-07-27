package com.rakilahmed.models.user;

public class Manager extends User {
    private String managerName;
    private String email;

    /**
     * Default constructor for Manager class.
     */
    public Manager() {
        super();
        this.managerName = "";
        this.email = "";
    }

    /**
     * Parameterized constructor for Manager class.
     * 
     * @param userId
     * @param username
     * @param password
     * @param managerName
     * @param email
     */
    public Manager(int userId, String username, String password, String managerName, String email) {
        super(userId, username, password);
        this.managerName = managerName;
        this.email = email;
    }

    /**
     * Getter for managerName.
     * 
     * @return managerName
     */
    public String getManagerName() {
        return managerName;
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
     * Setter for managerName.
     * 
     * @param managerName
     */
    public void setManagerName(String managerName) {
        this.managerName = managerName;
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
