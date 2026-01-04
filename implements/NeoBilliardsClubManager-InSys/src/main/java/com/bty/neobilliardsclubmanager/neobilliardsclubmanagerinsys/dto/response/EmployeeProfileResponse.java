package com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmployeeProfileResponse {

    Long id;

    String fullName;

    Boolean isMale;

    LocalDate dateOfBirth;

    String address;

    String phoneNumber;

    String citizenIDNumber;

    String email;

    String imagePath;
}
