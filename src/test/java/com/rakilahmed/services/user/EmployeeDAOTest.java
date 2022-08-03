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

import com.rakilahmed.models.user.Employee;
import com.rakilahmed.utils.ConnectionManager;

public class EmployeeDAOTest {
    private ConnectionManager connectionManagerMock;
    private Connection connectionMock;
    private PreparedStatement statementMock;
    private ResultSet resultSetMock;
    private EmployeeDAO employeeDAO;
    private Employee testEmployee;

    @Before
    public void setUp() throws SQLException {
        connectionManagerMock = mock(ConnectionManager.class);
        statementMock = mock(PreparedStatement.class);
        resultSetMock = mock(ResultSet.class);
        connectionMock = mock(Connection.class);
        employeeDAO = new EmployeeDAO(connectionManagerMock);
        testEmployee = new Employee(1, "rakil", "secret", "Rakil Ahmed", "rakil@email.com");

        when(connectionMock.prepareStatement(anyString())).thenReturn(statementMock);
        when(connectionMock.prepareStatement(anyString(), anyInt())).thenReturn(statementMock);
        when(connectionManagerMock.getConnection()).thenReturn(connectionMock);

        when(statementMock.executeQuery()).thenReturn(resultSetMock);
        when(resultSetMock.next()).thenReturn(true).thenReturn(false);
        when(resultSetMock.getInt("user_id")).thenReturn(1);
        when(resultSetMock.getString("username")).thenReturn(testEmployee.getUsername());
        when(resultSetMock.getString("password")).thenReturn(testEmployee.getPassword());
        when(resultSetMock.getString("full_name")).thenReturn(testEmployee.getFullName());
        when(resultSetMock.getString("email")).thenReturn(testEmployee.getEmail());
    }

    @Test
    public void testConnectionNotNull() {
        assertNotNull(connectionMock);
    }

    @Test
    public void testSucessfulInsert() throws SQLException {
        int expected = testEmployee.getId();
        Employee employee = new Employee("rakil", "secret", "Rakil Ahmed",
                "rakil@email.com");

        assertEquals(expected, employeeDAO.insert(employee));
    }

    @Test
    public void testFailedInsert() throws SQLException {
        Employee employee = new Employee("rakil", "secret", "Rakil Ahmed", "rakil@email.com");
        when(statementMock.executeQuery()).thenThrow(new SQLException());
        assertEquals(-1, employeeDAO.insert(employee));
    }

    @Test
    public void testSuccessfulExists() throws SQLException {
        boolean expected = true;
        Employee employee = new Employee(1, "rakil", "secret", "Rakil Ahmed", "rakil@email.com");

        if (testEmployee.toString().equals(employee.toString())) {
            when(resultSetMock.next()).thenReturn(true);
        } else {
            when(resultSetMock.next()).thenReturn(false);
        }

        boolean actual = employeeDAO.exists(employee);
        assertEquals(expected, actual);
    }

    @Test
    public void testFailedExists() throws SQLException {
        assertEquals(false, employeeDAO.exists(null));

        Employee employee = new Employee(2, "rakil1", "secret", "Rakil Ahmed", "rakil1@email.com");
        when(statementMock.executeQuery()).thenThrow(new SQLException());
        assertEquals(false, employeeDAO.exists(employee));
    }

    @Test
    public void testSuccessfulVerify() throws SQLException {
        int expected = 1;
        Employee employee = new Employee("rakil", "secret", "Rakil Ahmed", "rakil@email.com");

        if (employee.getUsername().equals(testEmployee.getUsername())
                && employee.getPassword().equals(testEmployee.getPassword())) {
            when(resultSetMock.next()).thenReturn(true);
        } else {
            when(resultSetMock.next()).thenReturn(false);
        }

        int actual = employeeDAO.verify(employee.getUsername(), employee.getPassword());
        assertEquals(expected, actual);
    }

    @Test
    public void testFailedVerify() throws SQLException {
        assertEquals(-1, employeeDAO.verify(null, null));

        when(statementMock.executeQuery()).thenThrow(new SQLException());
        assertEquals(-1, employeeDAO.verify("rakil1", "secret"));
    }

    @Test
    public void testSuccessfulUpdate() throws SQLException {
        Employee employee = new Employee(1, "edited", "edited", "Rakil Ahmed", "rakil@email.com");
        when(resultSetMock.getString("username")).thenReturn(employee.getUsername());
        when(resultSetMock.getString("password")).thenReturn(employee.getPassword());
        when(resultSetMock.getString("full_name")).thenReturn(employee.getFullName());
        when(resultSetMock.getString("email")).thenReturn(employee.getEmail());
        assertEquals(true, employeeDAO.update(employee.getId(), employee));

        Employee updatedEmployee = employeeDAO.get(employee.getId());
        assertEquals(employee.toString(), updatedEmployee.toString());
    }

    @Test
    public void testFailedUpdate() throws SQLException {
        Employee employee = new Employee(1, "edited", "edited", "Rakil Ahmed", "rakil@email.com");
        when(statementMock.executeUpdate()).thenThrow(new SQLException());
        assertEquals(false, employeeDAO.update(employee.getId(), employee));
    }

    @Test
    public void testSuccessfulGet() throws SQLException {
        Employee expected = new Employee(1, "rakil", "secret", "Rakil Ahmed", "rakil@email.com");
        Employee actual = employeeDAO.get(1);

        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testFailedGet() throws SQLException {
        when(statementMock.executeQuery()).thenThrow(new SQLException());
        assertEquals(null, employeeDAO.get(2));
    }

    @Test
    public void testSuccessfulGetAll() throws SQLException {
        List<Employee> employees = employeeDAO.getAll();
        Employee employee = employees.get(0);

        assertEquals(1, employees.size());
        assertEquals(testEmployee.toString(), employee.toString());
    }

    @Test
    public void testFailedGetAll() throws SQLException {
        when(statementMock.executeQuery()).thenThrow(new SQLException());
        assertEquals(new ArrayList<>(), employeeDAO.getAll());
    }
}
