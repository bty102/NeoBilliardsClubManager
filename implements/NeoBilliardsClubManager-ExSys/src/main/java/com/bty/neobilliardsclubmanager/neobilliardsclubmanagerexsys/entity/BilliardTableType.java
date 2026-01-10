package com.bty.neobilliardsclubmanager.neobilliardsclubmanagerexsys.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "BilliardTableType")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BilliardTableType {

    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "Name", columnDefinition = "NVARCHAR(50)", length = 50, nullable = false, unique = true)
    String name;

    @Column(name = "PricePerHour", nullable = false)
    Long pricePerHour;

    @Column(name = "Description", columnDefinition = "NVARCHAR(3000)", length = 3000, nullable = true)
    String description;

    @OneToMany(mappedBy = "billiardTableType", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<BilliardTable> billiardTables = new ArrayList<>();
}
