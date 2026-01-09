package com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.dto.request;

import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BilliardTableUpdateRequest {

    Long id;

    @Min(value = 0, message = "Số bàn không hợp lệ")
    Long tableNumber;

    Long billiardTableTypeId;
}
