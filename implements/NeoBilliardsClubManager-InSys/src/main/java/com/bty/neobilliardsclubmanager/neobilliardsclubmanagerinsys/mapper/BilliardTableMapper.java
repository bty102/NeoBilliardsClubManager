package com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.mapper;

import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.dto.response.BilliardTableResponse;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.entity.BilliardTable;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BilliardTableMapper {

    final BilliardTableTypeMapper billiardTableTypeMapper;

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
}
