package com.rakilahmed.controllers.user;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.rakilahmed.models.user.Employee;
import com.rakilahmed.services.user.EmployeeDAO;

public class EmployeeController extends UserController<Employee> {
    private final EmployeeDAO employeeDAO;
    private final Logger logger = LogManager.getLogger(EmployeeController.class);

    /**
     * Parameterized constructor for EmployeeController class.
     * 
     * @param employeeDAO The DAO class to set.
     */
    public EmployeeController(EmployeeDAO employeeDAO) {
        this.employeeDAO = employeeDAO;
    }

    public int register(Employee employee) {
        logger.info("Registering employee: " + employee.getUsername());

        if (employee.getId() > 0 && employeeDAO.exists(employee)) {
            logger.warn("Employee already exists: " + employee.getUsername());
            return -1;
        }

        int id = employeeDAO.insert(employee);

        if (id <= 0) {
            logger.warn("Employee registration failed: " + employee.getUsername());
            return -1;
        }

        logger.info("Employee registered successfully: " + id);
        return id;
    }

    public int login(String username, String password) {
        logger.info("Logging in employee: " + username);

        int id = employeeDAO.verify(username, password);

        if (id <= 0) {
            logger.warn("Employee credentials are incorrect: " + username);
            return -1;
        }

        logger.info("Employee logged in successfully: " + username);
        return id;
    }

    public String viewProfile(int id) {
        if (id <= 0) {
            logger.warn("Employee id is invalid: " + id);
            return "Employee id is invalid. Employee ID: " + id;
        }

        logger.info("Locating employee profile: " + id);
        Employee employee = employeeDAO.get(id);

        if (!employeeDAO.exists(employee)) {
            logger.warn("Employee profile not found: " + id);
            return "Employee profile not found";
        }

        logger.info("Employee profile found successfully: " + employee.getUsername());
        return employee.toString();
    }

    public String updateProfile(int id, String username, String password, String fullName, String email) {
        logger.info("Updating employee profile: " + id);

        if (id <= 0) {
            logger.warn("Employee id is invalid: " + id);
            return "Employee id is invalid. Employee ID: " + id;
        }

        if (username == null || username.isEmpty() || password == null || password.isEmpty() || fullName == null
                || fullName.isEmpty() || email == null || email.isEmpty()) {
            logger.warn("Username, password, full name, or email is empty: " + id);
            return "Username, password, full name, and email cannot be empty";
        }

        Employee employee = employeeDAO.get(id);

        if (!employeeDAO.exists(employee)) {
            logger.warn("Employee profile not found: " + id);
            return "Employee profile not found";
        }

        employee.setUsername(username);
        employee.setPassword(password);
        employee.setFullName(fullName);
        employee.setEmail(email);

        if (!employeeDAO.update(employee.getId(), employee)) {
            logger.warn("Employee profile update failed: " + id);
            return "Employee profile update failed";
        }

        logger.info("Employee profile updated successfully: " + employee.getUsername());
        return "Employee profile updated successfully. Employee ID: " + employee.getId();
    }

    public Employee get(int id) {
        if (id <= 0) {
            logger.warn("Employee id is invalid: " + id);
            return null;
        }

        logger.info("Locating employee: " + id);
        Employee employee = employeeDAO.get(id);

        if (!employeeDAO.exists(employee)) {
            logger.warn("Employee not found: " + id);
            return null;
        }

        logger.info("Employee found successfully: " + employee.getUsername());
        return employee;
    }

    public List<Employee> getAll() {
        logger.info("Getting all employees");
        List<Employee> employees = employeeDAO.getAll();

        if (employees.isEmpty()) {
            logger.warn("No employees found");
            return null;
        }

        logger.info("All employees found successfully");
        return employees;
    }
}
