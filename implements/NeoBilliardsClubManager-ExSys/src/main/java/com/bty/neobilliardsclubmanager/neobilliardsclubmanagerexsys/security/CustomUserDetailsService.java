package com.bty.neobilliardsclubmanager.neobilliardsclubmanagerexsys.security;

import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerexsys.entity.Member;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerexsys.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> {throw new UsernameNotFoundException("Không tìm thấy hội viên");});

        return new CustomUserDetails(member);
    }
}
