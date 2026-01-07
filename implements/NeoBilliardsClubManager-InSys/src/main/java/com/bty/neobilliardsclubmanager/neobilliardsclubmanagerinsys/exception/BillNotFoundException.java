package com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.exception;

// Ngoai le the hien viec khong tim thay hoa don
public class BillNotFoundException extends RuntimeException {
    public BillNotFoundException(String message) {
        super(message);
    }
}
