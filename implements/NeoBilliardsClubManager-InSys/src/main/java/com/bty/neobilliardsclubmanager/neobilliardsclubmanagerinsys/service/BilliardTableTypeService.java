package com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.service;

import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.dto.request.BilliardTableTypeCreationRequest;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.dto.request.BilliardTableTypeUpdateRequest;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.dto.response.BilliardTableTypeResponse;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.entity.BilliardTableType;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.exception.BilliardTableTypeNotFoundException;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.exception.BilliardTableTypeUpdateException;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.mapper.BilliardTableTypeMapper;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.repository.BilliardTableTypeRepository;
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
public class BilliardTableTypeService {

    final BilliardTableTypeRepository billiardTableTypeRepository;
    final BilliardTableTypeMapper billiardTableTypeMapper;

    public List<BilliardTableTypeResponse> getAll() {
        List<BilliardTableType> billiardTableTypes = billiardTableTypeRepository.findAll();
        return billiardTableTypes
                .stream()
                .map(billiardTableType -> billiardTableTypeMapper.toBilliardTableTypeResponse(billiardTableType))
                .toList();
    }

    public List<BilliardTableTypeResponse> findBilliardTableTypesByNameContaining(String name) {
        return billiardTableTypeRepository.findByNameContaining(name)
                .stream()
                .map(billiardTableType -> billiardTableTypeMapper.toBilliardTableTypeResponse(billiardTableType))
                .toList();
    }

    public BilliardTableTypeResponse getBilliardTableTypeById(Long id) {
        BilliardTableType billiardTableType = billiardTableTypeRepository.findById(id)
                .orElseThrow(() -> {throw new BilliardTableTypeNotFoundException("Không tìm thấy loa bàn");
                });
        return billiardTableTypeMapper.toBilliardTableTypeResponse(billiardTableType);
    }

    @PreAuthorize("hasRole(T(com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.constant.Role).ADMIN.name())")
    public void updateBilliardTableType(@Valid BilliardTableTypeUpdateRequest request) {
        BilliardTableType billiardTableType = billiardTableTypeRepository.findById(request.getId())
                .orElseThrow(() -> {throw new BilliardTableTypeNotFoundException("Không tìm thấy loại bàn này");});

        if(billiardTableType.getName() != request.getName()
        && billiardTableTypeRepository.existsByName(request.getName())) {
            throw new BilliardTableTypeUpdateException("Tên loại đã tồn tại");
        }

        billiardTableType.setName(request.getName());
        billiardTableType.setPricePerHour(request.getPricePerHour());
        billiardTableType.setDescription(request.getDescription());
        billiardTableTypeRepository.save(billiardTableType);

    }

    @PreAuthorize("hasRole(T(com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.constant.Role).ADMIN.name())")
    public void createBilliardTableType(@Valid BilliardTableTypeCreationRequest request) {
        BilliardTableType billiardTableType = billiardTableTypeMapper.toBilliardTableType(request);
        billiardTableTypeRepository.save(billiardTableType);
    }
}
