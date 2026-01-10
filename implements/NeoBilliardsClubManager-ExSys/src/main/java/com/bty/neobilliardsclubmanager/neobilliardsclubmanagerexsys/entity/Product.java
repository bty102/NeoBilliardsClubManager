package com.bty.neobilliardsclubmanager.neobilliardsclubmanagerexsys.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Product")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Product {

    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "Name", columnDefinition = "NVARCHAR(250)", length = 250, nullable = false, unique = true)
    String name;

    @Column(name = "Price", nullable = false)
    Long price;

    @Column(name = "Description", columnDefinition = "NVARCHAR(3000)", length = 3000,nullable = true)
    String description;

    @Column(name = "IsLocked", nullable = false)
    Boolean isLocked;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<BillDetail> billDetails = new ArrayList<>();
}
