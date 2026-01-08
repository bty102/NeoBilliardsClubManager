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
public class BillDetailCreationRequest {

    @Min(value = 0, message = "Số lượng không hợp lệ")
    Long quantity;

    Long billId;

    Long productId;
}
