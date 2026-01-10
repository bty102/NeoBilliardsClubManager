package com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MonthlyRevenueStatisticsForTheYear {

    Integer month;
    Long totalRevenue;
}
