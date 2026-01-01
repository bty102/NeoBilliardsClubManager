package com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.mapper;

import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.dto.response.BilliardTableTypeResponse;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.entity.BilliardTableType;
import org.springframework.stereotype.Component;

@Component
public class BilliardTableTypeMapper {

    public BilliardTableTypeResponse toBilliardTableTypeResponse(BilliardTableType billiardTableType) {
        return BilliardTableTypeResponse.builder()
                .id(billiardTableType.getId())
                .name(billiardTableType.getName())
                .pricePerHour(billiardTableType.getPricePerHour())
                .description(billiardTableType.getDescription())
                .build();
    }
}
