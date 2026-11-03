package com.hcl.leavemanagement.model;

public class PermanentEmployee extends Employee {

    public PermanentEmployee(String employeeId, String employeeName, String department, String email, int availableLeaveBalance) {

        super(employeeId, employeeName, department, email, availableLeaveBalance, "Permanent");
    }

    @Override
    public String getEmployeeType() {
        return "Permanent";
    }

    @Override
    public void displayDetails() {
        super.displayDetails(true);
    }
}
