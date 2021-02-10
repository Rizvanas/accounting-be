package com.rizvanchalilovas.accountingbe.security;

import com.rizvanchalilovas.accountingbe.models.CompanyEmployee;
import com.rizvanchalilovas.accountingbe.models.Role;
import com.rizvanchalilovas.accountingbe.models.Status;
import com.rizvanchalilovas.accountingbe.models.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class SecurityUser implements UserDetails {

    private final String username;
    private final String password;
    private final List<GrantedAuthority> authorities;
    private final Boolean isActive;

    public SecurityUser(String username, String password, List<GrantedAuthority> authorities, Boolean isActive) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.isActive = isActive;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isActive;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isActive;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isActive;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }

    public static UserDetails fromUser(User user) {
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.getStatus().equals(Status.ACTIVE),
                user.getStatus().equals(Status.ACTIVE),
                user.getStatus().equals(Status.ACTIVE),
                user.getStatus().equals(Status.ACTIVE),
                getAuthorities(user)
        );
    }

    private static Collection<? extends GrantedAuthority> getAuthorities(User user) {
        return getGrantedAuthorities(user.getJobs());
    }


    private static List<CompanyGrantedAuthority> getGrantedAuthorities(Set<CompanyEmployee> jobs) {
        return jobs.stream()
                .flatMap(job -> job.getRole().getPermissions().stream()
                        .map(perm -> new CompanyGrantedAuthority(perm.getName(), job.getCompany().getId())))
                .collect(Collectors.toList());
    }
}
