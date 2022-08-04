package com.rakilahmed.services.reimbursement;

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

import com.rakilahmed.models.reimbursement.Reimbursement;
import com.rakilahmed.utils.ConnectionManager;

public class ReimbursementDAOTest {
    private ConnectionManager connectionManagerMock;
    private Connection connectionMock;
    private PreparedStatement statementMock;
    private ResultSet resultSetMock;
    private ReimbursementDAO reimbursementDAO;
    private Reimbursement testReimbursement;

    @Before
    public void setUp() throws SQLException {
        connectionManagerMock = mock(ConnectionManager.class);
        statementMock = mock(PreparedStatement.class);
        resultSetMock = mock(ResultSet.class);
        connectionMock = mock(Connection.class);
        reimbursementDAO = new ReimbursementDAO(connectionManagerMock);
        testReimbursement = new Reimbursement(1, 1, 1.0, 1, "PENDING");

        when(connectionMock.prepareStatement(anyString())).thenReturn(statementMock);
        when(connectionMock.prepareStatement(anyString(), anyInt())).thenReturn(statementMock);
        when(connectionManagerMock.getConnection()).thenReturn(connectionMock);

        when(statementMock.executeQuery()).thenReturn(resultSetMock);
        when(resultSetMock.next()).thenReturn(true).thenReturn(false);
        when(resultSetMock.getInt("reimbursement_id")).thenReturn(1);
        when(resultSetMock.getInt("user_id")).thenReturn(testReimbursement.getEmployeeId());
        when(resultSetMock.getDouble("amount")).thenReturn(testReimbursement.getAmount());
        when(resultSetMock.getInt("manager_id")).thenReturn(testReimbursement.getManagerId());
        when(resultSetMock.getString("status")).thenReturn(testReimbursement.getStatus());
    }

    @Test
    public void testConnectionNotNull() {
        assertNotNull(connectionMock);
    }

    @Test
    public void testSucessfulInsert() throws SQLException {
        int expected = testReimbursement.getId();
        Reimbursement reimbursement = new Reimbursement(1, 1, 1.0, 1, "PENDING");

        assertEquals(expected, reimbursementDAO.insert(reimbursement));
    }

    @Test
    public void testFailedInsert() throws SQLException {
        Reimbursement reimbursement = new Reimbursement(1, 1, 1.0, 1, "PENDING");
        when(statementMock.executeQuery()).thenThrow(new SQLException());
        assertEquals(-1, reimbursementDAO.insert(reimbursement));
    }

    @Test
    public void testSuccessfulExists() throws SQLException {
        boolean expected = true;
        Reimbursement reimbursement = new Reimbursement(1, 1, 1.0, 1, "PENDING");

        if (testReimbursement.toString().equals(reimbursement.toString())) {
            when(resultSetMock.next()).thenReturn(true);
        } else {
            when(resultSetMock.next()).thenReturn(false);
        }

        boolean actual = reimbursementDAO.exists(reimbursement);
        assertEquals(expected, actual);
    }

    @Test
    public void testFailedExists() throws SQLException {
        assertEquals(false, reimbursementDAO.exists(null));

        Reimbursement reimbursement = new Reimbursement(1, 1, 1.0, 1, "PENDING");
        when(resultSetMock.next()).thenThrow(new SQLException());
        assertEquals(false, reimbursementDAO.exists(reimbursement));
    }

    @Test
    public void testSuccessfulUpdate() throws SQLException {
        Reimbursement reimbursement = new Reimbursement(1, 1, 1.0, 1, "APPROVED");
        when(resultSetMock.getInt("reimbursement_id")).thenReturn(reimbursement.getId());
        when(resultSetMock.getInt("user_id")).thenReturn(reimbursement.getEmployeeId());
        when(resultSetMock.getDouble("amount")).thenReturn(reimbursement.getAmount());
        when(resultSetMock.getInt("manager_id")).thenReturn(reimbursement.getManagerId());
        when(resultSetMock.getString("status")).thenReturn(reimbursement.getStatus());
        assertEquals(true, reimbursementDAO.update(reimbursement.getId(), reimbursement));

        Reimbursement updatedReimbursement = reimbursementDAO.get(reimbursement.getId());
        assertEquals(reimbursement.toString(), updatedReimbursement.toString());
    }

