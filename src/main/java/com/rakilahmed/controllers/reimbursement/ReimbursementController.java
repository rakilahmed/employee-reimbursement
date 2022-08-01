package com.rakilahmed.controllers.reimbursement;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.rakilahmed.models.reimbursement.Reimbursement;
import com.rakilahmed.services.reimbursement.ReimbursementDAO;

public class ReimbursementController {
    private ReimbursementDAO reimbursementDAO;
    private Logger logger = LogManager.getLogger(ReimbursementController.class);

    /**
     * Parameterized constructor for EmployeeController class.
     * 
     * @param reimbursementDAO The DAO class to set.
     */
    public ReimbursementController(ReimbursementDAO reimbursementDAO) {
        this.reimbursementDAO = reimbursementDAO;
    }

    /**
     * @param reimbursement The reimbursement to add.
     * @return String indicating if the creation was successful or not.
     */
    public String create(Reimbursement reimbursement) {
        logger.info("Creating reimbursement. Employee ID: " + reimbursement.getEmployeeId());

        if (reimbursement.getId() > 0 && reimbursementDAO.exists(reimbursement)) {
            logger.warn("Reimbursement already exists: " + reimbursement.getId());
            return "Reimbursement already exists";
        }

        int id = reimbursementDAO.insert(reimbursement);

        if (id <= 0) {
            logger.warn("Reimbursement creation failed: " + reimbursement.getId());
            return "Reimbursement creation failed";
        }

        reimbursement.setId(id);

        logger.info("Reimbursement created successfully: " + reimbursement.getId());
        return "Reimbursement created successfully. Reimbursement ID: " + reimbursement.getId();
    }

    /**
     * @param id        The id of the reimbursement to get.
     * @param managerId The id of the manager who is getting the reimbursement.
     * @param status    The status of the reimbursement to get.
     * @return String indicating if the update was successful or not.
     */
    public String update(int id, int managerId, String status) {
        logger.info("Updating reimbursement. Reimbursement ID: " + id);

        if (id <= 0) {
            logger.warn("Reimbursement ID is invalid: " + id);
            return "Reimbursement ID is invalid";
        }

        if (managerId <= 0) {
            logger.warn("Manager ID is invalid: " + managerId);
            return "Manager ID is invalid";
        }

        if (status == null || status.isEmpty()) {
            logger.warn("Status is invalid: " + status);
            return "Status is invalid";
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

        if (reimbursements.size() == 0 || reimbursements == null) {
            logger.warn("No reimbursements found");
            return null;
        }

        logger.info("Reimbursements retrieved successfully");
        return reimbursements;
    }
}
