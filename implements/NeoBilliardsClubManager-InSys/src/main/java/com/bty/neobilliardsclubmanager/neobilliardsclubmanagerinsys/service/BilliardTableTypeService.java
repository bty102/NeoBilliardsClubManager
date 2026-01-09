package com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.service;

import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.dto.response.BilliardTableTypeResponse;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.entity.BilliardTableType;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.mapper.BilliardTableTypeMapper;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.repository.BilliardTableTypeRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
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
}
