package com.rakilahmed.models.user;

public class Employee extends User {
    private String employeeName;
    private String email;

    /**
     * Default constructor for Employee class.
     */
    public Employee() {
        super();
        this.employeeName = "";
        this.email = "";
    }

    /**
     * Parameterized constructor for Employee class.
     * 
     * @param userId
     * @param username
     * @param password
     * @param employeeName
     * @param email
     */
    public Employee(int userId, String username, String password, String employeeName, String email) {
        super(userId, username, password);
        this.employeeName = employeeName;
        this.email = email;
    }

    /**
     * Getter for employeeName.
     * 
     * @return employeeName
     */
    public String getEmployeeName() {
        return employeeName;
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
     * Setter for employeeName.
     * 
     * @param employeeName
     */
    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
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
