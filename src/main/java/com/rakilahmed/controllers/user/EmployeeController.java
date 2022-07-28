package com.rakilahmed.controllers.user;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.rakilahmed.models.user.Employee;
import com.rakilahmed.models.user.User;
import com.rakilahmed.services.UserDAO;

public class EmployeeController extends UserController {
    private UserDAO userDAO;
    private Employee currentEmployee;
    private Logger logger = LogManager.getLogger(EmployeeController.class);

    /**
     * Default constructor for EmployeeController class.
     */
    public EmployeeController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    /**
     * Returns the current logged in employee.
     * 
     * @return The current logged in employee.
     */
    public Employee getCurrentEmployee() {
        return currentEmployee;
    }

    /**
     * Registers a new employee.
     *
     * @param employee The employee to register.
     * @return String indicating whether the employee was registered successfully.
     */
    @Override
    public String register(User employee) {
        logger.info("Registering employee: " + employee.getUsername());
        int id = userDAO.insert(employee);

        if (id > 0) {
            employee.setUserId(id);
            currentEmployee = (Employee) employee;

            logger.info("Employee registered successfully: " + employee.getUsername());
            return "Employee registered successfully";
        }

        currentEmployee = (Employee) employee;

        logger.info("Employee registration failed: " + employee.getUsername());
        return "Employee registration failed";
    }

    /**
     * Logs in an employee.
     *
     * @param employee The employee to login.
     * @return String indicating whether the employee was logged in successfully.
     */
    @Override
    public String login(User employee) {
        logger.info("Logging in employee: " + employee.getUsername());

        if (userDAO.exists(employee.getUsername(), employee.getPassword())) {
            employee.setLoggedIn(true);
            currentEmployee = (Employee) employee;

            logger.info("Employee logged in successfully: " + employee.getUsername());
            return "Employee logged in successfully";
        }

        currentEmployee = (Employee) employee;

        logger.info("Employee login failed: " + employee.getUsername());
        return "Employee login failed";
    }

    /**
     * Logs out an employee.
     *
     * @param employee The employee to log out.
     * @return String indicating whether the employee was logged out successfully.
     */
    @Override
    public String logout(User employee) {
        logger.info("Logging out employee: " + employee.getUsername());

        if (employee.isLoggedIn()) {
            employee.setLoggedIn(false);
            currentEmployee = null;

            logger.info("Employee logged out successfully: " + employee.getUsername());
            return "Employee logged out successfully";
        }

        currentEmployee = (Employee) employee;

        logger.info("Employee logout failed: " + employee.getUsername());
        return "Employee logout failed";
    }

    /**
     * View employee's profile.
     * 
     * @param id The employee's id.
     * @return Employee's profile as a string.
     */
    public String viewProfile(int id) {
        logger.info("Locating employee profile: " + id);
        Employee employee = userDAO.getEmployee(id);

        if (employee == null) {
            logger.info("Employee's profile not found: " + id);
            return "Employee not found";
        }

        employee.setUserId(id);
        employee.setLoggedIn(true);

        logger.info("Employee's profile found: " + id);
        return employee.toString();
    }

    /**
     * Edit employee's profile.
     * 
     * @param employee    The employee to edit.
     * @param newUsername The new username of the employee.
     * @param newPassword The new password of the employee.
     * @param newFullName The new full name of the employee.
     * @param newEmail    The new email of the employee.
     * @return Employee, with the new profile.
     */
    public Employee editProfile(Employee employee, String newUsername, String newPassword, String newFullName,
            String newEmail) {
        logger.info("Editing employee profile: " + employee.getUsername());
        int id = employee.getUserId();

        if (userDAO.update(employee, newUsername, newPassword, newFullName, newEmail)) {
            employee.setUserId(id);
            employee.setUsername(newUsername);
            employee.setPassword(newPassword);
            employee.setFullName(newFullName);
            employee.setEmail(newEmail);
            employee.setLoggedIn(true);

            currentEmployee = employee;
            logger.info("Employee profile edited successfully: " + employee.getUsername());
        }

        logger.info("Employee's profile editing faild or no change was made: " + employee.getUsername());
        return employee;
    }
}
