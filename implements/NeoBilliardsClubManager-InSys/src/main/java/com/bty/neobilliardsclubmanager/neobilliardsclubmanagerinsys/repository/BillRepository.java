package com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.repository;

import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.entity.Bill;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.entity.BilliardTable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
    Optional<Bill> findByBilliardTableAndCheckOutTime(BilliardTable billiardTable, LocalDateTime checkOutTime);

    Page<Bill> findByBilliardTable_TableNumber(Long tableNumber, Pageable pageable);

    Page<Bill> findAll(Pageable pageable);
}
