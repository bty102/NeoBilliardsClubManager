package com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.security;

import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.constant.Role;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.entity.Account;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.entity.EmployeeProfile;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    private Long id;
    private String username;
    private String password;
    private Boolean isLocked;
    private EmployeeProfile employeeProfile;
    private Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(Account account) {
        this.id = account.getId();
        this.username = account.getUsername();
        this.password = account.getPassword();
        this.isLocked = account.getIsLocked();
        this.employeeProfile = account.getEmployeeProfile();
        List<GrantedAuthority> authorities = new ArrayList<>();
        if(account.getRole().equals(1)) {
            // ADMIN
            authorities.add(new SimpleGrantedAuthority("ROLE_" + Role.ADMIN.name()));
        } else if (account.getRole().equals(2)) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + Role.EMPLOYEE.name()));
        } else {

        }
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public @Nullable String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.isLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Long getId() {
        return id;
    }

    public EmployeeProfile getEmployeeProfile() {
        return employeeProfile;
    }
}
