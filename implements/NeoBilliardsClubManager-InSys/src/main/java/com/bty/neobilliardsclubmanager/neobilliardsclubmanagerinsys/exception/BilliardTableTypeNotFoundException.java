package com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.exception;

// Ngoai le the hien viec khong tim that loai ban
public class BilliardTableTypeNotFoundException extends RuntimeException {
    public BilliardTableTypeNotFoundException(String message) {
        super(message);
    }
}
