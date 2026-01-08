package com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.exception;

// Ngoai le the hien viec khong tim thay chi tiet hoa don
public class BillDetailNotFoundException extends RuntimeException {

    public BillDetailNotFoundException(String message) {
        super(message);
    }
}
