package com.rakilahmed.controllers.reimbursement;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.rakilahmed.models.reimbursement.Reimbursement;
import com.rakilahmed.services.reimbursement.ReimbursementDAO;

public class ReimbursementControllerTest {
    private ReimbursementDAO reimbursementDAOMock;
    private ReimbursementController reimbursementController;
    private Reimbursement reimbursement;

    @Before
    public void setUp() {
        reimbursementDAOMock = mock(ReimbursementDAO.class);
        reimbursementController = new ReimbursementController(reimbursementDAOMock);

        reimbursement = new Reimbursement(1, 10, 100, 15);
    }

    @Test
    public void testSuccessfulCreate() {
        String expected = "Reimbursement created successfully. Reimbursement ID: 1";
        when(reimbursementDAOMock.insert(reimbursement)).thenReturn(1);
        String actual = reimbursementController.create(reimbursement);

        assertEquals(expected, actual);
    }

    @Test
    public void testFailedCreate() {
        String expectedAlreadyExists = "Reimbursement already exists";
        when(reimbursementDAOMock.exists(reimbursement)).thenReturn(true);
        assertEquals(expectedAlreadyExists, reimbursementController.create(reimbursement));

        String expectedCreationFailed = "Reimbursement creation failed";
        when(reimbursementDAOMock.exists(reimbursement)).thenReturn(false);
        when(reimbursementDAOMock.insert(reimbursement)).thenReturn(0);
        String actual = reimbursementController.create(reimbursement);

        assertEquals(expectedCreationFailed, actual);
    }

    @Test
    public void testSuccessfulUpdate() {
        String expected = "Reimbursement updated successfully. Reimbursement ID: 1";
        when(reimbursementDAOMock.get(reimbursement.getId())).thenReturn(reimbursement);
        when(reimbursementDAOMock.exists(reimbursement)).thenReturn(true);
        when(reimbursementDAOMock.update(reimbursement.getId(), reimbursement)).thenReturn(true);
        String actual = reimbursementController.update(reimbursement.getId(), reimbursement.getManagerId(), "APPROVED");

        assertEquals(expected, actual);
    }

    @Test
    public void testFailedUpdate() {
        String expectedInvalidId = "Reimbursement id is invalid. Reimbursement ID: -1";
        assertEquals(expectedInvalidId, reimbursementController.update(-1, reimbursement.getManagerId(), "APPROVED"));

        String expectedManagerIdIsInvalid = "Manager id is invalid. Manager ID: -1";
        assertEquals(expectedManagerIdIsInvalid, reimbursementController.update(reimbursement.getId(), -1, "APPROVED"));

        String expectedStatusIsInvalid = "Status is invalid. Status: INVALID";
        assertEquals(expectedStatusIsInvalid,
                reimbursementController.update(reimbursement.getId(), reimbursement.getManagerId(), "INVALID"));

        String expectedReimbursementDoesNotExist = "Reimbursement does not exist";
        when(reimbursementDAOMock.get(reimbursement.getId())).thenReturn(null);
        assertEquals(expectedReimbursementDoesNotExist,
                reimbursementController.update(reimbursement.getId(), reimbursement.getManagerId(), "APPROVED"));

        String expected = "Reimbursement update failed";
        when(reimbursementDAOMock.get(reimbursement.getId())).thenReturn(reimbursement);
        when(reimbursementDAOMock.exists(reimbursement)).thenReturn(true);
        when(reimbursementDAOMock.update(reimbursement.getId(), reimbursement)).thenReturn(false);
        String actual = reimbursementController.update(reimbursement.getId(), reimbursement.getManagerId(), "APPROVED");

        assertEquals(expected, actual);
    }

