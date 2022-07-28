package com.rakilahmed.controllers.user;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.rakilahmed.models.user.Employee;
import com.rakilahmed.services.UserDAO;

public class EmployeeControllerTest {
    private UserDAO userDAOMock;
    private EmployeeController employeeController;
    private Employee employee;

    @Before
    public void setUp() {
        userDAOMock = mock(UserDAO.class);
        employeeController = new EmployeeController(userDAOMock);

        employee = new Employee("rakil", "secret", "Rakil Ahmed", "rakil@email.com");
        employee.setLoggedIn(true);
    }

    @Test
    public void testSuccessfulRegister() {
        String expected = "Employee registered successfully";
        when(userDAOMock.insert(employee)).thenReturn(1);
        String actual = employeeController.register(employee);

        assertEquals(expected, actual);
    }

    @Test
    public void testFailedRegister() {
        String expected = "Employee registration failed";
        when(userDAOMock.insert(employee)).thenReturn(0);
        String actual = employeeController.register(employee);

        assertEquals(expected, actual);
    }

    @Test
    public void testSuccessfulLogin() {
        String expected = "Employee logged in successfully";
        when(userDAOMock.exists(employee.getUsername(), employee.getPassword())).thenReturn(true);
        String actual = employeeController.login(employee);

        assertEquals(expected, actual);
    }

    @Test
    public void testFailedLogin() {
        String expected = "Employee login failed";
        when(userDAOMock.exists(employee.getUsername(), employee.getPassword())).thenReturn(false);
        String actual = employeeController.login(employee);

        assertEquals(expected, actual);
    }

    @Test
    public void testSuccessfulLogout() {
        String expected = "Employee logged out successfully";
        String actual = employeeController.logout(employee);

        assertEquals(expected, actual);
    }

    @Test
    public void testFailedLogout() {
        String expected = "Employee logout failed";
        employee.setLoggedIn(false);
        String actual = employeeController.logout(employee);

        assertEquals(expected, actual);
    }

    @Test
    public void testViewProfile() {
        String expected = employee.toString();
        when(userDAOMock.getEmployee(employee.getUserId())).thenReturn(employee);
        String actual = employeeController.viewProfile(employee.getUserId());

        assertEquals(expected, actual);
    }

    @Test
    public void testViewProfileNull() {
        String expected = "Employee not found";
        when(userDAOMock.getEmployee(employee.getUserId())).thenReturn(null);
        String actual = employeeController.viewProfile(0);

        assertEquals(expected, actual);
    }
}
