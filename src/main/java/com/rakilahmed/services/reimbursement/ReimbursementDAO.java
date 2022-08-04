package com.rakilahmed.services.reimbursement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.rakilahmed.models.reimbursement.Reimbursement;
import com.rakilahmed.services.DAO;
import com.rakilahmed.utils.ConnectionManager;

public class ReimbursementDAO extends DAO<Reimbursement> {
    private ConnectionManager connectionManager = new ConnectionManager();
    private final Logger logger = LogManager.getLogger(ReimbursementDAO.class);

    /**
     * Default constructor for ReimbursementDAO class.
     */
    public ReimbursementDAO() {
    }

    /**
     * Constructor for ReimbursementDAO class.
     * 
     * @param connectionManager ConnectionManager object.
     */
    public ReimbursementDAO(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    public int insert(Reimbursement reimbursement) {
        logger.info("Inserting reimbursement. Employee ID: " + reimbursement.getEmployeeId());

        try {
            Connection connection = connectionManager.getConnection();
            String sql = "INSERT INTO reimbursements (user_id, amount, date, manager_id, status) VALUES (?, ?, ?, ?, ?) RETURNING reimbursement_id";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, reimbursement.getEmployeeId());
            statement.setDouble(2, reimbursement.getAmount());
            statement.setString(3, reimbursement.getDateRequested());
            statement.setInt(4, reimbursement.getManagerId());
            statement.setString(5, reimbursement.getStatus());

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt("reimbursement_id");
                logger.info("Reimbursement inserted with ID: " + id);
                return id;
            }
        } catch (SQLException e) {
            logger.error("Error inserting reimbursement.", e);
        } finally {
            connectionManager.close();
            logger.info("Connection closed.");
        }

