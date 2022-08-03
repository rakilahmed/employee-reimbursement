package com.rakilahmed.services.user;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.rakilahmed.models.user.Manager;
import com.rakilahmed.utils.ConnectionManager;

public class ManagerDAOTest {
    private ConnectionManager connectionManagerMock;
    private Connection connectionMock;
    private PreparedStatement statementMock;
    private ResultSet resultSetMock;
    private ManagerDAO managerDAO;
    private Manager testManager;

    @Before
    public void setUp() throws SQLException {
        connectionManagerMock = mock(ConnectionManager.class);
        statementMock = mock(PreparedStatement.class);
        resultSetMock = mock(ResultSet.class);
        connectionMock = mock(Connection.class);
        managerDAO = new ManagerDAO(connectionManagerMock);
        testManager = new Manager(1, "rakil", "secret", "Rakil Ahmed", "rakil@email.com");

        when(connectionMock.prepareStatement(anyString())).thenReturn(statementMock);
        when(connectionMock.prepareStatement(anyString(), anyInt())).thenReturn(statementMock);
        when(connectionManagerMock.getConnection()).thenReturn(connectionMock);

        when(statementMock.executeQuery()).thenReturn(resultSetMock);
        when(resultSetMock.next()).thenReturn(true).thenReturn(false);
        when(resultSetMock.getInt("user_id")).thenReturn(1);
        when(resultSetMock.getString("username")).thenReturn(testManager.getUsername());
        when(resultSetMock.getString("password")).thenReturn(testManager.getPassword());
        when(resultSetMock.getString("full_name")).thenReturn(testManager.getFullName());
        when(resultSetMock.getString("email")).thenReturn(testManager.getEmail());
    }

    @Test
    public void testConnectionNotNull() {
        assertNotNull(connectionMock);
    }

    @Test
    public void testSucessfulInsert() throws SQLException {
        int expected = testManager.getId();
        Manager employee = new Manager("rakil", "secret", "Rakil Ahmed",
                "rakil@email.com");

        assertEquals(expected, managerDAO.insert(employee));
    }

    @Test
    public void testFailedInsert() throws SQLException {
        Manager employee = new Manager("rakil", "secret", "Rakil Ahmed", "rakil@email.com");
        when(statementMock.executeQuery()).thenThrow(new SQLException());
        assertEquals(-1, managerDAO.insert(employee));
    }

    @Test
    public void testSuccessfulExists() throws SQLException {
        boolean expected = true;
        Manager employee = new Manager(1, "rakil", "secret", "Rakil Ahmed", "rakil@email.com");

        if (testManager.toString().equals(employee.toString())) {
            when(resultSetMock.next()).thenReturn(true);
        } else {
            when(resultSetMock.next()).thenReturn(false);
        }

        boolean actual = managerDAO.exists(employee);
        assertEquals(expected, actual);
    }

    @Test
    public void testFailedExists() throws SQLException {
        assertEquals(false, managerDAO.exists(null));

        Manager employee = new Manager(2, "rakil1", "secret", "Rakil Ahmed", "rakil1@email.com");
        when(statementMock.executeQuery()).thenThrow(new SQLException());
        assertEquals(false, managerDAO.exists(employee));
    }

    @Test
    public void testSuccessfulVerify() throws SQLException {
        boolean expected = true;
        Manager employee = new Manager("rakil", "secret", "Rakil Ahmed", "rakil@email.com");

        if (employee.getUsername().equals(testManager.getUsername())
                && employee.getPassword().equals(testManager.getPassword())) {
            when(resultSetMock.next()).thenReturn(true);
        } else {
            when(resultSetMock.next()).thenReturn(false);
        }

        boolean actual = managerDAO.verify(employee.getUsername(), employee.getPassword());
        assertEquals(expected, actual);
    }

    @Test
    public void testFailedVerify() throws SQLException {
        assertEquals(false, managerDAO.verify(null, null));

        when(statementMock.executeQuery()).thenThrow(new SQLException());
        assertEquals(false, managerDAO.verify("rakil1", "secret"));
    }

    @Test
    public void testSuccessfulUpdate() throws SQLException {
        Manager employee = new Manager(1, "edited", "edited", "Rakil Ahmed", "rakil@email.com");
        when(resultSetMock.getString("username")).thenReturn(employee.getUsername());
        when(resultSetMock.getString("password")).thenReturn(employee.getPassword());
        when(resultSetMock.getString("full_name")).thenReturn(employee.getFullName());
        when(resultSetMock.getString("email")).thenReturn(employee.getEmail());
        assertEquals(true, managerDAO.update(employee.getId(), employee));

        Manager updatedEmployee = managerDAO.get(employee.getId());
        assertEquals(employee.toString(), updatedEmployee.toString());
    }

    @Test
    public void testFailedUpdate() throws SQLException {
        Manager employee = new Manager(1, "edited", "edited", "Rakil Ahmed", "rakil@email.com");
        when(statementMock.executeUpdate()).thenThrow(new SQLException());
        assertEquals(false, managerDAO.update(employee.getId(), employee));
    }

    @Test
    public void testSuccessfulGet() throws SQLException {
        Manager expected = new Manager(1, "rakil", "secret", "Rakil Ahmed", "rakil@email.com");
        Manager actual = managerDAO.get(1);

        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testFailedGet() throws SQLException {
        when(statementMock.executeQuery()).thenThrow(new SQLException());
        assertEquals(null, managerDAO.get(2));
    }

    @Test
    public void testSuccessfulGetAll() throws SQLException {
        List<Manager> employees = managerDAO.getAll();
        Manager employee = employees.get(0);

        assertEquals(1, employees.size());
        assertEquals(testManager.toString(), employee.toString());
    }

    @Test
    public void testFailedGetAll() throws SQLException {
        when(statementMock.executeQuery()).thenThrow(new SQLException());
        assertEquals(new ArrayList<>(), managerDAO.getAll());
    }
}
