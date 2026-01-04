package com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.mapper;

import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.dto.response.AccountResponse;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.entity.Account;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountMapper {

    final EmployeeProfileMapper employeeProfileMapper;

    public AccountResponse toAccountResponse(Account account) {
        return AccountResponse.builder()
                .id(account.getId())
                .username(account.getUsername())
                .role(account.getRole())
                .isLocked(account.getIsLocked())
                .createdAt(account.getCreatedAt())
                .employeeProfile(employeeProfileMapper.toEmployeeProfileResponse(account.getEmployeeProfile()))
                .build();
    }
}
