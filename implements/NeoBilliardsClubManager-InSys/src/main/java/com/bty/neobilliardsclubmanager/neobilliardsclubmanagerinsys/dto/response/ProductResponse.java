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
public class ProductResponse {

    Long id;

    String name;

    Long price;

    String description;

    Boolean isLocked;
}
