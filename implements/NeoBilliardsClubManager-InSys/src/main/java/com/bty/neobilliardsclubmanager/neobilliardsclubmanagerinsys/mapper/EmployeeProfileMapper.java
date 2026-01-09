package com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.mapper;

import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.dto.request.EmployeeProfileCreationRequest;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.dto.request.EmployeeProfileUpdateRequest;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.dto.response.EmployeeProfileResponse;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.entity.Account;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.entity.EmployeeProfile;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.exception.AccountNotFoundException;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.repository.AccountRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmployeeProfileMapper {

    final AccountRepository accountRepository;
    private final ThymeleafViewResolver thymeleafViewResolver;

    public EmployeeProfileResponse toEmployeeProfileResponse(EmployeeProfile employeeProfile) {
        return EmployeeProfileResponse.builder()
                .id(employeeProfile.getId())
                .fullName(employeeProfile.getFullName())
                .isMale(employeeProfile.getIsMale())
                .dateOfBirth(employeeProfile.getDateOfBirth())
                .address(employeeProfile.getAddress())
                .phoneNumber(employeeProfile.getPhoneNumber())
                .citizenIDNumber(employeeProfile.getCitizenIDNumber())
                .email(employeeProfile.getEmail())
                .imagePath(employeeProfile.getImagePath())
                .build();
    }

    public EmployeeProfile toEmployeeProfile(EmployeeProfileCreationRequest request) {
        Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> {throw new AccountNotFoundException("Không tìm thấy tài khoản");
                });
        return EmployeeProfile.builder()
                .fullName(request.getFullName())
                .isMale(request.getIsMale())
                .dateOfBirth(request.getDateOfBirth())
                .address(request.getAddress())
                .phoneNumber(request.getPhoneNumber())
                .citizenIDNumber(request.getCitizenIDNumber())
                .email(request.getEmail())
                .account(account)
                .build();
    }

    public EmployeeProfileUpdateRequest toEmployeeProfileUpdateRequest(EmployeeProfileResponse response) {
        return EmployeeProfileUpdateRequest.builder()
                .id(response.getId())
                .fullName(response.getFullName())
                .isMale(response.getIsMale())
                .dateOfBirth(response.getDateOfBirth())
                .address(response.getAddress())
                .phoneNumber(response.getPhoneNumber())
                .citizenIDNumber(response.getCitizenIDNumber())
                .email(response.getEmail())
                .build();
    }
}
