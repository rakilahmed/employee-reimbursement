package com.rakilahmed.models.reimbursement;

import java.time.LocalDate;

public class Reimbursement {
    private int id;
    private int employeeId;
    private final double amount;
    private String dateRequested = LocalDate.now().toString();
    private int managerId = 0;
    private String status = "PENDING";

    /**
     * Default constructor for Reimbursement class.
     */
    public Reimbursement() {
        this.id = 0;
        this.employeeId = 0;
        this.amount = 0;
        this.dateRequested = "";
        this.managerId = 0;
        this.status = "PENDING";
    }

    /**
     * Parameterized constructor for Reimbursement class.
     * 
     * @param employeeId The employee's id.
     * @param amount     The amount requested.
     */
    public Reimbursement(int employeeId, double amount) {
        this.employeeId = employeeId;
        this.amount = amount;
    }

    /**
     * Parameterized constructor for Reimbursement class.
     * 
     * @param id         The reimbursement's id.
     * @param employeeId The employee's id.
     * @param amount     The amount requested.
     */
    public Reimbursement(int id, int employeeId, double amount) {
        this.id = id;
        this.employeeId = employeeId;
        this.amount = amount;
    }

    /**
     * Parameterized constructor for Reimbursement class.
     * 
     * @param id         The reimbursement's id.
     * @param employeeId The employee's id.
     * @param amount     The amount requested.
     * @param managerId  The manager's id.
     */
    public Reimbursement(int id, int employeeId, double amount, int managerId) {
        this.id = id;
        this.employeeId = employeeId;
        this.amount = amount;
        this.managerId = managerId;
    }

    /**
     * Parameterized constructor for Reimbursement class.
     * 
     * @param id         The reimbursement's id.
     * @param employeeId The employee's id.
     * @param amount     The amount requested.
     * @param managerId  The manager's id.
     * @param status     The status of the reimbursement.
     */
    public Reimbursement(int id, int employeeId, double amount, int managerId, String status) {
        this.id = id;
        this.employeeId = employeeId;
        this.amount = amount;
        this.managerId = managerId;
        this.status = status;
    }

    /**
     * Getter for id.
     * 
     * @return id The reimbursement's id.
     */
    public int getId() {
        return id;
    }

    /**
     * Getter for employeeId.
     * 
     * @return employeeId The employee's id.
     */
    public int getEmployeeId() {
        return employeeId;
    }

    /**
     * Getter for amount.
     * 
     * @return amount
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Getter for dateRequested.
     * 
     * @return dateRequested
     */
    public String getDateRequested() {
        return dateRequested;
    }

    /**
     * Getter for managerId.
     * 
     * @return managerId
     */
    public int getManagerId() {
        return managerId;
    }

    /**
     * Getter for status.
     * 
     * @return status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Setter for id.
     * 
     * @param id The reimbursement's id.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Setter for employeeId.
     * 
     * @param employeeId The employee's id.
     */
    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    /**
     * Setter for managerId.
     * 
     * @param managerId The manager's id.
     */
    public void setManagerId(int managerId) {
        this.managerId = managerId;
    }

    /**
     * Setter for status.
     * 
     * @param status The status of the reimbursement.
     */
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Reimbursement [id=" + id + ", employeeId=" + employeeId + ", amount=" + amount
                + ", dateRequested=" + dateRequested + ", managerId=" + managerId + ", status=" + status + "]";
    }
}
