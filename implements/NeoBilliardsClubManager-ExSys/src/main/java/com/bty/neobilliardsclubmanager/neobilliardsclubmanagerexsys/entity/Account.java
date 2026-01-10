package com.bty.neobilliardsclubmanager.neobilliardsclubmanagerexsys.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Account")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Account {

    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "Username", columnDefinition = "NVARCHAR(50)", length = 50, nullable = false, unique = true)
    String username;

    @Column(name = "Password", columnDefinition = "NVARCHAR(3000)", length = 3000, nullable = false)
    String password;

    @Column(name = "Role", nullable = false)
    Integer role;

    @Column(name = "IsLocked", nullable = false)
    Boolean isLocked;

    @Column(name = "CreatedAt", nullable = false)
    LocalDateTime createdAt;

    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
    EmployeeProfile employeeProfile;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Bill> bills = new ArrayList<>();
}
