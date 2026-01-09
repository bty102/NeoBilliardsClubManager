package com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.service;

import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.dto.request.BillCreationRequest;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.dto.response.BillResponse;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.entity.*;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.exception.*;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.mapper.BillMapper;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.repository.AccountRepository;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.repository.BillRepository;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.repository.BilliardTableRepository;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.repository.MemberRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    final BillMapper billMapper;
    final MemberRepository memberRepository;
    final MemberService memberService;

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
        if(bill.getMember() != null && bill.getMember().getMemberLevel() != null) {
            billTotalDiscountPercentage = bill.getMember().getMemberLevel().getBillTotalDiscountPercentage();
        }
        totalAmount = totalAmount - (long)(totalAmount * (billTotalDiscountPercentage/100.0));

        bill.setTotalAmount(totalAmount);
        billRepository.save(bill);
    }

    // Tham so:
    //      - pageNumber >= 1
    //      - pageSize >= 1
    public Page<BillResponse> findBillsByTableNumber(Long tableNumber, int pageNumber, int pageSize) {
        if(pageNumber < 1) pageNumber = 1;
        if(pageSize < 1) pageSize = 1;

        Sort sort = Sort.by("createdAt").descending();
        Pageable pageable = PageRequest.of(pageNumber-1, pageSize, sort);
        return billRepository.findByBilliardTable_TableNumber(tableNumber, pageable)
                .map(bill -> billMapper.toBillResponse(bill));
    }

    // Tham so:
    //      - pageNumber >= 1
    //      - pageSize >= 1
    public Page<BillResponse> getBills(int pageNumber, int pageSize) {
        if(pageNumber < 1) pageNumber = 1;
        if(pageSize < 1) pageSize = 1;

        Sort sort = Sort.by("createdAt").descending();
        Pageable pageable = PageRequest.of(pageNumber-1, pageSize, sort);
        return billRepository.findAll(pageable)
                .map(bill -> billMapper.toBillResponse(bill));
    }

    public BillResponse getBillById(Long id) {
        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> {throw new BillNotFoundException("Không tìm thấy hóa đơn");
                });
        return billMapper.toBillResponse(bill);
    }

    @PreAuthorize("hasRole(T(com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.constant.Role).ADMIN.name()) or @billService.isOwnerOfBill(#billId, authentication.principal.id)")
    @Transactional(rollbackFor = Exception.class)
    public void updateMemberOfBill(Long billId, Long memberId) {
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> {throw new BillNotFoundException("Không tìm thấy hóa đơn");});

        if(bill.getPaid()) {
            throw new BillUpdateException("Hóa đơn đã thanh toán, không thể cập nhật hội viên");
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> {throw new MemberNotFoundException("Không tim thấy hội viên");
                });
        if(member.getIsLocked()) {

            throw new BillUpdateException("Hội viên đã bị khóa, không thể cập nhật hội viên này cho hóa đơn");
        }

        bill.setMember(member);
        billRepository.save(bill);

        if(bill.getCheckOutTime() != null)
            calculateAndUpdateTotalAmount(billId);
    }

    @PreAuthorize("hasRole(T(com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.constant.Role).ADMIN.name()) or @billService.isOwnerOfBill(#billId, authentication.principal.id)")
    @Transactional(rollbackFor = Exception.class)
    public void confirmPayment(Long billId) {
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> {throw new BillNotFoundException("Không tìm thấy hóa đơn");});

        if(bill.getPaid()) {
            throw new BillPaymentConfirmationException("Hóa đơn đã được thanh toán");
        }

        if(bill.getCheckOutTime() == null) {
            throw new BillPaymentConfirmationException("Hóa đơn đang được xử lý");
        }

        bill.setPaid(true);
        billRepository.save(bill);

        if(bill.getMember() != null) {
            memberService.updateMemberLevelForMember(bill.getMember().getId());
        }
    }
}