    @Test
    public void testSuccessfulGet() {
        String expected = reimbursement.toString();
        when(reimbursementDAOMock.get(1)).thenReturn(reimbursement);
        when(reimbursementDAOMock.exists(reimbursement)).thenReturn(true);
        String actual = reimbursementController.get(1).toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testFailedGet() {
        assertEquals(null, reimbursementController.get(0));

        when(reimbursementDAOMock.get(reimbursement.getId())).thenReturn(null);
        assertEquals(null, reimbursementController.get(reimbursement.getId()));
    }

    @Test
    public void testSuccessfulGetAll() {
        List<Reimbursement> reimbursements = new ArrayList<>();
        reimbursements.add(reimbursement);

        int expected = reimbursements.size();
        when(reimbursementDAOMock.getAll()).thenReturn(reimbursements);
        int actual = reimbursementController.getAll().size();

        assertEquals(expected, actual);
    }

    @Test
    public void testFailedGetAll() {
        List<Reimbursement> reimbursements = new ArrayList<>();
        when(reimbursementDAOMock.getAll()).thenReturn(reimbursements);

        assertEquals(null, reimbursementController.getAll());
    }

    @Test
    public void testSuccessfulGetAllPending() {
        List<Reimbursement> reimbursements = new ArrayList<>();
        reimbursements.add(reimbursement);

        int expected = reimbursements.size();
        when(reimbursementDAOMock.getAllPending()).thenReturn(reimbursements);
        int actual = reimbursementController.getAllPending().size();

        assertEquals(expected, actual);
        assertEquals(reimbursement.getStatus(), reimbursements.get(0).getStatus());
    }

    @Test
    public void testFailedGetAllPending() {
        List<Reimbursement> reimbursements = new ArrayList<>();
        when(reimbursementDAOMock.getAllPending()).thenReturn(reimbursements);

        assertEquals(null, reimbursementController.getAllPending());
    }

    @Test
    public void testSuccessfulGetAllResolved() {
        List<Reimbursement> reimbursements = new ArrayList<>();

        reimbursement.setManagerId(1);
        reimbursement.setStatus("APPROVED");

        when(reimbursementDAOMock.get(reimbursement.getId())).thenReturn(reimbursement);
        when(reimbursementDAOMock.exists(reimbursement)).thenReturn(true);
        when(reimbursementDAOMock.update(reimbursement.getId(), reimbursement)).thenReturn(true);
        reimbursementController.update(1, 1, "APPROVED");
        reimbursements.add(reimbursement);

        int expected = reimbursements.size();
        when(reimbursementDAOMock.getAllResolved()).thenReturn(reimbursements);
        int actual = reimbursementController.getAllResolved().size();

        assertEquals(expected, actual);
        assertEquals(reimbursement.getStatus(), reimbursements.get(0).getStatus());
    }

    @Test
    public void testFailedGetAllResolved() {
        List<Reimbursement> reimbursements = new ArrayList<>();
        when(reimbursementDAOMock.getAllResolved()).thenReturn(reimbursements);

        assertEquals(null, reimbursementController.getAllResolved());
    }

    @Test
    public void testSuccessfulGetAllForEmployee() {
        List<Reimbursement> reimbursements = new ArrayList<>();
        reimbursements.add(reimbursement);

        int expected = reimbursements.size();
        when(reimbursementDAOMock.getAllForEmployee(1)).thenReturn(reimbursements);
        int actual = reimbursementController.getAllForEmployee(1).size();

        assertEquals(expected, actual);
    }

    @Test
    public void testFailedGetAllForEmployee() {
        List<Reimbursement> reimbursements = new ArrayList<>();
        when(reimbursementDAOMock.getAllForEmployee(1)).thenReturn(reimbursements);

        assertEquals(null, reimbursementController.getAllForEmployee(1));
    }

    @Test
    public void testSuccessfulGetAllPendingForEmployee() {
        List<Reimbursement> reimbursements = new ArrayList<>();
        reimbursements.add(reimbursement);

        int expected = reimbursements.size();
        when(reimbursementDAOMock.getAllPendingForEmployee(1)).thenReturn(reimbursements);
        int actual = reimbursementController.getAllPendingForEmployee(1).size();

        assertEquals(expected, actual);
        assertEquals(reimbursement.getStatus(), reimbursements.get(0).getStatus());
    }

    @Test
    public void testFailedGetAllPendingForEmployee() {
        List<Reimbursement> reimbursements = new ArrayList<>();
        when(reimbursementDAOMock.getAllPendingForEmployee(1)).thenReturn(reimbursements);

        assertEquals(null, reimbursementController.getAllPendingForEmployee(1));
    }

    @Test
    public void testSuccessfulGetAllResolvedForEmployee() {
        List<Reimbursement> reimbursements = new ArrayList<>();
        reimbursement.setManagerId(1);
        reimbursement.setStatus("APPROVED");

        when(reimbursementDAOMock.get(reimbursement.getId())).thenReturn(reimbursement);
        when(reimbursementDAOMock.exists(reimbursement)).thenReturn(true);
        when(reimbursementDAOMock.update(reimbursement.getId(), reimbursement)).thenReturn(true);
        reimbursementController.update(1, 1, "APPROVED");
        reimbursements.add(reimbursement);

        int expected = reimbursements.size();
        when(reimbursementDAOMock.getAllResolvedForEmployee(1)).thenReturn(reimbursements);
        int actual = reimbursementController.getAllResolvedForEmployee(1).size();

        assertEquals(expected, actual);
        assertEquals(reimbursement.getStatus(), reimbursements.get(0).getStatus());
    }

    @Test
    public void testFailedGetAllResolvedForEmployee() {
        List<Reimbursement> reimbursements = new ArrayList<>();
        when(reimbursementDAOMock.getAllResolvedForEmployee(1)).thenReturn(reimbursements);

        assertEquals(null, reimbursementController.getAllResolvedForEmployee(1));
    }
}
