package com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.service;

import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.dto.request.BillCreationRequest;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.dto.response.BilliardTableResponse;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.entity.BilliardTable;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.exception.BillCreationException;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.exception.BilliardTableOpeningException;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.mapper.BilliardTableMapper;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.repository.BilliardTableRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BilliardTableService {

    final BilliardTableRepository billiardTableRepository;
    final BilliardTableMapper billiardTableMapper;
    final BillService billService;

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

}
