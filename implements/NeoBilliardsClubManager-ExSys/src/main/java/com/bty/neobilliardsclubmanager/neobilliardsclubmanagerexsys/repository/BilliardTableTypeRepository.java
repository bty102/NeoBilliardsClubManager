package com.bty.neobilliardsclubmanager.neobilliardsclubmanagerexsys.repository;

import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerexsys.entity.BilliardTableType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BilliardTableTypeRepository extends JpaRepository<BilliardTableType, Long> {
    List<BilliardTableType> findByNameContaining(String name);

    boolean existsByName(String name);
}
