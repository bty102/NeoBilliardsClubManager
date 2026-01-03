package com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.mapper;

import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.dto.response.MemberResponse;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.entity.Member;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MemberMapper {

    final MemberLevelMapper memberLevelMapper;

    public MemberResponse toMemberResponse(Member member) {
        return MemberResponse.builder()
                .id(member.getId())
                .fullName(member.getFullName())
                .isMale(member.getIsMale())
                .dateOfBirth(member.getDateOfBirth())
                .email(member.getEmail())
                .imagePath(member.getImagePath())
                .username(member.getUsername())
                .isLocked(member.getIsLocked())
                .createdAt(member.getCreatedAt())
                .memberLevel(memberLevelMapper.toMemberLevelResponse(member.getMemberLevel()))
                .build();
    }
}
