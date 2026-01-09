package com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.service;

import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.dto.request.BillCreationRequest;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.dto.request.BilliardTableCreationRequest;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.dto.request.BilliardTableUpdateRequest;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.dto.response.BilliardTableResponse;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.entity.Bill;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.entity.BilliardTable;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.entity.BilliardTableType;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.exception.*;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.mapper.BilliardTableMapper;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.repository.BillRepository;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.repository.BilliardTableRepository;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.repository.BilliardTableTypeRepository;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Validated
public class BilliardTableService {

    final BilliardTableRepository billiardTableRepository;
    final BilliardTableMapper billiardTableMapper;
    final BillService billService;
    final BillRepository billRepository;
    final BilliardTableTypeRepository billiardTableTypeRepository;

    // Tham so:
    //      - pageNumber >= 1
    //      - pageSize >= 1
    @PreAuthorize("hasRole(T(com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.constant.Role).ADMIN.name()) or #isLocked == false")
    public Page<BilliardTableResponse> getBilliardTablesByIsLocked(boolean isLocked, int pageNumber, int pageSize) {

        if(pageNumber < 1) pageNumber = 1;
        if(pageSize < 1) pageSize = 1;

        Pageable pageable = PageRequest.of(pageNumber-1, pageSize);

        return billiardTableRepository.findByIsLocked(isLocked, pageable)
                .map(billiardTable -> billiardTableMapper.toBilliardTableResponse(billiardTable));
    }

    @Transactional(rollbackFor = Exception.class)
    public void openBilliardTable(Long tableNumber, Long openedByAccountId) {
        BilliardTable billiardTable = billiardTableRepository.findByTableNumber(tableNumber)
                .orElseThrow(() -> {throw new BilliardTableOpeningException("Không tìm thấy số bàn này");
                });

        if(billiardTable.getIsLocked()) {
            throw new BilliardTableOpeningException("Bàn đã bị khóa");
        }

        if(billiardTable.getIsOpening()) {
            throw new BilliardTableOpeningException("Không thể mở bàn vì bàn đã được mở trước đó");
        }

        BillCreationRequest billCreationRequest = BillCreationRequest.builder()
                .billiardTableNumber(tableNumber)
                .createdByAccountId(openedByAccountId)
                .build();

        billService.createBill(billCreationRequest);
        billiardTable.setIsOpening(true);
        billiardTableRepository.save(billiardTable);
    }

    @Transactional(rollbackFor = Exception.class)
    public void closeBilliardTable(Long tableNumber) {
        BilliardTable billiardTable = billiardTableRepository.findByTableNumber(tableNumber)
                .orElseThrow(() -> {throw new BilliardTableClosingException("Đóng bàn thất bại");
                });
        if(billiardTable.getIsLocked()) {
            throw new BilliardTableClosingException("Đóng bàn thất bại");
        }
        if(!billiardTable.getIsOpening()) {
            throw new BilliardTableClosingException("Đóng bàn thất bại");
        }

        Bill bill = billRepository.findByBilliardTableAndCheckOutTime(billiardTable, null)
                .orElseThrow(() -> {throw new BilliardTableClosingException("Đóng bàn thất bại");});
        billService.updateCheckOutTime(bill.getId(), LocalDateTime.now());
        billiardTable.setIsOpening(false);
        billiardTableRepository.save(billiardTable);
    }

    public BilliardTableResponse getBilliardTableByTableNumber(Long tableNumber) {
        BilliardTable billiardTable = billiardTableRepository.findByTableNumber(tableNumber)
                .orElseThrow(() -> {throw new BilliardTableNotFoundException("Không tìm thấy bàn");
                });
        return billiardTableMapper.toBilliardTableResponse(billiardTable);
    }

    @PreAuthorize("hasRole(T(com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.constant.Role).ADMIN.name())")
    public void updateBilliardTableImagePath(Long tableNumber, String imagePath) {

        BilliardTable billiardTable = billiardTableRepository.findByTableNumber(tableNumber)
                .orElseThrow(() -> {throw new BilliardTableNotFoundException("Không tìm thấy bàn");});
        billiardTable.setImagePath(imagePath);
        billiardTableRepository.save(billiardTable);
    }

    @PreAuthorize("hasRole(T(com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.constant.Role).ADMIN.name())")
    public void updateBilliardTable(@Valid BilliardTableUpdateRequest request) {

        BilliardTable billiardTable = billiardTableRepository.findById(request.getId())
                .orElseThrow(() -> {throw new BilliardTableUpdateException("Không tồn tại bàn này");});

        if(billiardTable.getTableNumber() != request.getTableNumber() && billiardTableRepository.existsByTableNumber(request.getTableNumber())) {
            throw new BilliardTableUpdateException("Đã tồn tại số bàn");
        }

        BilliardTableType billiardTableType = billiardTableTypeRepository.findById(request.getBilliardTableTypeId())
                .orElseThrow(() -> {throw new BilliardTableUpdateException("Không tồn tại loại bàn này");});

        billiardTable.setTableNumber(request.getTableNumber());
        billiardTable.setBilliardTableType(billiardTableType);
        billiardTableRepository.save(billiardTable);
    }

    @PreAuthorize("hasRole(T(com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.constant.Role).ADMIN.name())")
    public void createBilliardTable(@Valid BilliardTableCreationRequest request) {
        if(billiardTableRepository.existsByTableNumber(request.getTableNumber())) {
            throw new BilliardTableCreationException("Số bàn đã tồn tại");
        }

        BilliardTable billiardTable = billiardTableMapper.toBilliardTable(request);
        billiardTableRepository.save(billiardTable);
    }

    @PreAuthorize("hasRole(T(com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.constant.Role).ADMIN.name())")
    public void lockBilliardTable(Long tableNumber) {
        BilliardTable billiardTable = billiardTableRepository.findByTableNumber(tableNumber)
                .orElseThrow(() -> {throw new BilliardTableNotFoundException("Không tìm thấy bàn này");});
        billiardTable.setIsLocked(true);
        billiardTableRepository.save(billiardTable);
    }

    @PreAuthorize("hasRole(T(com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.constant.Role).ADMIN.name())")
    public void unlockBilliardTable(Long tableNumber) {
        BilliardTable billiardTable = billiardTableRepository.findByTableNumber(tableNumber)
                .orElseThrow(() -> {throw new BilliardTableNotFoundException("Không tìm thấy bàn này");});
        billiardTable.setIsLocked(false);
        billiardTableRepository.save(billiardTable);
    }
}
