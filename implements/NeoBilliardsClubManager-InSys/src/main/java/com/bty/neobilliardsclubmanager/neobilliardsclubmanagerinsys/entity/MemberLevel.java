package com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "MemberLevel")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MemberLevel {

    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "Name", columnDefinition = "NVARCHAR(50)", length = 50, nullable = false, unique = true)
    String name;

    @Column(name = "RequiredPlaytimeHours", nullable = false, unique = true)
    Long requiredPlaytimeHours;

    @Column(name = "BillTotalDiscountPercentage", nullable = false)
    Integer billTotalDiscountPercentage;

    @OneToMany(mappedBy = "memberLevel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Member> members = new ArrayList<>();
}
