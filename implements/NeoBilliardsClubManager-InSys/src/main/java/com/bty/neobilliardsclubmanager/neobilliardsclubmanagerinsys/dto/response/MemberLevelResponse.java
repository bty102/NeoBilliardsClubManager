package com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MemberLevelResponse {

    Long id;

    String name;

    Long requiredPlaytimeHours;

    Integer billTotalDiscountPercentage;
}
