package com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.mapper;

import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.dto.request.BilliardTableTypeCreationRequest;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.dto.request.BilliardTableTypeUpdateRequest;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.dto.request.BilliardTableUpdateRequest;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.dto.response.BilliardTableTypeResponse;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.entity.BilliardTable;
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

    public BilliardTableTypeUpdateRequest toBilliardTableTypeUpdateRequest(BilliardTableTypeResponse response) {
        return BilliardTableTypeUpdateRequest.builder()
                .id(response.getId())
                .name(response.getName())
                .pricePerHour(response.getPricePerHour())
                .description(response.getDescription())
                .build();
    }

    public BilliardTableType toBilliardTableType(BilliardTableTypeCreationRequest request) {
        return BilliardTableType.builder()
                .name(request.getName())
                .pricePerHour(request.getPricePerHour())
                .description(request.getDescription())
                .build();
    }
}
