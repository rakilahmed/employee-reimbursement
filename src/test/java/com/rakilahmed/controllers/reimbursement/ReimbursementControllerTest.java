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
    public void testReimbursementAlreadyExists() {
        String expected = "Reimbursement already exists";
        when(reimbursementDAOMock.exists(reimbursement)).thenReturn(true);
        String actual = reimbursementController.create(reimbursement);

        assertEquals(expected, actual);
    }

    @Test
    public void testFailedCreate() {
        String expected = "Reimbursement creation failed";
        when(reimbursementDAOMock.insert(reimbursement)).thenReturn(0);
        String actual = reimbursementController.create(reimbursement);

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
    public void testSuccessfulGetAll() {
        List<Reimbursement> reimbursements = new ArrayList<>();
        reimbursements.add(reimbursement);

        int expected = reimbursements.size();
        when(reimbursementDAOMock.getAll()).thenReturn(reimbursements);
        int actual = reimbursementController.getAll().size();

        assertEquals(expected, actual);
    }
}
