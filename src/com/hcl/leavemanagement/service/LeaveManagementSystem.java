
package com.hcl.leavemanagement.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import com.hcl.leavemanagement.exception.EmployeeNotFoundException;
import com.hcl.leavemanagement.exception.InsufficientLeaveException;
import com.hcl.leavemanagement.model.ContractEmployee;
import com.hcl.leavemanagement.model.Employee;
import com.hcl.leavemanagement.model.LeaveRequest;
import com.hcl.leavemanagement.model.LeaveStatus;
import com.hcl.leavemanagement.model.LeaveType;
import com.hcl.leavemanagement.model.PermanentEmployee;

public class LeaveManagementSystem {

    private final List<Employee> employees = new ArrayList<>();
    private final Set<String> employeeEmails = new HashSet<>();
    private final Map<String, Employee> employeeMap = new HashMap<>();
    private final List<LeaveRequest> leaveRequests = new ArrayList<>();

    private final Scanner scanner = new Scanner(System.in);
    private int nextRequestNumber = 1001;

    public void start() {

        boolean running = true;

        while (running) {
            displayMenu();

            try {
                int choice = readInt("Enter your choice: ");

                switch (choice) {
                    case 1:
                        addEmployee();
                        break;

                    case 2:
                        displayAllEmployees();
                        break;

                    case 3:
                        searchEmployee();
                        break;

                    case 4:
                        applyLeave();
                        break;

                    case 5:
                        approveLeave();
                        break;

                    case 6:
                        rejectLeave();
                        break;

                    case 7:
                        displayAllLeaveRequests();
                        break;

                    case 8:
                        displayLeaveBalance();
                        break;

                    case 9:
                        running = false;
                        System.out.println("Exiting system. Goodbye!");
                        break;

                    default:
                        System.out.println("Invalid choice. Try again.");
                }

            } catch (EmployeeNotFoundException | InsufficientLeaveException | IllegalArgumentException e) {
            	System.out.println("Error: " + e.getMessage());

            } catch (IllegalStateException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void displayMenu() {
        System.out.println("\n===== EMPLOYEE LEAVE MANAGEMENT =====");
        System.out.println("1. Add Employee");
        System.out.println("2. Display All Employees");
        System.out.println("3. Search Employee");
        System.out.println("4. Apply Leave");
        System.out.println("5. Approve Leave");
        System.out.println("6. Reject Leave");
        System.out.println("7. Display All Leave Requests");
        System.out.println("8. Display Leave Balance");
        System.out.println("9. Exit");
    }

    private String readText(String message) {
        System.out.print(message);
        String value = scanner.nextLine().trim();

        if (value.isEmpty()) {
            throw new IllegalArgumentException("Input cannot be empty.");
        }

        return value;
    }

    private int readInt(String message) {
        String value = readText(message);

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Please enter a valid whole number.");
        }
    }

    private void addEmployee() {

        String id = readText("Enter employee ID: ");

        if (employeeMap.containsKey(id)) {
            throw new IllegalArgumentException("Employee ID already exists.");
        }

        String name = readText("Enter employee name: ");
        String department = readText("Enter department: ");
        String email = readText("Enter email: ");
        String emailKey = email.toLowerCase(java.util.Locale.ROOT);

        if (employeeEmails.contains(emailKey)) {
            throw new IllegalArgumentException(
                    "Email already exists.");
        }

        int balance = readInt("Enter leave balance: ");

        if (balance < 0) {
            throw new IllegalArgumentException("Leave balance cannot be negative.");
        }

        System.out.println("1. Permanent Employee");
        System.out.println("2. Contract Employee");

        int type = readInt("Select employee type: ");

        Employee employee;

        if (type == 1) {
            employee = new PermanentEmployee(id, name, department, email, balance);
        } else if (type == 2) {
            employee = new ContractEmployee(id, name, department, email, balance);
        } else {
            throw new IllegalArgumentException("Invalid employee type.");
        }

        employees.add(employee);
        employeeMap.put(id, employee);
        employeeEmails.add(emailKey);

        System.out.println("Employee added successfully.");
    }

    private void displayAllEmployees() {

        if (employees.isEmpty()) {
            System.out.println("No employees found.");
            return;
        }

        Iterator<Employee> iterator = employees.iterator();

        while (iterator.hasNext()) {
            Employee employee = iterator.next();
            employee.displayDetails();
            System.out.println("----------------------------");
        }
    }

    private Employee findEmployee(String employeeId) {

        Employee employee = employeeMap.get(employeeId);

        if (employee == null) {
            throw new EmployeeNotFoundException(employeeId);
        }

        return employee;
    }

    private void searchEmployee() {

        String id = readText("Enter employee ID to search: ");

        Employee employee = findEmployee(id);
        employee.displayDetails();
    }

    private void applyLeave() {

        String employeeId = readText("Enter employee ID: ");
        Employee employee = findEmployee(employeeId);

        System.out.println("Leave Types: CASUAL, SICK, EARNED");

        String typeText = readText("Enter leave type: ");

        LeaveType leaveType;

        try {
            leaveType = LeaveType.valueOf(typeText.toUpperCase(java.util.Locale.ROOT));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid leave type. Use CASUAL, SICK, or EARNED.");
        }

        int days = readInt("Enter number of leave days: ");

        if (days <= 0) {
            throw new IllegalArgumentException("Leave days must be greater than zero.");
        }

        if (days > employee.getAvailableLeaveBalance()) {
            throw new InsufficientLeaveException(employee.getAvailableLeaveBalance(), days);
        }

        String reason = readText("Enter leave reason: ");

        String requestId = "LR" + nextRequestNumber++;

        LeaveRequest request = new LeaveRequest(requestId, employeeId, leaveType, days, reason);

        leaveRequests.add(request);

        System.out.println("Leave applied successfully.");
        System.out.println("Request ID: " + requestId);
        System.out.println("Status: " + request.getStatus());
        System.out.println("Balance will be deducted only after approval.");
    }

    private LeaveRequest findLeaveRequest(String requestId) {

        for (LeaveRequest request : leaveRequests) {
            if (request.getRequestId().equalsIgnoreCase(requestId)) {
                return request;
            }
        }

        throw new IllegalArgumentException("Leave request " + requestId + " does not exist.");
    }

    private void approveLeave() {

        String requestId = readText("Enter leave request ID to approve: ");

        LeaveRequest request = findLeaveRequest(requestId);

        if (request.getStatus() != LeaveStatus.PENDING) {
            throw new IllegalStateException("Only pending requests can be approved.");
        }

        Employee employee = findEmployee(request.getEmployeeId());

        int days = request.getNumberOfDays();
        int oldBalance = employee.getAvailableLeaveBalance();

        employee.deductLeave(days);
        request.setStatus(LeaveStatus.APPROVED);

        System.out.println("Leave approved successfully.");
        System.out.println("Previous balance: " + oldBalance);
        System.out.println("New balance: "+ employee.getAvailableLeaveBalance());
    }

    private void rejectLeave() {

        String requestId = readText("Enter leave request ID to reject: ");

        LeaveRequest request = findLeaveRequest(requestId);

        if (request.getStatus() != LeaveStatus.PENDING) {
            throw new IllegalStateException("Only pending requests can be rejected.");
        }

        request.setStatus(LeaveStatus.REJECTED);

        System.out.println("Leave rejected successfully.");
        System.out.println("Leave balance remains unchanged.");
    }

    private void displayAllLeaveRequests() {

        if (leaveRequests.isEmpty()) {
            System.out.println("No leave requests found.");
            return;
        }

        for (LeaveRequest request : leaveRequests) {
            request.displayDetails();
        }
    }

    private void displayLeaveBalance() {

        String id = readText("Enter employee ID: ");
        Employee employee = findEmployee(id);

        System.out.println("Employee: "+ employee.getEmployeeName());
        System.out.println("Available leave balance: "+ employee.getAvailableLeaveBalance());
    }

    public void close() {
        scanner.close();
    }
}
