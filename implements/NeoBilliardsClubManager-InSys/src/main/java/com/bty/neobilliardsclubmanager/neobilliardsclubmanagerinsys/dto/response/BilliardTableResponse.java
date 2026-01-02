package com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.dto.response;

import jakarta.persistence.Column;
import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BilliardTableResponse {

    Long id;

    Long tableNumber;

    String imagePath;

    Boolean isLocked;

    Boolean isOpening;

    BilliardTableTypeResponse billiardTableType;
}
