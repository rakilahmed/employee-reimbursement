package com.rakilahmed.controllers.reimbursement;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.rakilahmed.models.reimbursement.Reimbursement;
import com.rakilahmed.services.reimbursement.ReimbursementDAO;

public class ReimbursementController {
    private final ReimbursementDAO reimbursementDAO;
    private final Logger logger = LogManager.getLogger(ReimbursementController.class);

    /**
     * Parameterized constructor for EmployeeController class.
     * 
     * @param reimbursementDAO The DAO class to set.
     */
    public ReimbursementController(ReimbursementDAO reimbursementDAO) {
        this.reimbursementDAO = reimbursementDAO;
    }

    /**
     * Creates a new reimbursement.
     * 
     * @param reimbursement The reimbursement to add.
     * @return Id of the reimbursement.
     */
    public int create(Reimbursement reimbursement) {
        logger.info("Creating reimbursement. Employee ID: " + reimbursement.getEmployeeId());

        if (reimbursement.getId() > 0 && reimbursementDAO.exists(reimbursement)) {
            logger.warn("Reimbursement already exists: " + reimbursement.getId());
            return -1;
        }

        int id = reimbursementDAO.insert(reimbursement);

        if (id <= 0) {
            logger.warn("Reimbursement creation failed: " + reimbursement.getId());
            return -1;
        }

        logger.info("Reimbursement created successfully: " + id);
        return id;
    }

    /**
     * Updates a reimbursement.
     * 
     * @param id        The id of the reimbursement to get.
     * @param managerId The id of the manager who is getting the reimbursement.
     * @param status    The status of the reimbursement to get.
     * @return String indicating if the update was successful or not.
     */
    public String update(int id, int managerId, String status) {
        logger.info("Updating reimbursement. Reimbursement ID: " + id);

        if (id <= 0) {
            logger.warn("Reimbursement ID is invalid: " + id);
            return "Reimbursement id is invalid. Reimbursement ID: " + id;
        }

        if (managerId <= 0) {
            logger.warn("Manager ID is invalid: " + managerId);
            return "Manager id is invalid. Manager ID: " + managerId;
        }

        if (status == null || status.isEmpty() || !status.equals("APPROVED") && !status.equals("DENIED")) {
            logger.warn("Status is invalid: " + status);
            return "Status is invalid. Status: " + status;
        }

        Reimbursement reimbursement = reimbursementDAO.get(id);

        if (!reimbursementDAO.exists(reimbursement)) {
            logger.warn("Reimbursement does not exist: " + id);
            return "Reimbursement does not exist";
        }

        reimbursement.setManagerId(managerId);
        reimbursement.setStatus(status);

        if (!reimbursementDAO.update(id, reimbursement)) {
            logger.warn("Reimbursement update failed: " + id);
            return "Reimbursement update failed";
        }

        logger.info("Reimbursement updated successfully: " + id);
        return "Reimbursement updated successfully. Reimbursement ID: " + id;
    }

    /**
     * Returns the reimbursement with the given id.
     * 
     * @param id The id of the reimbursement to get.
     * @return String indicating if the retrieval was successful or not.
     */
    public Reimbursement get(int id) {
        if (id <= 0) {
            logger.warn("Reimbursement ID is invalid: " + id);
            return null;
        }

        logger.info("Getting reimbursement. ID: " + id);

        Reimbursement reimbursement = reimbursementDAO.get(id);

        if (!reimbursementDAO.exists(reimbursement)) {
            logger.warn("Reimbursement does not exist: " + id);
            return null;
        }

        logger.info("Reimbursement retrieved successfully: " + id);
        return reimbursement;
    }

    /**
     * Returns all reimbursements from the database.
     * 
     * @return All reimbursements.
     */
    public List<Reimbursement> getAll() {
        logger.info("Getting all reimbursements");

        List<Reimbursement> reimbursements = reimbursementDAO.getAll();

        if (reimbursements.size() == 0) {
            logger.warn("No reimbursements found");
            return null;
        }

        logger.info("Reimbursements retrieved successfully");
        return reimbursements;
    }

    /**
     * Returns all pending reimbursements from the database.
     * 
     * @return All pending reimbursements.
     */
    public List<Reimbursement> getAllPending() {
        logger.info("Getting all pending reimbursements");

        List<Reimbursement> reimbursements = reimbursementDAO.getAllPending();

        if (reimbursements.size() == 0) {
            logger.warn("No pending reimbursements found");
            return null;
        }

        logger.info("Pending reimbursements retrieved successfully");
        return reimbursements;
    }

    /**
     * Returns all resolved reimbursements from the database.
     * 
     * @return All resolved reimbursements.
     */
    public List<Reimbursement> getAllResolved() {
        logger.info("Getting all resolved reimbursements");

        List<Reimbursement> reimbursements = reimbursementDAO.getAllResolved();

        if (reimbursements.size() == 0) {
            logger.warn("No resolved reimbursements found");
            return null;
        }

        logger.info("Resolved reimbursements retrieved successfully");
        return reimbursements;
    }

    /**
     * Returns all reimbursements from the database for the given employee.
     * 
     * @param employeeId The id of the employee to get reimbursements for.
     * @return All reimbursements for the employee.
     */
    public List<Reimbursement> getAllForEmployee(int employeeId) {
        if (employeeId <= 0) {
            logger.warn("Employee ID is invalid: " + employeeId);
            return null;
        }

        logger.info("Getting all reimbursements for employee. Employee ID: " + employeeId);

        List<Reimbursement> reimbursements = reimbursementDAO.getAllForEmployee(employeeId);

        if (reimbursements.size() == 0) {
            logger.warn("No reimbursements found for employee: " + employeeId);
            return null;
        }

        logger.info("Reimbursements retrieved successfully for employee: " + employeeId);
        return reimbursements;
    }

    /**
     * Returns all pending reimbursements from the database for the given employee.
     * 
     * @param employeeId The id of the employee to get reimbursements for.
     * @return All pending reimbursements for the employee.
     */
    public List<Reimbursement> getAllPendingForEmployee(int employeeId) {
        if (employeeId <= 0) {
            logger.warn("Employee ID is invalid: " + employeeId);
            return null;
        }

        logger.info("Getting all pending reimbursements for employee. Employee ID: " + employeeId);

        List<Reimbursement> reimbursements = reimbursementDAO.getAllPendingForEmployee(employeeId);

        if (reimbursements.size() == 0) {
            logger.warn("No pending reimbursements found for employee: " + employeeId);
            return null;
        }

        logger.info("Pending reimbursements retrieved successfully for employee: " + employeeId);
        return reimbursements;
    }

    /**
     * Returns all resolved reimbursements from the database for the given employee.
     * 
     * @param employeeId The id of the employee to get reimbursements for.
     * @return All resolved reimbursements for the employee.
     */
    public List<Reimbursement> getAllResolvedForEmployee(int employeeId) {
        if (employeeId <= 0) {
            logger.warn("Employee ID is invalid: " + employeeId);
            return null;
        }

        logger.info("Getting all resolved reimbursements for employee. Employee ID: " + employeeId);

        List<Reimbursement> reimbursements = reimbursementDAO.getAllResolvedForEmployee(employeeId);

        if (reimbursements.size() == 0) {
            logger.warn("No resolved reimbursements found for employee: " + employeeId);
            return null;
        }

        logger.info("Resolved reimbursements retrieved successfully for employee: " + employeeId);
        return reimbursements;
    }
}