        return -1;
    }

    public boolean exists(Reimbursement reimbursement) {
        if (reimbursement == null) {
            logger.error("Error checking if reimbursement exists. Reimbursement is null.");
            return false;
        }

        logger.info("Checking if reimbursement exists. ID: " + reimbursement.getId());
        boolean exists = false;

        try {
            Connection connection = connectionManager.getConnection();
            String sql = "SELECT * FROM reimbursements WHERE reimbursement_id = ?";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, reimbursement.getId());

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                logger.info("Reimbursement exists: " + reimbursement.getId());
                exists = true;
            }
        } catch (SQLException e) {
            logger.error("Error checking if reimbursement exists.", e);
        } finally {
            connectionManager.close();
            logger.info("Connection closed.");
        }

        return exists;
    }

    public boolean update(int id, Reimbursement updatedReimbursement) {
        if (updatedReimbursement == null) {
            logger.error("Error updating reimbursement. Reimbursement is null.");
            return false;
        }

        logger.info("Updating reimbursement. ID: " + id);
        boolean updated = false;

        try {
            Connection connection = connectionManager.getConnection();
            String sql = "UPDATE reimbursements SET manager_id = ?, status = ? WHERE reimbursement_id = ?";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, updatedReimbursement.getManagerId());
            statement.setString(2, updatedReimbursement.getStatus());
            statement.setInt(3, id);

            statement.executeUpdate();
            logger.info("Reimbursement updated. ID: " + id);
            updated = true;
        } catch (SQLException e) {
            logger.error("Error updating reimbursement.", e);
        } finally {
            connectionManager.close();
            logger.info("Connection closed.");
        }

        return updated;
    }

    public Reimbursement get(int id) {
        logger.info("Getting reimbursement. ID: " + id);
        Reimbursement reimbursement = null;

        try {
            Connection connection = connectionManager.getConnection();
            String sql = "SELECT * FROM reimbursements WHERE reimbursement_id = ?";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                reimbursement = new Reimbursement(resultSet.getInt("reimbursement_id"),
                        resultSet.getInt("user_id"),
                        resultSet.getDouble("amount"),
                        resultSet.getInt("manager_id"),
                        resultSet.getString("status"));
                logger.info("Reimbursement retrieved. ID: " + id);
            }
        } catch (SQLException e) {
            logger.error("Error getting reimbursement.", e);
        } finally {
            connectionManager.close();
            logger.info("Connection closed.");
        }

        return reimbursement;
    }

    public List<Reimbursement> getAll() {
        logger.info("Getting all reimbursements.");
        List<Reimbursement> reimbursements = new ArrayList<>();

        try {
            Connection connection = connectionManager.getConnection();
            String sql = "SELECT * FROM reimbursements ORDER BY reimbursement_id";

            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                reimbursements.add(new Reimbursement(resultSet.getInt("reimbursement_id"),
                        resultSet.getInt("user_id"),
                        resultSet.getDouble("amount"),
                        resultSet.getInt("manager_id"),
                        resultSet.getString("status")));
            }
            logger.info("All reimbursements retrieved.");
        } catch (SQLException e) {
            logger.error("Error getting all reimbursements.", e);
        } finally {
            connectionManager.close();
            logger.info("Connection closed.");
        }

        return reimbursements;
    }

    /**
     * Gets all pending reimbursements.
     * 
     * @return A list of all pending reimbursements.
     */
    public List<Reimbursement> getAllPending() {
        logger.info("Getting all pending reimbursements.");
        List<Reimbursement> reimbursements = new ArrayList<>();

        try {
            Connection connection = connectionManager.getConnection();
            String sql = "SELECT * FROM reimbursements WHERE status = 'PENDING' ORDER BY reimbursement_id";

            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                reimbursements.add(new Reimbursement(resultSet.getInt("reimbursement_id"),
                        resultSet.getInt("user_id"),
                        resultSet.getDouble("amount"),
                        resultSet.getInt("manager_id"),
                        resultSet.getString("status")));
            }
            logger.info("All pending reimbursements retrieved.");
        } catch (SQLException e) {
            logger.error("Error getting all pending reimbursements.", e);
        } finally {
            connectionManager.close();
            logger.info("Connection closed.");
        }

        return reimbursements;
    }

    /**
     * Gets all resolved reimbursements.
     * 
     * @return A list of all resolved reimbursements.
     */
    public List<Reimbursement> getAllResolved() {
        logger.info("Getting all resolved reimbursements.");
        List<Reimbursement> reimbursements = new ArrayList<>();

        try {
            Connection connection = connectionManager.getConnection();
            String sql = "SELECT * FROM reimbursements WHERE status IN ('APPROVED', 'DENIED') ORDER BY reimbursement_id";

            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                reimbursements.add(new Reimbursement(resultSet.getInt("reimbursement_id"),
                        resultSet.getInt("user_id"),
                        resultSet.getDouble("amount"),
                        resultSet.getInt("manager_id"),
                        resultSet.getString("status")));
            }
            logger.info("All resolved reimbursements retrieved.");
        } catch (SQLException e) {
            logger.error("Error getting all resolved reimbursements.", e);
        } finally {
            connectionManager.close();
            logger.info("Connection closed.");
        }

        return reimbursements;
    }

    /**
     * Gets all reimbursements for an employee.
     * 
     * @param employeeId The ID of the employee.
     * @return A list of all reimbursements for the employee.
     */
    public List<Reimbursement> getAllForEmployee(int employeeId) {
        logger.info("Getting all reimbursements for employee. ID: " + employeeId);
        List<Reimbursement> reimbursements = new ArrayList<>();

        try {
            Connection connection = connectionManager.getConnection();
            String sql = "SELECT * FROM reimbursements WHERE user_id = ? ORDER BY reimbursement_id";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, employeeId);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                reimbursements.add(new Reimbursement(resultSet.getInt("reimbursement_id"),
                        resultSet.getInt("user_id"),
                        resultSet.getDouble("amount"),
                        resultSet.getInt("manager_id"),
                        resultSet.getString("status")));
            }
            logger.info("All reimbursements retrieved for employee. ID: " + employeeId);
        } catch (SQLException e) {
            logger.error("Error getting all reimbursements for employee.", e);
        } finally {
            connectionManager.close();
            logger.info("Connection closed.");
        }

        return reimbursements;
    }

    /**
     * Gets all pending reimbursements for an employee.
     * 
     * @param employeeId The ID of the employee.
     * @return A list of all pending reimbursements for the employee.
     */
    public List<Reimbursement> getAllPendingForEmployee(int employeeId) {
        logger.info("Getting all pending reimbursements for employee. ID: " + employeeId);
        List<Reimbursement> reimbursements = new ArrayList<>();

        try {
            Connection connection = connectionManager.getConnection();
            String sql = "SELECT * FROM reimbursements WHERE user_id = ? AND status = 'PENDING' ORDER BY reimbursement_id";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, employeeId);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                reimbursements.add(new Reimbursement(resultSet.getInt("reimbursement_id"),
                        resultSet.getInt("user_id"),
                        resultSet.getDouble("amount"),
                        resultSet.getInt("manager_id"),
                        resultSet.getString("status")));
            }
            logger.info("All pending reimbursements retrieved for employee. ID: " + employeeId);
        } catch (SQLException e) {
            logger.error("Error getting all pending reimbursements for employee.", e);
        } finally {
            connectionManager.close();
            logger.info("Connection closed.");
        }

        return reimbursements;
    }

    /**
     * Gets all resolved reimbursements for an employee.
     * 
     * @param employeeId The ID of the employee.
     * @return A list of all resolved reimbursements for the employee.
     */
    public List<Reimbursement> getAllResolvedForEmployee(int employeeId) {
        logger.info("Getting all resolved reimbursements for employee. ID: " + employeeId);
        List<Reimbursement> reimbursements = new ArrayList<>();

        try {
            Connection connection = connectionManager.getConnection();
            String sql = "SELECT * FROM reimbursements WHERE user_id = ? AND status IN ('APPROVED', 'DENIED') ORDER BY reimbursement_id";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, employeeId);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                reimbursements.add(new Reimbursement(resultSet.getInt("reimbursement_id"),
                        resultSet.getInt("user_id"),
                        resultSet.getDouble("amount"),
                        resultSet.getInt("manager_id"),
                        resultSet.getString("status")));
            }
            logger.info("All resolved reimbursements retrieved for employee. ID: " + employeeId);
        } catch (SQLException e) {
            logger.error("Error getting all resolved reimbursements for employee.", e);
        } finally {
            connectionManager.close();
            logger.info("Connection closed.");
        }

        return reimbursements;
    }
}
