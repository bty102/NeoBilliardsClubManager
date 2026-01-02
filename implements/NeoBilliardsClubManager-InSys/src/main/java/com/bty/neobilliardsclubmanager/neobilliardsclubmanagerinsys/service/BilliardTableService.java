package com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.service;

import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.dto.response.BilliardTableResponse;
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

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BilliardTableService {

    final BilliardTableRepository billiardTableRepository;
    final BilliardTableMapper billiardTableMapper;

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

}
