package com.rakilahmed.models.user;

public class Employee extends User {
    /**
     * Default constructor for Employee class.
     */
    public Employee() {
        super();
    }

    /**
     * Parameterized constructor for Employee class.
     * 
     * @param username
     * @param password
     * @param employeeName
     * @param email
     */
    public Employee(String username, String password, String employeeName, String email) {
        super(username, password, employeeName, email);
    }
}
