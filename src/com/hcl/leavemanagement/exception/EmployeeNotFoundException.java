
package com.hcl.leavemanagement.exception;

public class EmployeeNotFoundException extends RuntimeException {

    public EmployeeNotFoundException(String employeeId) {
        super("Employee " + employeeId + " does not exist.");
    }
}
