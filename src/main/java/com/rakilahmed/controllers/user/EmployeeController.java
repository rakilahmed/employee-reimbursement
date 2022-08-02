package com.rakilahmed.controllers.user;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.rakilahmed.models.user.Employee;
import com.rakilahmed.services.user.EmployeeDAO;

public class EmployeeController extends UserController<Employee> {
    private final EmployeeDAO employeeDAO;
    private Employee currentEmployee;
    private final Logger logger = LogManager.getLogger(EmployeeController.class);

    /**
     * Parameterized constructor for EmployeeController class.
     * 
     * @param employeeDAO The DAO class to set.
     */
    public EmployeeController(EmployeeDAO employeeDAO) {
        this.employeeDAO = employeeDAO;
    }

    /**
     * Returns the current logged in employee.
     * 
     * @return The current logged in employee.
     */
    public Employee getCurrentEmployee() {
        return currentEmployee;
    }

    public String register(Employee employee) {
        logger.info("Registering employee: " + employee.getUsername());

        if (employee.getId() > 0 && employeeDAO.exists(employee)) {
            logger.warn("Employee already exists: " + employee.getUsername());
            return "Employee already exists";
        }

        int id = employeeDAO.insert(employee);

        if (id <= 0) {
            logger.warn("Employee registration failed: " + employee.getUsername());
            return "Employee registration failed";
        }

        employee.setId(id);
        currentEmployee = employee;

        logger.info("Employee registered successfully: " + employee.getId());
        return "Employee registered successfully. Employee ID: " + employee.getId();
    }

    public String login(Employee employee) {
        logger.info("Logging in employee: " + employee.getUsername());

        if (!employeeDAO.exists(employee)) {
            logger.warn("Employee does not exist: " + employee.getUsername());
            return "Employee does not exist";
        }

        employee.setLoggedIn(true);
        currentEmployee = employee;

        logger.info("Employee logged in successfully: " + employee.getUsername());
        return "Employee logged in successfully. Employee ID: " + employee.getId();
    }

    public String logout(Employee employee) {
        logger.info("Logging out employee: " + employee.getUsername());

        if (!employee.isLoggedIn()) {
            logger.warn("Employee is not logged in: " + employee.getUsername());
            return "Employee is not logged in";
        }

        employee.setLoggedIn(false);
        currentEmployee = null;

        logger.info("Employee logged out successfully: " + employee.getUsername());
        return "Employee logged out successfully. Employee ID: " + employee.getId();
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
            logger.warn("Employee profile update failed: " + id);
            return "Employee profile update failed. Employee ID: " + id;
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
        employee.setLoggedIn(true);

        if (!employeeDAO.update(employee.getId(), employee)) {
            logger.warn("Employee profile update failed: " + id);
            return "Employee profile update failed";
        }

        currentEmployee = employee;

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
