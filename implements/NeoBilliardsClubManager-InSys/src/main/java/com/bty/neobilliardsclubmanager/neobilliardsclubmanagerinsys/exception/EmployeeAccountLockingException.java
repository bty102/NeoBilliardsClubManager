package com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.exception;

// Ngoai le the hien viec khoa tai khoan nhan vien that bai
public class EmployeeAccountLockingException extends RuntimeException {
    public EmployeeAccountLockingException(String message) {
        super(message);
    }
}
