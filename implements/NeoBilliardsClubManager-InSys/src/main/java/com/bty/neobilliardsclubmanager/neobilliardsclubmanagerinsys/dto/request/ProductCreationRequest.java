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
public class ProductCreationRequest {

    @Size(min = 3, max = 250, message = "Tên sản phẩm ít nhất là 3 và nhiều nhất là 250 ký tự")
    String name;

    @Min(value = 0, message = "Giá không hợp lệ")
    Long price;

    @Size(max = 3000, message = "Mô tả nhiều nhất là 3000 ký tự")
    String description;
}
