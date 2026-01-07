package com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.exception;

// Ngoai le the hien viec khong tim thay hoi vien
public class MemberNotFoundException extends RuntimeException {
    public MemberNotFoundException(String message) {
        super(message);
    }
}
