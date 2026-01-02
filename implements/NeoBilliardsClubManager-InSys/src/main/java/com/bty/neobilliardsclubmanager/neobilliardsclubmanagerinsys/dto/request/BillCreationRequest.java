package com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BillCreationRequest {

    Long billiardTableNumber;
    Long createdByAccountId;
}
