package com.bty.neobilliardsclubmanager.neobilliardsclubmanagerexsys.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Bill")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Bill {

    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "CheckInTime", nullable = false)
    LocalDateTime checkInTime;

    @Column(name = "CheckOutTime", nullable = true)
    LocalDateTime checkOutTime;

    @Column(name = "TotalAmount", nullable = true)
    Long totalAmount;

    @Column(name = "CreatedAt", nullable = false)
    LocalDateTime createdAt;

    @Column(name = "Paid", nullable = false)
    Boolean paid;

    @ManyToOne
    @JoinColumn(name = "BilliardTableId")
    BilliardTable billiardTable;

    @ManyToOne
    @JoinColumn(name = "OfMemberId")
    Member member;

    @ManyToOne
    @JoinColumn(name = "CreatedByAccountId")
    Account account;

    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<BillDetail> billDetails = new ArrayList<>();
}
