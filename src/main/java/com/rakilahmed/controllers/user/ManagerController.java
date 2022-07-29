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
        if (manager == null) {
            logger.warn("Manager is null");
            return "Manager is null";
        } else if (managerDAO.exists(manager)) {
            logger.warn("Manager already exists");
            return "Manager already exists";
        }

        logger.info("Registering manager: " + manager.getId());
        int id = managerDAO.insert(manager);

        if (id > 0) {
            manager.setUserId(id);
            currentManager = manager;

            logger.info("Manager registered successfully: " + manager.getId());
            return "Manager registered successfully";
        }

        currentManager = manager;

        logger.warn("Manager registration failed: " + manager.getId());
        return "Manager registration failed";
    }

    public String login(Manager manager) {
        if (manager == null) {
            logger.warn("Manager is null");
            return "Manager is null";
        }

        logger.info("Logging in manager: " + manager.getId());

        if (managerDAO.exists(manager)) {
            manager.setLoggedIn(true);
            currentManager = manager;

            logger.info("Manager logged in successfully: " + manager.getId());
            return "Manager logged in successfully";
        }

        currentManager = manager;

        logger.warn("Manager login failed: " + manager.getId());
        return "Manager login failed";
    }

    public String logout(Manager manager) {
        if (manager == null) {
            logger.warn("Manager is null");
            return "Manager is null";
        }

        logger.info("Logging out manager: " + manager.getId());

        if (manager.isLoggedIn()) {
            manager.setLoggedIn(false);
            currentManager = null;

            logger.info("Manager logged out successfully: " + manager.getId());
            return "Manager logged out successfully";
        }

        currentManager = manager;

        logger.warn("Manager logout failed: " + manager.getId());
        return "Manager logout failed";
    }

    public String viewProfile(int id) {
        if (id <= 0) {
            logger.warn("Manager id is invalid: " + id);
            return "Manager id is invalid";
        }

        logger.info("Viewing manager profile: " + id);
        Manager manager = managerDAO.get(id);

        if (manager == null) {
            logger.warn("Manager is null");
            return "Manager is null";
        } else if (!managerDAO.exists(manager)) {
            logger.warn("Manager profile not found: " + id);
            return "Manager profile not found";
        }

        logger.info("Manager profile found: " + id);
        return manager.toString();
    }

    public Manager editProfile(Manager manager, String newUsername, String newPassword, String newFullName,
            String newEmail) {
        if (manager == null) {
            logger.warn("Manager is null");
            return null;
        }

        logger.info("Editing manager profile: " + manager.getId());

        if (managerDAO.exists(manager)) {
            manager.setUsername(newUsername);
            manager.setPassword(newPassword);
            manager.setFullName(newFullName);
            manager.setEmail(newEmail);
            managerDAO.update(manager.getId(), manager);

            logger.info("Manager profile edited successfully: " + manager.getId());

        } else {
            logger.warn("Manager profile not found: " + manager.getId());
        }

        return manager;
    }

    public Manager get(int id) {
        if (id <= 0) {
            logger.warn("Manager id is invalid: " + id);
            return null;
        }

        logger.info("Getting manager: " + id);

        Manager manager = managerDAO.get(id);

        if (manager == null) {
            logger.warn("Manager is null");
            return null;
        } else if (!managerDAO.exists(manager)) {
            logger.warn("Manager not found: " + id);
            return null;
        }

        logger.info("Manager found: " + id);
        return manager;
    }

    public List<Manager> getAll() {
        logger.info("Getting all managers");
        List<Manager> managers = managerDAO.getAll();

        if (managers != null) {
            logger.info("Retrieved all managers successfully: " + managers.size());
            return managers;
        }

        logger.info("Failed to retrieve all managers");
        return null;
    }
}
