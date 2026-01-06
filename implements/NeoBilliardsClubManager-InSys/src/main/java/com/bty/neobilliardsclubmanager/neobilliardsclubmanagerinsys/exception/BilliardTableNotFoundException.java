package com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.exception;

// Ngoai le the hien viec khong tim thay ban bida
public class BilliardTableNotFoundException extends RuntimeException {
    public BilliardTableNotFoundException(String message) {
        super(message);
    }
}
