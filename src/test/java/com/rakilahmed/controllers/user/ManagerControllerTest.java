package com.rakilahmed.controllers.user;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.rakilahmed.models.user.Manager;
import com.rakilahmed.services.user.ManagerDAO;

public class ManagerControllerTest {
    private ManagerDAO managerDAOMock;
    private ManagerController managerController;
    private Manager manager;

    @Before
    public void setUp() {
        managerDAOMock = mock(ManagerDAO.class);
        managerController = new ManagerController(managerDAOMock);

        manager = new Manager(1, "rakil", "secret", "Rakil Ahmed", "rakil@email.com");
    }

    @Test
    public void testSuccessfulRegister() {
        int expected = 1;
        when(managerDAOMock.insert(manager)).thenReturn(1);
        int actual = managerController.register(manager);

        assertEquals(expected, actual);
    }

    @Test
    public void testFailedRegister() {
        int expectedAlreadyExists = -1;
        when(managerDAOMock.exists(manager)).thenReturn(true);
        assertEquals(expectedAlreadyExists, managerController.register(manager));

        int expectedRegistrationFailed = -1;
        when(managerDAOMock.exists(manager)).thenReturn(false);
        when(managerDAOMock.insert(manager)).thenReturn(0);
        assertEquals(expectedRegistrationFailed, managerController.register(manager));
    }

    @Test
    public void testSuccessfulLogin() {
        int expected = 1;
        when(managerDAOMock.verify(manager.getUsername(), manager.getPassword())).thenReturn(1);
        int actual = managerController.login(manager.getUsername(), manager.getPassword());

        assertEquals(expected, actual);
    }

    @Test
    public void testFailedLogin() {
        int expected = -1;
        when(managerDAOMock.exists(manager)).thenReturn(false);
        int actual = managerController.login(manager.getUsername(), manager.getPassword());

        assertEquals(expected, actual);
    }

    @Test
    public void testSuccessfulViewProfile() {
        String expected = manager.toString();
        when(managerDAOMock.get(manager.getId())).thenReturn(manager);
        when(managerDAOMock.exists(manager)).thenReturn(true);
        String actual = managerController.viewProfile(manager.getId());

        assertEquals(expected, actual);
    }

    @Test
    public void testFailedViewProfile() {
        String expectedInvalidId = "Manager id is invalid. Manager ID: 0";
        assertEquals(expectedInvalidId, managerController.viewProfile(0));

        String expectedProfileNotFound = "Manager profile not found";
        when(managerDAOMock.get(manager.getId())).thenReturn(manager);
        when(managerDAOMock.exists(manager)).thenReturn(false);
        assertEquals(expectedProfileNotFound, managerController.viewProfile(manager.getId()));
    }

    @Test
    public void testSuccessfulUpdateProfile() {
        String expected = "Manager profile updated successfully. Manager ID: 1";
        when(managerDAOMock.get(manager.getId())).thenReturn(manager);
        when(managerDAOMock.exists(manager)).thenReturn(true);
        when(managerDAOMock.update(manager.getId(), manager)).thenReturn(true);
        String actual = managerController.updateProfile(manager.getId(), "test", "pass", "test pass", "test@email.com");

        assertEquals(expected, actual);
    }

    @Test
    public void testFailedUpdateProfile() {
        String expectedInvalidId = "Manager id is invalid. Manager ID: 0";
        assertEquals(expectedInvalidId,
                managerController.updateProfile(0, "test", "pass", "test pass", "test@email.com"));

        String expectedEmptyValues = "Username, password, name, or email cannot be empty";
        assertEquals(expectedEmptyValues, managerController.updateProfile(manager.getId(), "", "", "", ""));

        String expectedProfileNotFound = "Manager profile not found";
        when(managerDAOMock.get(manager.getId())).thenReturn(manager);
        when(managerDAOMock.exists(manager)).thenReturn(false);
        assertEquals(expectedProfileNotFound,
                managerController.updateProfile(manager.getId(), "test", "pass", "test pass",
                        "test@email.com"));

        String expectedProfileUpdateFailed = "Manager profile update failed";
        when(managerDAOMock.get(manager.getId())).thenReturn(manager);
        when(managerDAOMock.exists(manager)).thenReturn(true);
        when(managerDAOMock.update(manager.getId(), manager)).thenReturn(false);
        assertEquals(expectedProfileUpdateFailed,
                managerController.updateProfile(manager.getId(), "test", "pass", "test pass", "test@email.com"));
    }

    @Test
    public void testSuccessfulGet() {
        String expected = manager.toString();
        when(managerDAOMock.get(manager.getId())).thenReturn(manager);
        when(managerDAOMock.exists(manager)).thenReturn(true);
        String actual = (managerController.get(manager.getId())).toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testFailedGet() {
        assertEquals(null, managerController.get(0));

        when(managerDAOMock.get(manager.getId())).thenReturn(manager);
        assertEquals(null, managerController.get(manager.getId()));
    }

    @Test
    public void testSuccessfulGetAll() {
        List<Manager> managers = new ArrayList<>();
        managers.add(manager);

        int expected = managers.size();
        when(managerDAOMock.getAll()).thenReturn(managers);
        int actual = managerController.getAll().size();

        assertEquals(expected, actual);
    }

    @Test
    public void testFailedGetAll() {
        List<Manager> managers = new ArrayList<>();
        when(managerDAOMock.getAll()).thenReturn(managers);

        assertEquals(null, managerController.getAll());
    }
}
