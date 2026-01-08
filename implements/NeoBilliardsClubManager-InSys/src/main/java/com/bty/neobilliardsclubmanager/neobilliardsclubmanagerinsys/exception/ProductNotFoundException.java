package com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.exception;

// Ngoai le the hien viec khong tim thay product
public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String message) {
        super(message);
    }
}
