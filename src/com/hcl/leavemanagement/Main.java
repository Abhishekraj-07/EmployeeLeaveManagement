package com.hcl.leavemanagement;

import com.hcl.leavemanagement.service.LeaveManagementSystem;

public class Main {

    public static void main(String[] args) {

        LeaveManagementSystem system = new LeaveManagementSystem();
        try {
            system.start();
        } finally {
            system.close();
        }
    }
}
