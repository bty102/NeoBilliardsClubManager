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
public class BilliardTableTypeResponse {

    Long id;

    String name;

    Long pricePerHour;

    String description;

}
