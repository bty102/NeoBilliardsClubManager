package com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.repository;

import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.entity.BilliardTable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BilliardTableRepository extends JpaRepository<BilliardTable, Long> {

    Page<BilliardTable> findByIsLocked(Boolean isLocked, Pageable pageable);

    Optional<BilliardTable> findByTableNumber(Long tableNumber);

    boolean existsByTableNumber(Long tableNumber);
}
