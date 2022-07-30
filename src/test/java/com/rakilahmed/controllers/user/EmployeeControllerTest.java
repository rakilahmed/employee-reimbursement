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
    }

    @Test
    public void testSuccessfulRegister() {
        String expected = "Employee registered successfully. Employee ID: 1";
        when(employeeDAOMock.insert(employee)).thenReturn(1);
        String actual = employeeController.register(employee);

        assertEquals(expected, actual);
    }

    @Test
    public void testEmployeeAlreadyExists() {
        String expected = "Employee already exists";
        when(employeeDAOMock.exists(employee)).thenReturn(true);
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
        String expected = "Employee logged in successfully. Employee ID: 1";
        when(employeeDAOMock.exists(employee)).thenReturn(true);
        String actual = employeeController.login(employee);

        assertEquals(expected, actual);
    }

    @Test
    public void testEmployeeDoesNotExist() {
        String expected = "Employee does not exist";
        when(employeeDAOMock.exists(employee)).thenReturn(false);
        String actual = employeeController.login(employee);

        assertEquals(expected, actual);
    }

    @Test
    public void testSuccessfulLogout() {
        String expected = "Employee logged out successfully. Employee ID: 1";
        employee.setLoggedIn(true);
        String actual = employeeController.logout(employee);

        assertEquals(expected, actual);
    }

    @Test
    public void testEmployeeIsNotLoggedIn() {
        String expected = "Employee is not logged in";
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
    public void testEmployeeIdIsInvalid() {
        String expected = "Employee id is invalid. Employee ID: 0";
        String actual = employeeController.viewProfile(0);

        assertEquals(expected, actual);
    }

    @Test
    public void testEmployeeProfileNotFound() {
        String expected = "Employee profile not found";
        when(employeeDAOMock.get(employee.getId())).thenReturn(employee);
        when(employeeDAOMock.exists(employee)).thenReturn(false);
        String actual = employeeController.viewProfile(employee.getId());

        assertEquals(expected, actual);
    }

    @Test
    public void testSuccessfulUpdateProfile() {
        String expected = "Employee profile updated successfully. Employee ID: 1";
        when(employeeDAOMock.get(employee.getId())).thenReturn(employee);
        when(employeeDAOMock.exists(employee)).thenReturn(true);
        when(employeeDAOMock.update(employee.getId(), employee)).thenReturn(true);
        String actual = employeeController.updateProfile(employee.getId(), "test", "pass", "test pass",
                "test@pass.com");

        assertEquals(expected, actual);
    }

    @Test
    public void testFailedUpdateProfile() {
        String expected = "Employee profile update failed";
        when(employeeDAOMock.get(employee.getId())).thenReturn(employee);
        when(employeeDAOMock.exists(employee)).thenReturn(true);
        when(employeeDAOMock.update(employee.getId(), employee)).thenReturn(false);
        String actual = employeeController.updateProfile(employee.getId(), "test", "pass", "test pass",
                "test@email.com");

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
