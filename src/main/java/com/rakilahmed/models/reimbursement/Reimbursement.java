package com.rakilahmed.models.reimbursement;

public class Reimbursement {
    private int reimbursementId;
    private double amountRequested;
    private String dateRequested;
    private int employeeId;
    private int managerId;
    private String status;

    /**
     * Parameterized constructor for Reimbursement class.
     * 
     * @param reimbursementId
     * @param amountRequested
     * @param dateRequested
     * @param employeeId
     * @param managerId
     * @param status
     */
    public Reimbursement(int reimbursementId, double amountRequested, String dateRequested, int employeeId,
            int managerId,
            String status) {
        this.reimbursementId = reimbursementId;
        this.amountRequested = amountRequested;
        this.dateRequested = dateRequested;
        this.employeeId = employeeId;
        this.managerId = managerId;
        this.status = status;
    }

    /**
     * Getter for reimbursementId.
     * 
     * @return reimbursementId
     */
    public int getReimbursementId() {
        return reimbursementId;
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
     * Getter for employeeId.
     * 
     * @return employeeId
     */
    public int getEmployeeId() {
        return employeeId;
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
}
