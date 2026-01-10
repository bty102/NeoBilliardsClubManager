package com.bty.neobilliardsclubmanager.neobilliardsclubmanagerexsys.service;

import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerexsys.entity.Member;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerexsys.repository.MemberRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MemberService {

    final MemberRepository memberRepository;
    final PasswordEncoder passwordEncoder;

    public void createMember(Member member) {
        if(memberRepository.existsByEmail(member.getEmail())) {
            throw new RuntimeException();
        }
        if(memberRepository.existsByUsername(member.getUsername())) {
            throw new RuntimeException();
        }
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        member.setCreatedAt(LocalDateTime.now());
        member.setIsLocked(false);
        memberRepository.save(member);
    }

    public Member getMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> {throw new RuntimeException();});
    }

    public void updateMember(Member member) {
        if(!memberRepository.existsById(member.getId())) {
            throw new RuntimeException();
        }
        Member member1 = memberRepository.findById(member.getId()).get();
        if(!member1.getEmail().equals(member.getEmail()) && memberRepository.existsByEmail(member.getEmail())) {
            throw new RuntimeException();
        }
        memberRepository.save(member);
    }
}
