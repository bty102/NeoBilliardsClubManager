package com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.service;

import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.dto.request.BillCreationRequest;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.entity.Account;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.entity.Bill;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.entity.BillDetail;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.entity.BilliardTable;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.exception.BillCreationException;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.exception.BillUpdateException;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.repository.AccountRepository;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.repository.BillRepository;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.repository.BilliardTableRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    @PreAuthorize("hasRole(T(com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.constant.Role).ADMIN.name()) or @billService.isOwnerOfBill(#billId, authentication.principal.id)")
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void updateCheckOutTime(Long billId, LocalDateTime checkOutTime) {
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> {throw new BillUpdateException("Cập nhật thất bại");
                });
        if(bill.getCheckOutTime() != null) {
            throw new BillUpdateException("Cập nhật thất bại");
        }
        bill.setCheckOutTime(checkOutTime);
        billRepository.save(bill);

        calculateAndUpdateTotalAmount(billId);
    }

    public boolean isOwnerOfBill(Long billId, Long accountId) {
        Optional<Bill> bill = billRepository.findById(billId);
        if(!bill.isPresent()) {
            return false;
        }
        return bill.get().getAccount().getId() == accountId;
    }

    @PreAuthorize("hasRole(T(com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.constant.Role).ADMIN.name()) or @billService.isOwnerOfBill(#billId, authentication.principal.id)")
    public void calculateAndUpdateTotalAmount(Long billId) {
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> {throw new BillUpdateException("Cập nhật thất bại");
                });

        if(bill.getCheckOutTime() == null) {
            throw new BillUpdateException("Cập nhật thất bại");
        }

        Duration duration = Duration.between(bill.getCheckInTime(), bill.getCheckOutTime());
        double hoursPlayed = duration.toMinutes()/60.0;// So gio da choi

        Long totalAmount = (long) (hoursPlayed * (bill.getBilliardTable().getBilliardTableType().getPricePerHour())); // Tong tien cuoi cung cua hoa don (VND)
        List<BillDetail> billDetails = bill.getBillDetails();
        for(BillDetail billDetail : billDetails) {
            totalAmount += billDetail.getProduct().getPrice() * billDetail.getQuantity();
        }
        long billTotalDiscountPercentage = 0;// Phan tram giam gia tren tong hoa don
        if(bill.getMember() != null) {
            billTotalDiscountPercentage = bill.getMember().getMemberLevel().getBillTotalDiscountPercentage();
        }
        totalAmount = totalAmount - (long)(totalAmount * (billTotalDiscountPercentage/100.0));

        bill.setTotalAmount(totalAmount);
        billRepository.save(bill);
    }
}
