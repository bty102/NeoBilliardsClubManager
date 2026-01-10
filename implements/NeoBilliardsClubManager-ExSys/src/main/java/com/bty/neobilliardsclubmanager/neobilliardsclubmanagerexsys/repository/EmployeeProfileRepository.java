package com.bty.neobilliardsclubmanager.neobilliardsclubmanagerexsys.repository;

import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerexsys.entity.EmployeeProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeProfileRepository extends JpaRepository<EmployeeProfile, Long> {
    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByCitizenIDNumber(String citizenIDNumber);

    boolean existsByEmail(String email);
}
