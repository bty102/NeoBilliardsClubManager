package com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.dto.response;

import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.entity.Bill;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.entity.Product;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BillDetailResponse {

    Long id;

    Long quantity;

    BillResponse bill;

    ProductResponse product;
}
