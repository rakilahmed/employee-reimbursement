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
     */
    public Manager(String username, String password) {
        super(username, password);
    }

    /**
     * Parameterized constructor for Manager class.
     * 
     * @param username    The username of the manager.
     * @param password    The password of the manager.
     * @param managerName The manager's name.
     * @param email       The email of the manager.
     */
    public Manager(String username, String password, String managerName, String email) {
        super(username, password, managerName, email);
    }

    /**
     * Parameterized constructor for Manager class.
     * 
     * @param id          The manager's id.
     * @param username    The manager's username.
     * @param password    The manager's password.
     * @param managerName The manager's name.
     * @param email       The manager's email.
     */
    public Manager(int id, String username, String password, String managerName, String email) {
        super(id, username, password, managerName, email);
    }

    @Override
    public String toString() {
        return "Manager{" +
                "userId=" + getId() +
                ", username='" + getUsername() + '\'' +
                ", password='" + getPassword() + '\'' +
                ", fullName='" + getFullName() + '\'' +
                ", email='" + getEmail() + '}';
    }
}
