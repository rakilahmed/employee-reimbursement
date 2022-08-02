package com.rakilahmed.services.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.rakilahmed.models.user.Employee;
import com.rakilahmed.services.DAO;
import com.rakilahmed.utils.ConnectionManager;

public class EmployeeDAO extends DAO<Employee> {
    private final ConnectionManager connectionManager;
    private final Logger logger = LogManager.getLogger(EmployeeDAO.class);

    /**
     * Default constructor for EmployeeDAO class.
     */
    public EmployeeDAO() {
        this.connectionManager = new ConnectionManager();
    }

    /**
     * Constructor for EmployeeDAO class.
     * 
     * @param connectionManager ConnectionManager object.
     */
    public EmployeeDAO(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    /**
     * Parameterized constructor for EmployeeDAO class.
     *
     * @param url      The URL of the database.
     * @param username The username of the database.
     * @param password The password of the database.
     */
    public EmployeeDAO(String url, String username, String password) {
        this.connectionManager = new ConnectionManager(url, username, password, new org.postgresql.Driver());
    }

    protected int getNextAvailableID() {
        logger.info("Getting next available ID.");

        try {
            Connection connection = connectionManager.getConnection();
            String sql = "SELECT max(user_id) FROM users";

            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            return resultSet.getInt(1) + 1;
        } catch (SQLException e) {
            logger.error("Error getting next available ID.", e);
        } finally {
            connectionManager.close();
            logger.info("Connection closed.");
        }

        return -1;
    }

    public int insert(Employee employee) {
        logger.info("Inserting employee: " + employee.getUsername());

        try {
            Connection connection = connectionManager.getConnection();
            String sql = "INSERT INTO users (user_id, username, password, full_name, email, user_type) VALUES (?, ?, ?, ?, ?, ?) RETURNING user_id";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, getNextAvailableID());
            statement.setString(2, employee.getUsername());
            statement.setString(3, employee.getPassword());
            statement.setString(4, employee.getFullName());
            statement.setString(5, employee.getEmail());
            statement.setString(6, "EMPLOYEE");

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int userId = resultSet.getInt("user_id");
                logger.info("Employee inserted successfully. ID: " + userId);
                return userId;
            } else {
                logger.error("Employee insertion failed.");
            }
        } catch (SQLException e) {
            logger.error("Error inserting employee: " + employee.getUsername(), e);
        } finally {
            connectionManager.close();
            logger.info("Connection closed.");
        }

        return -1;
    }

    public boolean exists(Employee employee) {
        if (employee == null) {
            logger.error("Error checking if employee exists. Employee is null.");
            return false;
        }

        logger.info("Verifying if employee exists: " + employee.getUsername());
        boolean exists = false;

        try {
            Connection connection = connectionManager.getConnection();
            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, employee.getUsername());
            statement.setString(2, employee.getPassword());

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                logger.info("Employee exists: " + employee.getUsername());
                exists = true;
            } else {
                logger.info("Employee does not exist: " + employee.getUsername());
            }
        } catch (SQLException e) {
            logger.error("Error verifying if employee exists: " + employee.getUsername(), e);
        } finally {
            connectionManager.close();
            logger.info("Connection closed.");
        }

        return exists;
    }

    /**
     * Verifies if the employee credentials are valid.
     * 
     * @param username The username of the employee.
     * @param password The password of the employee.
     * @return True if the employee credentials are valid, false otherwise.
     */
    public boolean verify(String username, String password) {
        if (username == null || password == null) {
            logger.error("Error verifying employee credentials. Username or password is null.");
            return false;
        }

        logger.info("Verifying if employee credentials are valid: " + username);
        boolean verified = false;

        try {
            Connection connection = connectionManager.getConnection();
            String sql = "SELECT * FROM users WHERE username = ? AND password = ? AND user_type = 'EMPLOYEE'";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                verified = true;
                logger.info("Employee credentials verified: " + username);
            } else {
                logger.info("Employee credentials not verified: " + username);
            }
        } catch (SQLException e) {
            logger.error("Error verifying if employee credentials are valid: " + username, e);
        } finally {
            connectionManager.close();
            logger.info("Connection closed.");
        }

        return verified;
    }

    public boolean update(int id, Employee updatedEmployee) {
        logger.info("Updating employee with ID: " + id);
        boolean updated = false;

        try {
            Connection connection = connectionManager.getConnection();
            String sql = "UPDATE users SET username = ?, password = ?, full_name = ?, email = ? WHERE user_id = ?";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, updatedEmployee.getUsername());
            statement.setString(2, updatedEmployee.getPassword());
            statement.setString(3, updatedEmployee.getFullName());
            statement.setString(4, updatedEmployee.getEmail());
            statement.setInt(5, id);

            statement.executeUpdate();
            logger.info("Employee updated successfully. ID:  " + id);
            updated = true;
        } catch (SQLException e) {
            logger.error("Error updating employee with ID: " + id, e);
        } finally {
            connectionManager.close();
            logger.info("Connection closed.");
        }

        return updated;
    }

    public Employee get(int id) {
        logger.info("Getting employee with ID: " + id);
        Employee employee = null;

        try {
            Connection connection = connectionManager.getConnection();
            String sql = "SELECT * FROM users WHERE user_id = ? AND user_type = 'EMPLOYEE'";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                employee = new Employee(resultSet.getInt("user_id"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("full_name"),
                        resultSet.getString("email"), true);
            }

            logger.info("Employee retrieved successfully. ID: " + id);
        } catch (SQLException e) {
            logger.error("Error getting employee with ID: " + id, e);
        } finally {
            connectionManager.close();
            logger.info("Connection closed.");
        }

        return employee;
    }

    public List<Employee> getAll() {
        logger.info("Getting all employees");
        List<Employee> employees = new ArrayList<>();

        try {
            Connection connection = connectionManager.getConnection();
            String sql = "SELECT * FROM users WHERE user_type = 'EMPLOYEE' ORDER BY user_id";

            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Employee employee = new Employee(resultSet.getInt("user_id"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("full_name"),
                        resultSet.getString("email"));
                employees.add(employee);
            }

            logger.info("All employees retrieved successfully.");
        } catch (SQLException e) {
            logger.error("Error getting all employees", e);
        } finally {
            connectionManager.close();
            logger.info("Connection closed.");
        }

        return employees;
    }
}
