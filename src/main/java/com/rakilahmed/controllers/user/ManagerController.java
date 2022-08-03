package com.rakilahmed.controllers.user;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.rakilahmed.models.user.Employee;
import com.rakilahmed.models.user.Manager;
import com.rakilahmed.services.user.EmployeeDAO;
import com.rakilahmed.services.user.ManagerDAO;

public class ManagerController extends UserController<Manager> {
    private final ManagerDAO managerDAO;
    private final EmployeeController employeeController;
    private final Logger logger = LogManager.getLogger(ManagerController.class);

    /**
     * Parameterized constructor for EmployeeController class.
     * 
     * @param managerDAO The DAO class to set.
     */
    public ManagerController(ManagerDAO managerDAO) {
        this.managerDAO = managerDAO;
        this.employeeController = new EmployeeController(new EmployeeDAO());
    }

    public int register(Manager manager) {
        logger.info("Registering manager: " + manager.getUsername());

        if (manager.getId() > 0 && managerDAO.exists(manager)) {
            logger.warn("Manager already exists: " + manager.getUsername());
            return -1;
        }

        int id = managerDAO.insert(manager);

        if (id <= 0) {
            logger.warn("Manager registration failed: " + manager.getUsername());
            return -1;
        }

        logger.info("Manager registered successfully: " + id);
        return id;
    }

    /**
     * Manager registers an employee.
     * 
     * @param employee The employee to be registered.
     * @return String indicating success or failure.
     */
    public String registerEmployee(Employee employee) {
        logger.info("Registering employee: " + employee.getUsername());
        int id = employeeController.register(employee);

        if (id > 0) {
            logger.info("Employee registered successfully: " + id);
            return "Employee registered successfully. Employee ID: " + id;
        }

        logger.warn("Employee registration failed: " + employee.getUsername());
        return "Employee registration failed";
    }

    public int login(String username, String password) {
        logger.info("Logging in manager: " + username);

        int id = managerDAO.verify(username, password);

        if (id <= 0) {
            logger.warn("Manager credentials are incorrect: " + username);
            return -1;
        }

        logger.info("Manager logged in successfully: " + username);
        return id;
    }

    public String viewProfile(int id) {
        if (id <= 0) {
            logger.warn("Manager id is invalid: " + id);
            return "Manager id is invalid. Manager ID: " + id;
        }

        logger.info("Viewing manager profile: " + id);
        Manager manager = managerDAO.get(id);

        if (!managerDAO.exists(manager)) {
            logger.warn("Manager profile not found: " + id);
            return "Manager profile not found";
        }

        logger.info("Manager profile found: " + id);
        return manager.toString();
    }

    public String updateProfile(int id, String username, String password, String fullName, String email) {
        logger.info("Updating manager profile: " + id);

        if (id <= 0) {
            logger.warn("Manager id is invalid: " + id);
            return "Manager id is invalid. Manager ID: " + id;
        }

        if (username == null || username.isEmpty() || password == null || password.isEmpty() || fullName == null
                || fullName.isEmpty() || email == null || email.isEmpty()) {
            logger.warn("Username, password, full name, or email is empty: " + id);
            return "Username, password, name, or email cannot be empty";
        }

        Manager manager = managerDAO.get(id);

        if (!managerDAO.exists(manager)) {
            logger.warn("Manager profile not found: " + id);
            return "Manager profile not found";
        }

        manager.setUsername(username);
        manager.setPassword(password);
        manager.setFullName(fullName);
        manager.setEmail(email);

        if (!managerDAO.update(manager.getId(), manager)) {
            logger.warn("Manager profile update failed: " + id);
            return "Manager profile update failed";
        }

        logger.info("Manager profile updated successfully: " + id);
        return "Manager profile updated successfully. Manager ID: " + id;
    }

    public Manager get(int id) {
        if (id <= 0) {
            logger.warn("Manager id is invalid: " + id);
            return null;
        }

        logger.info("Getting manager: " + id);
        Manager manager = managerDAO.get(id);

        if (!managerDAO.exists(manager)) {
            logger.warn("Manager not found: " + id);
            return null;
        }

        logger.info("Manager found successfully: " + id);
        return manager;
    }

    public List<Manager> getAll() {
        logger.info("Getting all managers");
        List<Manager> managers = managerDAO.getAll();

        if (managers.isEmpty()) {
            logger.warn("No managers found");
            return null;
        }

        logger.info("All managers found successfully");
        return managers;
    }

    /**
     * Retrieves all registered employees.
     * 
     * @return List of employees.
     */
    public List<Employee> getAllEmployees() {
        logger.info("Getting all employees");
        List<Employee> employees = employeeController.getAll();

        if (employees.isEmpty()) {
            logger.warn("No employees found");
            return null;
        }

        logger.info("All employees found successfully");
        return employees;
    }
}
