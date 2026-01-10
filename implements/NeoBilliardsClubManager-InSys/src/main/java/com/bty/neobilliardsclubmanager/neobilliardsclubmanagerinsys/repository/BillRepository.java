package com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.repository;

import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.dto.response.MonthlyRevenueStatisticsForTheYear;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.entity.Bill;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.entity.BilliardTable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Query("""
        SELECT new com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.dto.response
            .MonthlyRevenueStatisticsForTheYear(
                MONTH(b.checkOutTime),
                SUM(b.totalAmount)
            )
        FROM Bill b
        WHERE b.paid = true
          AND YEAR(b.checkOutTime) = :year
        GROUP BY MONTH(b.checkOutTime)
        ORDER BY MONTH(b.checkOutTime)
    """)
    List<MonthlyRevenueStatisticsForTheYear> getMonthlyRevenue(int year);
}
