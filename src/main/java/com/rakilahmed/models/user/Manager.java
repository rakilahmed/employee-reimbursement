package com.rakilahmed.models.user;

public class Manager extends User {
    /**
     * Default constructor for Manager class.
     */
    public Manager() {
        super();
    }

    /**
     * Parameterized constructor for Manager class.
     * 
     * @param username
     * @param password
     * @param managerName
     * @param email
     */
    public Manager(String username, String password, String managerName, String email) {
        super(username, password, managerName, email);
    }

    @Override
    public String toString() {
        return "Manager{" +
                "userId=" + getUserId() +
                ", username='" + getUsername() + '\'' +
                ", password='" + getPassword() + '\'' +
                ", fullName='" + getFullName() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", isLoggedIn=" + isLoggedIn() +
                '}';
    }
}
