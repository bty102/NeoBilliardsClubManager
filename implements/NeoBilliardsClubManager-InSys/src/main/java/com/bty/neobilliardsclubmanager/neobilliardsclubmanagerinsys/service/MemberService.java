package com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.service;

import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.dto.response.MemberResponse;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.mapper.MemberMapper;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.repository.MemberRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MemberService {

    final MemberRepository memberRepository;
    final MemberMapper memberMapper;

    // Tham so:
    //      - pageNumber >= 1
    //      - pageSize >= 1
    public Page<MemberResponse> getMembers(int pageNumber, int pageSize) {
        if(pageNumber < 1) pageNumber = 1;
        if(pageSize < 1) pageSize = 1;

        Pageable pageable = PageRequest.of(pageNumber-1, pageSize);
        return memberRepository.findAll(pageable)
                .map(member -> memberMapper.toMemberResponse(member));
    }
}
