package com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.config;

import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.entity.Account;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.entity.EmployeeProfile;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.repository.AccountRepository;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.repository.EmployeeProfileRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class DataInit implements CommandLineRunner {

    @Value("${account.admin.username}")
    private String ADMIN_USERNAME;

    @Value("${account.admin.password}")
    private String ADMIN_PASSWORD;

    private AccountRepository accountRepository;

    private PasswordEncoder passwordEncoder;

    private EmployeeProfileRepository employeeProfileRepository;

    public DataInit(AccountRepository accountRepository,
                    PasswordEncoder passwordEncoder,
                    EmployeeProfileRepository employeeProfileRepository) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.employeeProfileRepository = employeeProfileRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if(!accountRepository.existsByUsername(ADMIN_USERNAME)) {
            Account account = Account.builder()
                    .username(ADMIN_USERNAME)
                    .password(passwordEncoder.encode(ADMIN_PASSWORD))
                    .role(1)
                    .isLocked(false)
                    .createdAt(LocalDateTime.now())
                    .build();
            accountRepository.save(account);
        }

        if(!accountRepository.existsByUsername("employee")) {
            Account account = Account.builder()
                    .username("employee")
                    .password(passwordEncoder.encode("employee"))
                    .role(2)
                    .isLocked(false)
                    .createdAt(LocalDateTime.now())
                    .build();

            EmployeeProfile employeeProfile = EmployeeProfile
                    .builder()
                    .fullName("Phạm Ngọc Mộng Phước")
                    .isMale(true)
                    .dateOfBirth(LocalDate.of(2004, 3, 15))
                    .address("Hue")
                    .phoneNumber("0782730023")
                    .citizenIDNumber("046204008838")
                    .email("p@gmail.com")
                    .imagePath(null)
                    .build();

            account.setEmployeeProfile(employeeProfile);
            employeeProfile.setAccount(account);

            accountRepository.save(account);
        }
    }
}
