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
    private ManagerDAO managerDAO;
    private ManagerController managerController;
    private Manager manager;

    @Before
    public void setUp() {
        managerDAO = mock(ManagerDAO.class);
        managerController = new ManagerController(managerDAO);

        manager = new Manager(1, "rakil", "secret", "Rakil Ahmed", "rakil@email.com");
        manager.setLoggedIn(true);
    }

    @Test
    public void testSuccessfulRegister() {
        String expected = "Manager registered successfully";
        when(managerDAO.insert(manager)).thenReturn(1);
        String actual = managerController.register(manager);

        assertEquals(expected, actual);
    }

    @Test
    public void testFailedRegister() {
        String expected = "Manager registration failed";
        when(managerDAO.insert(manager)).thenReturn(0);
        String actual = managerController.register(manager);

        assertEquals(expected, actual);
    }

    @Test
    public void testSuccessfulLogin() {
        String expected = "Manager logged in successfully";
        when(managerDAO.exists(manager)).thenReturn(true);
        String actual = managerController.login(manager);

        assertEquals(expected, actual);
    }

    @Test
    public void testFailedLogin() {
        String expected = "Manager login failed";
        when(managerDAO.exists(manager)).thenReturn(false);
        String actual = managerController.login(manager);

        assertEquals(expected, actual);
    }

    @Test
    public void testSuccessfulLogout() {
        String expected = "Manager logged out successfully";
        String actual = managerController.logout(manager);

        assertEquals(expected, actual);
    }

    @Test
    public void testFailedLogout() {
        String expected = "Manager logout failed";
        manager.setLoggedIn(false);
        String actual = managerController.logout(manager);

        assertEquals(expected, actual);
    }

    @Test
    public void testSuccessfulViewProfile() {
        String expected = manager.toString();
        when(managerDAO.get(manager.getId())).thenReturn(manager);
        when(managerDAO.exists(manager)).thenReturn(true);
        String actual = managerController.viewProfile(manager.getId());

        assertEquals(expected, actual);
    }

    @Test
    public void testFailedViewProfile() {
        String expected = "Manager profile not found";
        when(managerDAO.get(manager.getId())).thenReturn(manager);
        when(managerDAO.exists(manager)).thenReturn(false);
        String actual = managerController.viewProfile(manager.getId());

        assertEquals(expected, actual);
    }

    @Test
    public void testSuccessfulEditProfile() {
        String expected = manager.toString();
        when(managerDAO.update(manager.getId(), manager)).thenReturn(manager);
        String actual = (managerController.editProfile(manager, "rakil", "secret", "Rakil Ahmed", "test")).toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testSuccessfulGet() {
        String expected = manager.toString();
        when(managerDAO.get(manager.getId())).thenReturn(manager);
        when(managerDAO.exists(manager)).thenReturn(true);
        String actual = (managerController.get(manager.getId())).toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testSuccessfulGetAll() {
        List<Manager> managers = new ArrayList<>();
        managers.add(manager);

        int expected = managers.size();
        when(managerDAO.getAll()).thenReturn(managers);
        int actual = managerController.getAll().size();

        assertEquals(expected, actual);
    }
}
