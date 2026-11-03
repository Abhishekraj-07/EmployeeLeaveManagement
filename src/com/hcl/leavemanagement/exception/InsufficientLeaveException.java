
package com.hcl.leavemanagement.exception;

public class InsufficientLeaveException extends RuntimeException {

    public InsufficientLeaveException(int available, int requested) {
        super("Insufficient leave balance. Available: " + available + ", Requested: " + requested);
    }
}
