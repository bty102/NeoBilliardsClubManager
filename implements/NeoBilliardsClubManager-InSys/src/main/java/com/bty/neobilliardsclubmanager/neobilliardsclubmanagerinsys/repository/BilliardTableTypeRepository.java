package com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.repository;

import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.entity.BilliardTableType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BilliardTableTypeRepository extends JpaRepository<BilliardTableType, Long> {
}
