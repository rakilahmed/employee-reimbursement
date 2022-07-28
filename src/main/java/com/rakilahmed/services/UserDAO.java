package com.rakilahmed.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.rakilahmed.models.user.Employee;
import com.rakilahmed.models.user.User;
import com.rakilahmed.utils.ConnectionManager;

public class UserDAO {
    private ConnectionManager connectionManager;
    private Logger logger = LogManager.getLogger(UserDAO.class);

    /**
     * Default constructor for UserDAO class.
     */
    public UserDAO() {
        this.connectionManager = new ConnectionManager();
    }

    /**
     * Parameterized constructor for UserDAO class.
     *
     * @param url      The URL of the database.
     * @param username The username of the database.
     * @param password The password of the database.
     */
    public UserDAO(String url, String username, String password, String driver) {
        this.connectionManager = new ConnectionManager(url, username, password, new org.postgresql.Driver());
    }

    /**
     * Inserts a new user to the database.
     * 
     * @param user The user to insert.
     * @return User's ID if the user was inserted successfully, -1 otherwise.
     */
    public int insert(User user) {
        logger.info("Inserting user: " + user.getUsername());

        try {
            Connection connection = connectionManager.getConnection();
            String sql = "INSERT INTO users (username, password, full_name, email, user_type) VALUES (?, ?, ?, ?, ?) RETURNING user_id";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getFullName());
            statement.setString(4, user.getEmail());
            statement.setString(5, user instanceof Employee ? "EMPLOYEE" : "MANAGER");

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int userId = resultSet.getInt("user_id");
                logger.info("User inserted successfully. User ID: " + userId);
                return userId;
            } else {
                logger.error("User insertion failed.");
                return -1;
            }
        } catch (SQLException e) {
            logger.error("Error inserting user: " + user.getUsername(), e);
        } finally {
            connectionManager.close();
            logger.info("Connection closed.");
        }

        return -1;
    }

    /**
     * Verifies if a user exists in the database.
     * 
     * @param username The username of the user.
     * @param password The password of the user.
     * @return True if the user exists, false otherwise.
     */
    public boolean exists(String username, String password) {
        logger.info("Verifying if user exists: " + username);
        boolean exists = false;

        try {
            Connection connection = connectionManager.getConnection();
            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                exists = true;
            }

            logger.info("User verified: " + username);
        } catch (SQLException e) {
            logger.error("Error verifying if user exists: " + username, e);
        } finally {
            connectionManager.close();
            logger.info("Connection closed.");
        }

        return exists;
    }

    /**
     * Updates a user in the database.
     * 
     * @param user        The user to update.
     * @param newUsername The new username of the user.
     * @param newPassword The new password of the user.
     * @param newFullName The new full name of the user.
     * @param newEmail    The new email of the user.
     * @return True if the user was updated, false otherwise.
     */
    public boolean update(User user, String newUsername, String newPassword, String newFullName, String newEmail) {
        logger.info("Updating user: " + user.getUsername());
        boolean updated = false;

        try {
            Connection connection = connectionManager.getConnection();
            String sql = "UPDATE users SET username = ?, password = ?, full_name = ?, email = ? WHERE user_id = ?";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, newUsername);
            statement.setString(2, newPassword);
            statement.setString(3, newFullName);
            statement.setString(4, newEmail);
            statement.setInt(5, user.getUserId());

            statement.executeUpdate();
            updated = true;

            logger.info("User updated successfully. User ID: " + user.getUserId());
        } catch (SQLException e) {
            logger.error("Error updating user: " + user.getUsername(), e);
        } finally {
            connectionManager.close();
            logger.info("Connection closed.");
        }

        return updated;
    }

    /**
     * Gets the user with the given ID.
     * 
     * @param id The ID of the user.
     * @return User with the given ID.
     */
    public Employee getEmployee(int id) {
        logger.info("Getting employee with ID: " + id);
        Employee employee = null;

        try {
            Connection connection = connectionManager.getConnection();
            String sql = "SELECT * FROM users WHERE user_id = ? AND user_type = 'EMPLOYEE'";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                employee = new Employee(resultSet.getString("username"), resultSet.getString("password"),
                        resultSet.getString("full_name"), resultSet.getString("email"));
            }

            logger.info("Employee retrieved successfully. Employee ID: " + id);
        } catch (SQLException e) {
            logger.error("Error getting employee with ID: " + id, e);
        } finally {
            connectionManager.close();
            logger.info("Connection closed.");
        }

        return employee;
    }
}
