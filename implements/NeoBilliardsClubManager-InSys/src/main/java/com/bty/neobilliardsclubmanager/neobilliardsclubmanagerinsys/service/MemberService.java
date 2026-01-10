package com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.service;

import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.dto.response.MemberResponse;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.entity.Bill;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.entity.Member;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.entity.MemberLevel;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.exception.MemberNotFoundException;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.mapper.MemberMapper;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.repository.MemberLevelRepository;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.repository.MemberRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MemberService {

    final MemberRepository memberRepository;
    final MemberMapper memberMapper;
    final MemberLevelRepository memberLevelRepository;

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

    public void updateMemberLevelForMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> {throw new MemberNotFoundException("Không tìm thấy hội viên");
                });
        double hoursPlayedOfMember = 0;
        List<Bill> bills = member.getBills();
        for(Bill bill : bills) {

            if(bill.getCheckOutTime() == null) continue;

            Duration duration = Duration.between(bill.getCheckInTime(), bill.getCheckOutTime());
            double hoursPlayed = duration.toMinutes()/60.0;// So gio da choi
            hoursPlayedOfMember += hoursPlayed;
        }

        Sort sort = Sort.by("requiredPlaytimeHours").descending();
        List<MemberLevel> memberLevels = memberLevelRepository.findAll(sort);
        for(MemberLevel memberLevel : memberLevels) {
            if(hoursPlayedOfMember >= memberLevel.getRequiredPlaytimeHours()) {
                member.setMemberLevel(memberLevel);
                break;
            }
        }
        memberRepository.save(member);
    }

    public List<MemberResponse> getAllMembers() {
        List<Member> members = memberRepository.findAll();
        return members
                .stream()
                .map(member -> memberMapper.toMemberResponse(member))
                .toList();
    }

    public List<MemberResponse> findMembersByFullNameContaining(String key) {
        List<Member> members = memberRepository.findByFullNameContaining(key);
        return members
                .stream()
                .map(member -> memberMapper.toMemberResponse(member))
                .toList();
    }

    @PreAuthorize("hasRole(T(com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.constant.Role).ADMIN.name())")
    public void lockMember(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> {throw new MemberNotFoundException("Không tìm thấy hội viên");});
        member.setIsLocked(true);
        memberRepository.save(member);
    }

    @PreAuthorize("hasRole(T(com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.constant.Role).ADMIN.name())")
    public void unlockMember(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> {throw new MemberNotFoundException("Không tìm thấy hội viên");});
        member.setIsLocked(false);
        memberRepository.save(member);
    }
}