    @Test
    public void testFailedUpdate() throws SQLException {
        assertEquals(false, reimbursementDAO.update(1, null));

        Reimbursement reimbursement = new Reimbursement(1, 1, 1.0, 1, "APPROVED");
        when(statementMock.executeUpdate()).thenThrow(new SQLException());
        assertEquals(false, reimbursementDAO.update(reimbursement.getId(), reimbursement));
    }

    @Test
    public void testSuccessfulGet() throws SQLException {
        Reimbursement expected = new Reimbursement(1, 1, 1.0, 1, "PENDING");
        Reimbursement actual = reimbursementDAO.get(1);

        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testFailedGet() throws SQLException {
        when(resultSetMock.next()).thenThrow(new SQLException());
        assertEquals(null, reimbursementDAO.get(1));
    }

    @Test
    public void testSuccessfulGetAll() {
        List<Reimbursement> reimbursements = reimbursementDAO.getAll();
        Reimbursement reimbursement = reimbursements.get(0);

        assertEquals(1, reimbursements.size());
        assertEquals(testReimbursement.toString(), reimbursement.toString());
    }

    @Test
    public void testFailedGetAll() throws SQLException {
        when(resultSetMock.next()).thenThrow(new SQLException());
        assertEquals(new ArrayList<>(), reimbursementDAO.getAll());
    }

    @Test
    public void testSuccessfulGetAllPending() {
        List<Reimbursement> reimbursements = reimbursementDAO.getAllPending();
        Reimbursement reimbursement = reimbursements.get(0);

        assertEquals(1, reimbursements.size());
        assertEquals("PENDING", reimbursement.getStatus());
    }

    @Test
    public void testFailedGetAllPending() throws SQLException {
        when(resultSetMock.next()).thenThrow(new SQLException());
        assertEquals(new ArrayList<>(), reimbursementDAO.getAllPending());
    }

    @Test
    public void testSuccessfulGetAllResolved() throws SQLException {
        when(resultSetMock.getString("status")).thenReturn("APPROVED");
        List<Reimbursement> reimbursements = reimbursementDAO.getAllResolved();
        Reimbursement reimbursement = reimbursements.get(0);

        assertEquals(1, reimbursements.size());
        assertEquals("APPROVED", reimbursement.getStatus());
    }

    @Test
    public void testFailedGetAllResolved() throws SQLException {
        when(resultSetMock.next()).thenThrow(new SQLException());
        assertEquals(new ArrayList<>(), reimbursementDAO.getAllResolved());
    }

    @Test
    public void testSuccessfulGetAllForEmployee() throws SQLException {
        List<Reimbursement> reimbursements = reimbursementDAO.getAllForEmployee(1);
        Reimbursement reimbursement = reimbursements.get(0);

        assertEquals(1, reimbursements.size());
        assertEquals(testReimbursement.toString(), reimbursement.toString());
    }

    @Test
    public void testFailedGetAllForEmployee() throws SQLException {
        when(resultSetMock.next()).thenThrow(new SQLException());
        assertEquals(new ArrayList<>(), reimbursementDAO.getAllForEmployee(1));
    }

    @Test
    public void testSuccessfulGetAllPendingForEmployee() {
        List<Reimbursement> reimbursements = reimbursementDAO.getAllPendingForEmployee(1);
        Reimbursement reimbursement = reimbursements.get(0);

        assertEquals(1, reimbursements.size());
        assertEquals("PENDING", reimbursement.getStatus());
    }

    @Test
    public void testFailedGetAllPendingForEmployee() throws SQLException {
        when(resultSetMock.next()).thenThrow(new SQLException());
        assertEquals(new ArrayList<>(), reimbursementDAO.getAllPendingForEmployee(1));
    }

    @Test
    public void testSuccessfulGetAllResolvedForEmployee() throws SQLException {
        when(resultSetMock.getString("status")).thenReturn("APPROVED");
        List<Reimbursement> reimbursements = reimbursementDAO.getAllResolvedForEmployee(1);
        Reimbursement reimbursement = reimbursements.get(0);

        assertEquals(1, reimbursements.size());
        assertEquals("APPROVED", reimbursement.getStatus());
    }

    @Test
    public void testFailedGetAllResolvedForEmployee() throws SQLException {
        when(resultSetMock.next()).thenThrow(new SQLException());
        assertEquals(new ArrayList<>(), reimbursementDAO.getAllResolvedForEmployee(1));
    }
}
