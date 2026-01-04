package com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountResponse {

    Long id;

    String username;

    Integer role;

    Boolean isLocked;

    LocalDateTime createdAt;

    EmployeeProfileResponse employeeProfile;
}
