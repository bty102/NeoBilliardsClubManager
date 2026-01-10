package com.bty.neobilliardsclubmanager.neobilliardsclubmanagerexsys.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Member")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Member {

    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "FullName", columnDefinition = "NVARCHAR(250)", length = 250, nullable = false)
    @Size(min = 1, max = 250, message = "Tên hội viên ít nhất là 1 và nhiều nhất là 250 ký tự")
    String fullName;

    @Column(name = "IsMale", nullable = false)
    Boolean isMale;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "DateOfBirth", nullable = false)
    LocalDate dateOfBirth;

    @Column(name = "Email", columnDefinition = "VARCHAR(254)", length = 254, nullable = false, unique = true)
    @Email(message = "Email không hợp lệ")
    String email;

    @Column(name = "ImagePath", columnDefinition = "NVARCHAR(3000)", length = 3000, nullable = true)
    String imagePath;

    @Column(name = "Username", columnDefinition = "NVARCHAR(50)", length = 50, nullable = false, unique = true)
    @Size(min = 3, max = 50, message = "Tên đăng nhập ít nhất là 3 và nhiều nhất là 50 ký tự")
    String username;

    @Column(name = "Password", columnDefinition = "NVARCHAR(3000)", length = 3000, nullable = false)
    String password;

    @Column(name = "IsLocked", nullable = false)
    Boolean isLocked;

    @Column(name = "CreatedAt", nullable = false)
    LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "LevelId", nullable = true)
    MemberLevel memberLevel;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Bill> bills = new ArrayList<>();

}
