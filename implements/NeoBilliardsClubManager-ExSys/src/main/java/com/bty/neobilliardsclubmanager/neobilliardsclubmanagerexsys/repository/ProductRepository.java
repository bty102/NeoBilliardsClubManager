package com.bty.neobilliardsclubmanager.neobilliardsclubmanagerexsys.repository;

import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerexsys.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByIsLocked(Boolean isLocked);

    List<Product> findByNameContaining(String name);

    boolean existsByName(String name);
}
