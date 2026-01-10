package com.bty.neobilliardsclubmanager.neobilliardsclubmanagerexsys.config;

import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerexsys.entity.Member;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerexsys.repository.MemberRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DataInit implements CommandLineRunner {

    final MemberRepository memberRepository;
    final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
//        Member member = memberRepository.findById(3L).get();
//        member.setPassword(passwordEncoder.encode("vhm"));
//        memberRepository.save(member);
    }
}
