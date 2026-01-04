package com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.mapper;

import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.dto.response.EmployeeProfileResponse;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.entity.EmployeeProfile;
import org.springframework.stereotype.Component;

@Component
public class EmployeeProfileMapper {

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
}
