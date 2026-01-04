package com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.mapper;

import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.dto.response.BillResponse;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.entity.Bill;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BillMapper {

    final BilliardTableMapper billiardTableMapper;
    final MemberMapper memberMapper;
    final AccountMapper accountMapper;

    public BillResponse toBillResponse(Bill bill) {
        return BillResponse.builder()
                .id(bill.getId())
                .checkInTime(bill.getCheckInTime())
                .checkOutTime(bill.getCheckOutTime())
                .totalAmount(bill.getTotalAmount())
                .createdAt(bill.getCreatedAt())
                .paid(bill.getPaid())
                .billiardTable(billiardTableMapper.toBilliardTableResponse(bill.getBilliardTable()))
                .member(memberMapper.toMemberResponse(bill.getMember()))
                .account(accountMapper.toAccountResponse(bill.getAccount()))
                .build();
    }
}
