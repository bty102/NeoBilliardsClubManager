package com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Table(name = "EmployeeProfile")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmployeeProfile {

    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "FullName", columnDefinition = "NVARCHAR(250)", length = 250, nullable = false)
    String fullName;

    @Column(name = "IsMale", nullable = false)
    Boolean isMale;

    @Column(name = "DateOfBirth", nullable = false)
    LocalDate dateOfBirth;

    @Column(name = "Address", columnDefinition = "NVARCHAR(250)", length = 250, nullable = false)
    String address;

    @Column(name = "PhoneNumber", columnDefinition = "VARCHAR(15)", length = 15, nullable = false, unique = true)
    String phoneNumber;

    @Column(name = "CitizenIDNumber", columnDefinition = "CHAR(12)", length = 12, nullable = false, unique = true)
    String citizenIDNumber;

    @Column(name = "Email", columnDefinition = "VARCHAR(254)", length = 254, nullable = false, unique = true)
    String email;

    @Column(name = "ImagePath", columnDefinition = "NVARCHAR(3000)", length = 3000, nullable = true)
    String imagePath;

    @OneToOne
    @JoinColumn(name = "AccountId", unique = true, nullable = false)
    Account account;

}
