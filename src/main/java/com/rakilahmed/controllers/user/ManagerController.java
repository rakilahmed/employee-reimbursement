package com.rakilahmed.controllers.user;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.rakilahmed.models.user.Manager;
import com.rakilahmed.services.user.ManagerDAO;

public class ManagerController extends UserController<Manager> {
    private final ManagerDAO managerDAO;
    private Manager currentManager;
    private final Logger logger = LogManager.getLogger(ManagerController.class);

    /**
     * Default constructor for ManagerController class.
     */
    public ManagerController(ManagerDAO managerDAO) {
        this.managerDAO = managerDAO;
    }

    /**
     * Returns the current logged in manager.
     *
     * @return The current logged in manager.
     */
    public Manager getCurrentManager() {
        return currentManager;
    }

    public String register(Manager manager) {
        logger.info("Registering manager: " + manager.getUsername());

        if (managerDAO.exists(manager)) {
            logger.warn("Manager already exists: " + manager.getUsername());
            return "Manager already exists";
        }

        int id = managerDAO.insert(manager);

        if (id <= 0) {
            logger.warn("Manager registration failed: " + manager.getUsername());
            return "Manager registration failed";
        }

        manager.setId(id);
        currentManager = manager;

        logger.info("Manager registered successfully: " + manager.getId());
        return "Manager registered successfully. Manager ID: " + manager.getId();
    }

    public String login(Manager manager) {
        logger.info("Logging in manager: " + manager.getUsername());

        if (!managerDAO.exists(manager)) {
            logger.warn("Manager does not exist: " + manager.getUsername());
            return "Manager does not exist";
        }

        manager.setLoggedIn(true);
        currentManager = manager;

        logger.info("Manager logged in successfully: " + manager.getUsername());
        return "Manager logged in successfully. Manager ID: " + manager.getId();
    }

    public String logout(Manager manager) {
        logger.info("Logging out manager: " + manager.getUsername());

        if (!manager.isLoggedIn()) {
            logger.warn("Manager is not logged in: " + manager.getUsername());
            return "Manager is not logged in";
        }

        manager.setLoggedIn(false);
        currentManager = null;

        logger.info("Manager logged out successfully: " + manager.getUsername());
        return "Manager logged out successfully. Manager ID: " + manager.getId();
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
            logger.warn("Manager profile update failed: " + id);
            return "Manager profile update failed";
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
        manager.setLoggedIn(true);

        if (!managerDAO.update(manager.getId(), manager)) {
            logger.warn("Manager profile update failed: " + id);
            return "Manager profile update failed";
        }

        currentManager = manager;

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
}
