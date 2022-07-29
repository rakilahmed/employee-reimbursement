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
     * @param username     The username of the employee.
     * @param password     The password of the employee.
     * @param employeeName The employee's name.
     * @param email        The email of the employee.
     */
    public Employee(String username, String password, String employeeName, String email) {
        super(username, password, employeeName, email);
    }

    /**
     * Parameterized constructor for Employee class.
     * 
     * @param id           The employee's id.
     * @param username     The employee's username.
     * @param password     The employee's password.
     * @param employeeName The employee's name.
     * @param email        The employee's email.
     */
    public Employee(int id, String username, String password, String employeeName, String email) {
        super(id, username, password, employeeName, email);
    }

    /**
     * Parameterized constructor for Employee class.
     * 
     * @param id           The employee's id.
     * @param username     The employee's username.
     * @param password     The employee's password.
     * @param employeeName The employee's name.
     * @param email        The employee's email.
     * @param isLoggedIn   Whether the employee is logged in.
     */
    public Employee(int id, String username, String password, String employeeName, String email, boolean isLoggedIn) {
        super(id, username, password, employeeName, email, isLoggedIn);
    }

    @Override
    public String toString() {
        return "Employee{" +
                "userId=" + getId() +
                ", username='" + getUsername() + '\'' +
                ", password='" + getPassword() + '\'' +
                ", fullName='" + getFullName() + '\'' +
                ", email='" + getEmail() + '}';
    }
}
