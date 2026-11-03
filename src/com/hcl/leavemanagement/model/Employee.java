package com.hcl.leavemanagement.model;

import com.hcl.leavemanagement.exception.InsufficientLeaveException;

public class Employee {

    private String employeeId;
    private String employeeName;
    private String department;
    private String email;
    private int availableLeaveBalance;

    protected final String employmentCategory;

    public Employee(String employeeId, String employeeName, String department, String email, int availableLeaveBalance) {
        this(employeeId, employeeName, department, email, availableLeaveBalance, "General");
    }

    public Employee(String employeeId, String employeeName, String department, String email,int availableLeaveBalance, String employmentCategory) {

        if (availableLeaveBalance < 0) {
            throw new IllegalArgumentException("Leave balance cannot be negative.");
        }

        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.department = department;
        this.email = email;
        this.availableLeaveBalance = availableLeaveBalance;
        this.employmentCategory = employmentCategory;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAvailableLeaveBalance() {
        return availableLeaveBalance;
    }

    public String getEmployeeType() {
        return employmentCategory;
    }

    public void deductLeave(int days) {
        if (days <= 0) {
            throw new IllegalArgumentException("Leave days must be greater than zero.");
        }

        if (days > availableLeaveBalance) {
            throw new InsufficientLeaveException(availableLeaveBalance, days);
        }

        availableLeaveBalance -= days;
    }

    public void displayDetails() {
        displayDetails(false);
    }

    public void displayDetails(boolean showType) {
    	System.out.println("========HCL Employee Portal=========");
        System.out.println("Employee ID: " + employeeId);
        System.out.println("Name: " + employeeName);
        System.out.println("Department Name: " + department);
        System.out.println("Email: " + email);
        System.out.println("Leave Balance: "+ availableLeaveBalance);

        if (showType) {
            System.out.println("Employment Type: "+ getEmployeeType());
        }
    }
}
