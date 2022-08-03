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
        int expected = 1;
        when(employeeDAOMock.insert(employee)).thenReturn(1);
        int actual = employeeController.register(employee);

        assertEquals(expected, actual);
    }

    @Test
    public void testFailedRegister() {
        int expectedAlreadyExists = -1;
        when(employeeDAOMock.exists(employee)).thenReturn(true);
        assertEquals(expectedAlreadyExists, employeeController.register(employee));

        int expectedRegistrationFailed = -1;
        when(employeeDAOMock.exists(employee)).thenReturn(false);
        when(employeeDAOMock.insert(employee)).thenReturn(0);
        assertEquals(expectedRegistrationFailed, employeeController.register(employee));
    }

    @Test
    public void testSuccessfulLogin() {
        int expected = 1;
        when(employeeDAOMock.verify(employee.getUsername(), employee.getPassword())).thenReturn(1);
        int actual = employeeController.login(employee.getUsername(), employee.getPassword());

        assertEquals(expected, actual);
    }

    @Test
    public void testFailedLogin() {
        int expected = -1;
        when(employeeDAOMock.exists(employee)).thenReturn(false);
        int actual = employeeController.login(employee.getUsername(), employee.getPassword());

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
        String expectedInvalidId = "Employee id is invalid. Employee ID: 0";
        assertEquals(expectedInvalidId, employeeController.viewProfile(0));

        String expectedProfileNotFound = "Employee profile not found";
        when(employeeDAOMock.get(employee.getId())).thenReturn(employee);
        when(employeeDAOMock.exists(employee)).thenReturn(false);
        assertEquals(expectedProfileNotFound, employeeController.viewProfile(employee.getId()));
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
        String expectedInvalidId = "Employee id is invalid. Employee ID: 0";
        assertEquals(expectedInvalidId,
                employeeController.updateProfile(0, "test", "pass", "test pass", "test@pass.com"));

        String expectedEmptyValues = "Username, password, full name, and email cannot be empty";
        assertEquals(expectedEmptyValues, employeeController.updateProfile(employee.getId(), "", "", "", ""));

        String expectedProfileNotFound = "Employee profile not found";
        when(employeeDAOMock.get(employee.getId())).thenReturn(employee);
        when(employeeDAOMock.exists(employee)).thenReturn(false);
        assertEquals(expectedProfileNotFound,
                employeeController.updateProfile(employee.getId(), "test", "pass", "test pass",
                        "test@pass.com"));

        String expectedProfileUpdateFailed = "Employee profile update failed";
        when(employeeDAOMock.get(employee.getId())).thenReturn(employee);
        when(employeeDAOMock.exists(employee)).thenReturn(true);
        when(employeeDAOMock.update(employee.getId(), employee)).thenReturn(false);
        assertEquals(expectedProfileUpdateFailed,
                employeeController.updateProfile(employee.getId(), "test", "pass", "test pass",
                        "test@email.com"));
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
    public void testFailedGet() {
        assertEquals(null, employeeController.get(0));

        when(employeeDAOMock.get(employee.getId())).thenReturn(null);
        assertEquals(null, employeeController.get(employee.getId()));
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

    @Test
    public void testFailedGetAll() {
        List<Employee> employees = new ArrayList<>();
        when(employeeDAOMock.getAll()).thenReturn(employees);

        assertEquals(null, employeeController.getAll());
    }
}
