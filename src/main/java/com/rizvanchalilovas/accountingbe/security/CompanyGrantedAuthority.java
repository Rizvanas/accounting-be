package com.rizvanchalilovas.accountingbe.security;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

@Data
@EqualsAndHashCode
public class CompanyGrantedAuthority implements GrantedAuthority {

    private final String name;
    private final Long companyId;

    public CompanyGrantedAuthority(String name, Long companyId) {
        this.name = name;
        this.companyId = companyId;
    }

    @Override
    public String getAuthority() {
        return name + ":companyId:" + companyId;
    }
}
