package com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.exception;

// Ngoai le the hien viec khong tim thay ho so nhan vien
public class EmployeeProfileNotFoundException extends RuntimeException {
    public EmployeeProfileNotFoundException(String message) {
        super(message);
    }
}
