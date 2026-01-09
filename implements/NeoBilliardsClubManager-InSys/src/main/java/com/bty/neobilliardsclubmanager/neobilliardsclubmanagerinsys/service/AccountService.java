package com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.service;

import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.dto.request.EmployeeProfileCreationRequest;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.dto.response.AccountResponse;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.entity.Account;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.exception.AccountCreationException;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.exception.AccountNotFoundException;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.exception.EmployeeAccountLockingException;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.mapper.AccountMapper;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.repository.AccountRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountService {

    final AccountRepository accountRepository;
    final AccountMapper accountMapper;
    final PasswordEncoder passwordEncoder;
    final EmployeeProfileService employeeProfileService;


    @PreAuthorize("hasRole(T(com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.constant.Role).ADMIN.name())")
    public List<AccountResponse> getAllEmployees() {
        List<Account> employees = accountRepository.findByRole(2);
        return employees
                .stream()
                .map(account -> accountMapper.toAccountResponse(account))
                .toList();
    }

    @PreAuthorize("hasRole(T(com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.constant.Role).ADMIN.name())")
    public List<AccountResponse> findEmployeesByFullNameContainingOrAddressContaining(String key) {
        List<Account> employees = accountRepository.searchEmployee(2, key);
        return employees
                .stream()
                .map(account -> accountMapper.toAccountResponse(account))
                .toList();
    }

    @PreAuthorize("hasRole(T(com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.constant.Role).ADMIN.name())")
    @Transactional(rollbackFor = Exception.class)
    public void createEmployeeAccount(String username, String rawPassword, EmployeeProfileCreationRequest request) {
        if(accountRepository.existsByUsername(username)) {
            throw new AccountCreationException("Username đã tồn tại");
        }
        Account account = Account.builder()
                .username(username)
                .password(passwordEncoder.encode(rawPassword))
                .role(2)
                .isLocked(false)
                .createdAt(LocalDateTime.now())
                .build();
        account = accountRepository.save(account);
        request.setAccountId(account.getId());
        employeeProfileService.createEmployeeProfile(request);
    }

    @PreAuthorize("hasRole(T(com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.constant.Role).ADMIN.name())")
    public void lockEmployeeAccount(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> {throw new AccountNotFoundException("Không tìm thấy tài khoản");
                });
        if(!account.getRole().equals(2)) {
            throw new EmployeeAccountLockingException("Không phải tài khoản nhân viên");
        }
        account.setIsLocked(true);
        accountRepository.save(account);
    }

    @PreAuthorize("hasRole(T(com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.constant.Role).ADMIN.name())")
    public void unlockEmployeeAccount(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> {throw new AccountNotFoundException("Không tìm thấy tài khoản");
                });
        if(!account.getRole().equals(2)) {
            throw new EmployeeAccountLockingException("Không phải tài khoản nhân viên");
        }
        account.setIsLocked(false);
        accountRepository.save(account);
    }
}
