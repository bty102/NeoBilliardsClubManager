package com.bty.neobilliardsclubmanager.neobilliardsclubmanagerexsys.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "BilliardTable")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BilliardTable {

    @Id()
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "TableNumber", nullable = false, unique = true)
    Long tableNumber;

    @Column(name = "ImagePath", columnDefinition = "NVARCHAR(3000)", length = 3000, nullable = true)
    String imagePath;

    @Column(name = "IsLocked", nullable = false)
    Boolean isLocked;

    @Column(name = "IsOpening", nullable = false)
    Boolean isOpening;

    @ManyToOne
    @JoinColumn(name = "BilliardTableTypeId", nullable = false)
    BilliardTableType billiardTableType;

    @OneToMany(mappedBy = "billiardTable", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Bill> bills = new ArrayList<>();
}