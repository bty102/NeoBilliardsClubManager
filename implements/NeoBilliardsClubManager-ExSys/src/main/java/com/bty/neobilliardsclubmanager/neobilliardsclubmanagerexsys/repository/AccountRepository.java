package com.bty.neobilliardsclubmanager.neobilliardsclubmanagerexsys.repository;

import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerexsys.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByUsername(String username);

    boolean existsByUsername(String username);

    List<Account> findByRole(Integer role);

    @Query("""
    SELECT a
    FROM Account a
    JOIN a.employeeProfile e
    WHERE a.role = :role
      AND (
           LOWER(e.fullName) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR LOWER(e.address) LIKE LOWER(CONCAT('%', :keyword, '%'))
      )
    """)
    List<Account> searchEmployee(
            @Param("role") Integer role,
            @Param("keyword") String keyword
    );
}
