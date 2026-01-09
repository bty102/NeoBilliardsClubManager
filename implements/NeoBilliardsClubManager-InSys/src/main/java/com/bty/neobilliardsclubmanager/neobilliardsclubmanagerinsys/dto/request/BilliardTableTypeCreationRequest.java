package com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BilliardTableTypeCreationRequest {

    @Size(min = 3, max = 50, message = "Tên loại ít nhà là 3 và nhiều nhất là 50 ký tự")
    String name;

    @Min(value = 0, message = "Giá không hợp lệ")
    Long pricePerHour;

    @Size(max = 3000, message = "Mô tả không quá 3000 ký tự")
    String description;
}
