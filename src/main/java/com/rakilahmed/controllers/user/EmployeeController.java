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
     * Default constructor for EmployeeController class.
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
        if (employee == null) {
            logger.warn("Employee is null");
            return "Employee is null";
        }

        logger.info("Registering employee: " + employee.getUsername());
        int id = employeeDAO.insert(employee);

        if (id > 0) {
            employee.setUserId(id);
            currentEmployee = employee;

            logger.info("Employee registered successfully: " + employee.getUsername());
            return "Employee registered successfully";
        }

        currentEmployee = employee;

        logger.warn("Employee registration failed: " + employee.getUsername());
        return "Employee registration failed";
    }

    public String login(Employee employee) {
        if (employee == null) {
            logger.warn("Employee is null");
            return "Employee is null";
        }

        logger.info("Logging in employee: " + employee.getUsername());

        if (employeeDAO.exists(employee)) {
            employee.setLoggedIn(true);
            currentEmployee = employee;

            logger.info("Employee logged in successfully: " + employee.getUsername());
            return "Employee logged in successfully";
        }

        currentEmployee = employee;

        logger.warn("Employee login failed: " + employee.getUsername());
        return "Employee login failed";
    }

    public String logout(Employee employee) {
        if (employee == null) {
            logger.warn("Employee is null");
            return "Employee is null";
        }

        logger.info("Logging out employee: " + employee.getUsername());

        if (employee.isLoggedIn()) {
            employee.setLoggedIn(false);
            currentEmployee = null;

            logger.info("Employee logged out successfully: " + employee.getUsername());
            return "Employee logged out successfully";
        }

        currentEmployee = employee;

        logger.warn("Employee logout failed: " + employee.getUsername());
        return "Employee logout failed";
    }

    public String viewProfile(int id) {
        if (id <= 0) {
            logger.warn("Employee id is invalid: " + id);
            return "Employee id is invalid";
        }

        logger.info("Locating employee profile: " + id);
        Employee employee = employeeDAO.get(id);

        if (employee == null) {
            logger.warn("Employee is null");
            return "Employee is null";
        } else if (!employeeDAO.exists(employee)) {
            logger.warn("Employee profile not found: " + id);
            return "Employee profile not found";
        }

        logger.info("Employee profile found successfully: " + employee.getUsername());
        return employee.toString();
    }

    public Employee editProfile(Employee employee, String newUsername, String newPassword, String newFullName,
            String newEmail) {
        if (employee == null) {
            logger.warn("Employee is null");
            return null;
        }

        logger.info("Editing employee profile: " + employee.getUsername());

        if (employeeDAO.exists(employee)) {
            employee.setUsername(newUsername);
            employee.setPassword(newPassword);
            employee.setFullName(newFullName);
            employee.setEmail(newEmail);
            employeeDAO.update(employee.getId(), employee);

            logger.info("Employee profile edited successfully: " + employee.getUsername());
        } else {
            logger.warn("Employee not found: " + employee.getUsername());
        }

        return employee;
    }

    public Employee get(int id) {
        if (id <= 0) {
            logger.warn("Employee id is invalid: " + id);
            return null;
        }

        logger.info("Locating employee: " + id);
        Employee employee = employeeDAO.get(id);

        if (employee == null) {
            logger.warn("Employee is null");
            return null;
        } else if (!employeeDAO.exists(employee)) {
            logger.warn("Employee not found: " + id);
            return null;
        }

        logger.info("Employee found successfully: " + employee.getUsername());
        return employee;
    }

    public List<Employee> getAll() {
        logger.info("Getting all employees");
        List<Employee> employees = employeeDAO.getAll();

        if (employees != null) {
            logger.info("Retrieved all employees successfully: " + employees.size());
            return employees;
        }

        logger.info("Failed to retrieve all employees");
        return null;
    }
}
