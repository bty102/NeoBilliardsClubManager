package com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.service;

import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.dto.request.BillCreationRequest;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.entity.Account;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.entity.Bill;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.entity.BilliardTable;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.exception.BillCreationException;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.exception.BillUpdateException;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.repository.AccountRepository;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.repository.BillRepository;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.repository.BilliardTableRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BillService {

    final BillRepository billRepository;
    final BilliardTableRepository billiardTableRepository;
    final AccountRepository accountRepository;

    public void createBill(BillCreationRequest request) {
        BilliardTable billiardTable = billiardTableRepository.findByTableNumber(request.getBilliardTableNumber())
                .orElseThrow(() -> {throw new BillCreationException("Không thể tạo hóa đơn");
                });

        if(billiardTable.getIsLocked()) {
            throw new BillCreationException("Không thể tạo hóa đơn");
        }

        if(billiardTable.getIsOpening()) {
            throw new BillCreationException("Không thể tạo hóa đơn");
        }

        Account account = accountRepository.findById(request.getCreatedByAccountId())
                .orElseThrow(() -> {throw new BillCreationException("Không thể tạo hóa đơn");});

        Bill bill = Bill.builder()
                .checkInTime(LocalDateTime.now())
                .checkOutTime(null)
                .totalAmount(null)
                .createdAt(LocalDateTime.now())
                .billiardTable(billiardTable)
                .account(account)
                .member(null)
                .paid(false)
                .build();
        billRepository.save(bill);
    }

    public void updateCheckOutTime(Long billId, LocalDateTime checkOutTime) {
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> {throw new BillUpdateException("Cập nhật thất bại");
                });
        if(bill.getCheckOutTime() != null) {
            throw new BillUpdateException("Cập nhật thất bại");
        }
        bill.setCheckOutTime(checkOutTime);
        billRepository.save(bill);
    }
}
