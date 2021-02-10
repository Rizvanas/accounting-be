package com.rizvanchalilovas.accountingbe.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

@Getter
public class CompanyGrantedAuthority implements GrantedAuthority {

    private final String name;
    private final Long companyId;

    public CompanyGrantedAuthority(String name, Long companyId) {
        this.name = name;
        this.companyId = companyId;
    }

    @Override
    public String getAuthority() {
        return name;
    }
}
