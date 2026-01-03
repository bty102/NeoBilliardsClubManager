package com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.mapper;

import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.dto.response.MemberLevelResponse;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.entity.MemberLevel;
import org.springframework.stereotype.Component;

@Component
public class MemberLevelMapper {

    public MemberLevelResponse toMemberLevelResponse(MemberLevel memberLevel) {
        return MemberLevelResponse.builder()
                .id(memberLevel.getId())
                .name(memberLevel.getName())
                .requiredPlaytimeHours(memberLevel.getRequiredPlaytimeHours())
                .billTotalDiscountPercentage(memberLevel.getBillTotalDiscountPercentage())
                .build();
    }
}
