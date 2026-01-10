package com.bty.neobilliardsclubmanager.neobilliardsclubmanagerexsys.service;

import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerexsys.entity.Bill;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerexsys.repository.BillRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BillService {

    final BillRepository billRepository;

    public List<Bill> getBills() {
        return billRepository.findAll();
    }

    public List<Bill> getBillsByMemberId(Long memberId) {
        Sort sort = Sort.by("createdAt").descending();
        return billRepository.findByMember_Id(memberId, sort);
    }

    public Bill getBillById(Long id) {
        return billRepository.findById(id)
                .orElseThrow(() -> {throw new RuntimeException();});
    }

    public List<Bill> getCurrentPlayingBill(Long memberId) {
        return billRepository.findByMember_IdAndCheckOutTime(memberId, null);
    }
}
