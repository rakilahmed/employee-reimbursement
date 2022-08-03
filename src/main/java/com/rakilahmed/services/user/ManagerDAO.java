package com.rakilahmed.services.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.rakilahmed.models.user.Manager;
import com.rakilahmed.services.DAO;
import com.rakilahmed.utils.ConnectionManager;

public class ManagerDAO extends DAO<Manager> {
    private ConnectionManager connectionManager = new ConnectionManager();
    private final Logger logger = LogManager.getLogger(EmployeeDAO.class);

    /**
     * Default constructor for ManagerDAO class.
     */
    public ManagerDAO() {
    }

    /**
     * Parameterized constructor for ManagerDAO class.
     * 
     * @param connectionManager The connection manager to be used.
     */
    public ManagerDAO(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    public int insert(Manager manager) {
        logger.info("Inserting manager: " + manager.getUsername());

        try {
            Connection connection = connectionManager.getConnection();
            String sql = "INSERT INTO users (username, password, full_name, email, user_type) VALUES (?, ?, ?, ?, ?) RETURNING user_id";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, manager.getUsername());
            statement.setString(2, manager.getPassword());
            statement.setString(3, manager.getFullName());
            statement.setString(4, manager.getEmail());
            statement.setString(5, "MANAGER");

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int userId = resultSet.getInt("user_id");
                logger.info("Manager inserted successfully. ID: " + userId);
                return userId;
            }
        } catch (SQLException e) {
            logger.error("Manager insertion failed: " + manager.getUsername());
        } finally {
            connectionManager.close();
            logger.info("Connection closed.");
        }

        return -1;
    }

    /**
     * Verifies if the manager credentials are valid.
     * 
     * @param username The username of the manager.
     * @param password The password of the manager.
     * @return The id of the manager if the credentials are valid, -1 otherwise.
     */
    public int verify(String username, String password) {
        if (username == null || password == null) {
            logger.error("Error verifying manager credentials. Username or password is null.");
            return -1;
        }

        logger.info("Verifying manager credentials. Username: " + username);

        try {
            Connection connection = connectionManager.getConnection();
            String sql = "SELECT * FROM users WHERE username = ? AND password = ? AND user_type = 'MANAGER'";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                logger.info("Manager credentials verified successfully.");
                return resultSet.getInt("user_id");
            }
        } catch (SQLException e) {
            logger.error("Error verifying manager credentials.", e);
        } finally {
            connectionManager.close();
            logger.info("Connection closed.");
        }

        return -1;
    }

    public boolean exists(Manager manager) {
        if (manager == null) {
            logger.error("Error checking if manager exists. Manager is null.");
            return false;
        }

        logger.info("Verifying if manager exists: " + manager.getUsername());
        boolean exists = false;

        try {
            Connection connection = connectionManager.getConnection();
            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, manager.getUsername());
            statement.setString(2, manager.getPassword());

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                exists = true;
            }

            logger.info("Manager verified: " + manager.getUsername());
        } catch (SQLException e) {
            logger.error("Error verifying if manager exists: " + manager.getUsername(), e);
        } finally {
            connectionManager.close();
            logger.info("Connection closed.");
        }

        return exists;
    }

    public boolean update(int id, Manager updatedManager) {
        logger.info("Updating manager with ID: " + id);
        boolean updated = false;

        try {
            Connection connection = connectionManager.getConnection();
            String sql = "UPDATE users SET username = ?, password = ?, full_name = ?, email = ? WHERE user_id = ?";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, updatedManager.getUsername());
            statement.setString(2, updatedManager.getPassword());
            statement.setString(3, updatedManager.getFullName());
            statement.setString(4, updatedManager.getEmail());
            statement.setInt(5, id);

            statement.executeUpdate();
            logger.info("Employee updated successfully. ID:  " + id);
            updated = true;
        } catch (SQLException e) {
            logger.error("Error updating manager with ID: " + id, e);
        } finally {
            connectionManager.close();
            logger.info("Connection closed.");
        }

        return updated;
    }

    public Manager get(int id) {
        logger.info("Getting manager with ID: " + id);
        Manager manager = null;

        try {
            Connection connection = connectionManager.getConnection();
            String sql = "SELECT * FROM users WHERE user_id = ? AND user_type = 'MANAGER'";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                manager = new Manager(resultSet.getInt("user_id"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("full_name"),
                        resultSet.getString("email"));
            }

            logger.info("Manager retrieved successfully. ID: " + id);
        } catch (SQLException e) {
            logger.error("Error retrieving manager with ID: " + id, e);
        } finally {
            connectionManager.close();
            logger.info("Connection closed.");
        }

        return manager;
    }

    public List<Manager> getAll() {
        logger.info("Getting all managers");
        List<Manager> managers = new ArrayList<>();

        try {
            Connection connection = connectionManager.getConnection();
            String sql = "SELECT * FROM users WHERE user_type = 'MANAGER' ORDER BY user_id";

            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                managers.add(new Manager(resultSet.getInt("user_id"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("full_name"),
                        resultSet.getString("email")));
            }

            logger.info("All managers retrieved successfully.");
        } catch (SQLException e) {
            logger.error("Error retrieving all managers", e);
        } finally {
            connectionManager.close();
            logger.info("Connection closed.");
        }

        return managers;
    }
}
