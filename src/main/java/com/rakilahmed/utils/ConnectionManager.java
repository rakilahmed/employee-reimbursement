package com.rakilahmed.utils;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
    private final String url;
    private final String username;
    private final String password;
    private final Driver driver;
    private final boolean driverRegistered;

    /**
     * Default constructor for ConnectionManager class.
     */
    public ConnectionManager() {
        this.url = "jdbc:postgresql://revature-db.cn6l9mwmtabj.us-east-1.rds.amazonaws.com:5432/ers";
        this.username = "postgres";
        this.password = "1121Revature";
        this.driver = new org.postgresql.Driver();
        this.driverRegistered = false;
    }

    /**
     * Registers the driver.
     *
     * @throws SQLException if the driver cannot be registered.
     */
    private void registerDriver() throws SQLException {
        if (!driverRegistered) {
            DriverManager.registerDriver(this.driver);
        }
    }

    /**
     * Creates a connection.
     *
     * @return the connection
     * @throws SQLException if the connection cannot be created.
     */
    public Connection getConnection() throws SQLException {
        if (!driverRegistered) {
            this.registerDriver();
        }

        try {
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new SQLException("Failed to get connection: " + e.getMessage());
        }
    }

    /**
     * Closes the current open connection.
     */
    public void close() {
        try {
            this.getConnection().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
