package com.bty.neobilliardsclubmanager.neobilliardsclubmanagerexsys.repository;

import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerexsys.entity.Bill;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerexsys.entity.BilliardTable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
    Optional<Bill> findByBilliardTableAndCheckOutTime(BilliardTable billiardTable, LocalDateTime checkOutTime);

    Page<Bill> findByBilliardTable_TableNumber(Long tableNumber, Pageable pageable);

    Page<Bill> findAll(Pageable pageable);

    List<Bill> findByMember_Id(Long memberId);

    List<Bill> findByMember_IdAndCheckOutTime(Long memberId, LocalDateTime checkOutTime);

    List<Bill> findByMember_Id(Long memberId, Sort sort);
}
