package com.bty.neobilliardsclubmanager.neobilliardsclubmanagerexsys.repository;

import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerexsys.entity.Bill;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerexsys.entity.BillDetail;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerexsys.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BillDetailRepository extends JpaRepository<BillDetail, Long> {
    List<BillDetail> findByBill_Id(Long billId);

    Optional<BillDetail> findByBillAndProduct(Bill bill, Product product);

    boolean existsByBillAndProduct(Bill bill, Product product);
}
