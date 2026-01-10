package com.bty.neobilliardsclubmanager.neobilliardsclubmanagerexsys.repository;

import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerexsys.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Page<Member> findAll(Pageable pageable);

    List<Member> findByFullNameContaining(String fullName);

    Optional<Member> findByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);
}
