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
}
