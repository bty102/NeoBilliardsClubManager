package com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.service;

import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.dto.request.EmployeeProfileCreationRequest;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.dto.request.EmployeeProfileUpdateRequest;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.dto.response.EmployeeProfileResponse;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.entity.EmployeeProfile;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.exception.EmployeeProfileCreationException;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.exception.EmployeeProfileNotFoundException;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.exception.EmployeeProfileUpdateException;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.mapper.EmployeeProfileMapper;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.repository.EmployeeProfileRepository;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Validated
public class EmployeeProfileService {

    final EmployeeProfileRepository employeeProfileRepository;
    final EmployeeProfileMapper employeeProfileMapper;

    @PreAuthorize("hasRole(T(com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.constant.Role).ADMIN.name())")
    public void createEmployeeProfile(@Valid EmployeeProfileCreationRequest request) {

        if(employeeProfileRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new EmployeeProfileCreationException("Số điện thoại đã tồn tại");
        }
        if(employeeProfileRepository.existsByCitizenIDNumber(request.getCitizenIDNumber())) {

            throw new EmployeeProfileCreationException("Số CCCD đã tồn tại");
        }
        if(employeeProfileRepository.existsByEmail(request.getEmail())) {

            throw new EmployeeProfileCreationException("Email đã tồn tại");
        }
        List<EmployeeProfile> employeeProfiles = employeeProfileRepository.findAll();
        for (EmployeeProfile employeeProfile : employeeProfiles) {
            if(employeeProfile.getAccount().getId() == request.getAccountId()) {

                throw new EmployeeProfileCreationException("Account đã có profile");
            }
        }
        EmployeeProfile employeeProfile = employeeProfileMapper.toEmployeeProfile(request);
        employeeProfileRepository.save(employeeProfile);
    }

    @PreAuthorize("hasRole(T(com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.constant.Role).ADMIN.name())")
    public void updateEmployeeProfile(@Valid EmployeeProfileUpdateRequest request) {
        EmployeeProfile employeeProfile = employeeProfileRepository.findById(request.getId())
                .orElseThrow(() -> {throw new EmployeeProfileUpdateException("Hồ sơ nhân viên không tồn tại");
                });
        if(!employeeProfile.getPhoneNumber().equals(request.getPhoneNumber())
        && employeeProfileRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new EmployeeProfileUpdateException("Số điện thoại đã tồn tại");
        }
        if(!employeeProfile.getCitizenIDNumber().equals(request.getCitizenIDNumber())
        && employeeProfileRepository.existsByCitizenIDNumber(request.getCitizenIDNumber())) {

            throw new EmployeeProfileUpdateException("Số CCCD đã tồn tại");
        }
        if(!employeeProfile.getEmail().equals(request.getEmail())
        && employeeProfileRepository.existsByEmail(request.getEmail())) {

            throw new EmployeeProfileUpdateException("Email đã tồn tại");
        }

        employeeProfile.setFullName(request.getFullName());
        employeeProfile.setIsMale(request.getIsMale());
        employeeProfile.setDateOfBirth(request.getDateOfBirth());
        employeeProfile.setAddress(request.getAddress());
        employeeProfile.setPhoneNumber(request.getPhoneNumber());
        employeeProfile.setCitizenIDNumber(request.getCitizenIDNumber());
        employeeProfile.setEmail(request.getEmail());
        employeeProfileRepository.save(employeeProfile);
    }

    @PreAuthorize("hasRole(T(com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.constant.Role).ADMIN.name()) or (authentication.principal.employeeProfile.id == #id)")
    public EmployeeProfileResponse getEmployeeProfileById(Long id) {
        EmployeeProfile employeeProfile = employeeProfileRepository.findById(id)
                .orElseThrow(() -> {throw new EmployeeProfileNotFoundException("Không tồn tại hồ sơ nhân viên");
                });
        return employeeProfileMapper.toEmployeeProfileResponse(employeeProfile);
    }
}
