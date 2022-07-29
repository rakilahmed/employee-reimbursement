package com.rakilahmed.controllers.user;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.rakilahmed.models.user.Employee;
import com.rakilahmed.services.user.EmployeeDAO;

public class EmployeeControllerTest {
    private EmployeeDAO employeeDAOMock;
    private EmployeeController employeeController;
    private Employee employee;

    @Before
    public void setUp() {
        employeeDAOMock = mock(EmployeeDAO.class);
        employeeController = new EmployeeController(employeeDAOMock);

        employee = new Employee(1, "rakil", "secret", "Rakil Ahmed", "rakil@email.com");
        employee.setLoggedIn(true);
    }

    @Test
    public void testSuccessfulRegister() {
        String expected = "Employee registered successfully";
        when(employeeDAOMock.insert(employee)).thenReturn(1);
        String actual = employeeController.register(employee);

        assertEquals(expected, actual);
    }

    @Test
    public void testFailedRegister() {
        String expected = "Employee registration failed";
        when(employeeDAOMock.insert(employee)).thenReturn(0);
        String actual = employeeController.register(employee);

        assertEquals(expected, actual);
    }

    @Test
    public void testSuccessfulLogin() {
        String expected = "Employee logged in successfully";
        when(employeeDAOMock.exists(employee)).thenReturn(true);
        String actual = employeeController.login(employee);

        assertEquals(expected, actual);
    }

    @Test
    public void testFailedLogin() {
        String expected = "Employee login failed";
        when(employeeDAOMock.exists(employee)).thenReturn(false);
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
    public void testSuccessfulViewProfile() {
        String expected = employee.toString();
        when(employeeDAOMock.get(employee.getId())).thenReturn(employee);
        when(employeeDAOMock.exists(employee)).thenReturn(true);
        String actual = employeeController.viewProfile(employee.getId());

        assertEquals(expected, actual);
    }

    @Test
    public void testFailedViewProfile() {
        String expected = "Employee profile not found";
        when(employeeDAOMock.get(employee.getId())).thenReturn(employee);
        when(employeeDAOMock.exists(employee)).thenReturn(false);
        String actual = employeeController.viewProfile(employee.getId());

        assertEquals(expected, actual);
    }

    @Test
    public void testSuccessfulEditProfile() {
        String expected = employee.toString();
        when(employeeDAOMock.update(employee.getId(), employee)).thenReturn(employee);
        String actual = (employeeController.editProfile(employee, "test", "test", "test", "test")).toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testSuccssfulGet() {
        String expected = employee.toString();
        when(employeeDAOMock.get(employee.getId())).thenReturn(employee);
        when(employeeDAOMock.exists(employee)).thenReturn(true);
        String actual = employeeController.get(employee.getId()).toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testSuccessfulGetAll() {
        List<Employee> employees = new ArrayList<>();
        employees.add(employee);

        int expected = employees.size();
        when(employeeDAOMock.getAll()).thenReturn(employees);
        int actual = employeeController.getAll().size();

        assertEquals(expected, actual);
    }
}
