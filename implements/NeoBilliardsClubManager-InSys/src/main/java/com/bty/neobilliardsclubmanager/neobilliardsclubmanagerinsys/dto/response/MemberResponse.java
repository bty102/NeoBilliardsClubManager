package com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.dto.response;

import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.entity.MemberLevel;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MemberResponse {

    Long id;

    String fullName;

    Boolean isMale;

    LocalDate dateOfBirth;

    String email;

    String imagePath;

    String username;

    Boolean isLocked;

    LocalDateTime createdAt;

    MemberLevelResponse memberLevel;
}
