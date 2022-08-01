package com.rakilahmed.models.reimbursement;

import java.time.LocalDate;

public class Reimbursement {
    private int id;
    private int employeeId;
    private double amountRequested;
    private String dateRequested = LocalDate.now().toString();
    private int managerId = 0;
    private String status = "PENDING";

    /**
     * Default constructor for Reimbursement class.
     */
    public Reimbursement() {
        this.id = 0;
        this.employeeId = 0;
        this.amountRequested = 0;
        this.dateRequested = "";
        this.managerId = 0;
        this.status = "PENDING";
    }

    /**
     * Parameterized constructor for Reimbursement class.
     * 
     * @param employeeId      The employee's id.
     * @param amountRequested The amount requested.
     */
    public Reimbursement(int employeeId, double amountRequested) {
        this.employeeId = employeeId;
        this.amountRequested = amountRequested;
    }

    /**
     * Parameterized constructor for Reimbursement class.
     * 
     * @param id              The reimbursement's id.
     * @param employeeId      The employee's id.
     * @param amountRequested The amount requested.
     */
    public Reimbursement(int id, int employeeId, double amountRequested) {
        this.id = id;
        this.employeeId = employeeId;
        this.amountRequested = amountRequested;
    }

    /**
     * Parameterized constructor for Reimbursement class.
     * 
     * @param id              The reimbursement's id.
     * @param employeeId      The employee's id.
     * @param amountRequested The amount requested.
     * @param managerId       The manager's id.
     */
    public Reimbursement(int id, int employeeId, double amountRequested, int managerId) {
        this.id = id;
        this.employeeId = employeeId;
        this.amountRequested = amountRequested;
        this.managerId = managerId;
    }

    /**
     * Parameterized constructor for Reimbursement class.
     * 
     * @param id              The reimbursement's id.
     * @param employeeId      The employee's id.
     * @param amountRequested The amount requested.
     * @param managerId       The manager's id.
     * @param status          The status of the reimbursement.
     */
    public Reimbursement(int id, int employeeId, double amountRequested, int managerId, String status) {
        this.id = id;
        this.employeeId = employeeId;
        this.amountRequested = amountRequested;
        this.managerId = managerId;
        this.status = status;
    }

    /**
     * Getter for id.
     * 
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Getter for employeeId.
     * 
     * @return employeeId
     */
    public int getEmployeeId() {
        return employeeId;
    }

    /**
     * Getter for amountRequested.
     * 
     * @return amountRequested
     */
    public double getAmountRequested() {
        return amountRequested;
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
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Setter for managerId.
     * 
     * @param managerId
     */
    public void setManagerId(int managerId) {
        this.managerId = managerId;
    }

    /**
     * Setter for status.
     * 
     * @param status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Reimbursement [id=" + id + ", employeeId=" + employeeId + ", amountRequested=" + amountRequested
                + ", dateRequested=" + dateRequested + ", managerId=" + managerId + ", status=" + status + "]";
    }
}
