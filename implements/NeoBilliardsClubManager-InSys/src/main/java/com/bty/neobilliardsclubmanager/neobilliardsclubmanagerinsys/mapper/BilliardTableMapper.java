package com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.mapper;

import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.dto.request.BilliardTableCreationRequest;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.dto.request.BilliardTableUpdateRequest;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.dto.response.BilliardTableResponse;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.entity.BilliardTable;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.entity.BilliardTableType;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.exception.BilliardTableTypeNotFoundException;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.repository.BilliardTableTypeRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BilliardTableMapper {

    final BilliardTableTypeMapper billiardTableTypeMapper;
    final BilliardTableTypeRepository billiardTableTypeRepository;

    public BilliardTableResponse toBilliardTableResponse(BilliardTable billiardTable) {
        return BilliardTableResponse.builder()
                .id(billiardTable.getId())
                .tableNumber(billiardTable.getTableNumber())
                .imagePath(billiardTable.getImagePath())
                .isLocked(billiardTable.getIsLocked())
                .isOpening(billiardTable.getIsOpening())
                .billiardTableType(billiardTableTypeMapper.toBilliardTableTypeResponse(billiardTable.getBilliardTableType()))
                .build();
    }

    public BilliardTableUpdateRequest toBilliardTableUpdateRequest(BilliardTableResponse response) {
        return BilliardTableUpdateRequest.builder()
                .id(response.getId())
                .tableNumber(response.getTableNumber())
                .billiardTableTypeId(response.getBilliardTableType().getId())
                .build();
    }

    public BilliardTableCreationRequest toBilliardTableCreationRequest(BilliardTableResponse response) {
        return BilliardTableCreationRequest.builder()
                .tableNumber(response.getTableNumber())
                .billiardTableTypeId(response.getBilliardTableType().getId())
                .build();
    }

    public BilliardTable toBilliardTable(BilliardTableCreationRequest request) {
        BilliardTableType billiardTableType = billiardTableTypeRepository.findById(request.getBilliardTableTypeId())
                .orElseThrow(() -> {throw new BilliardTableTypeNotFoundException("Không tồn tại loại bàn");
                });
        return BilliardTable.builder()
                .tableNumber(request.getTableNumber())
                .billiardTableType(billiardTableType)
                .isLocked(false)
                .isOpening(false)
                .imagePath(null)
                .build();
    }
}
