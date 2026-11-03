package com.hcl.leavemanagement.model;

public class ContractEmployee extends Employee {

    public ContractEmployee(String employeeId, String employeeName, String department, String email, int availableLeaveBalance) {

        super(employeeId, employeeName, department, email, availableLeaveBalance, "Contract");
    }

    @Override
    public String getEmployeeType() {
        return "Contract";
    }

    @Override
    public void displayDetails() {
        super.displayDetails(true);
    }
}
