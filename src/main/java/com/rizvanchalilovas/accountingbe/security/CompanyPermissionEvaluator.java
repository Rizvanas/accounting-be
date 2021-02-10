package com.rizvanchalilovas.accountingbe.security;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Arrays;

@Component
public class CompanyPermissionEvaluator implements PermissionEvaluator {

    @Override
    public boolean hasPermission(Authentication auth, Object id, Object permission) {
        if ((auth == null) || id == null || permission == null) {
            return false;
        }

        var companyId = Long.parseLong(id.toString());

        return hasPrivileges(auth, companyId, permission.toString());
    }

    @Override
    public boolean hasPermission(
            Authentication auth,
            Serializable companyId,
            String targetType,
            Object permission
    ) {
        if ((auth == null) || (targetType == null) || companyId == null || !(permission instanceof String)) {
            return false;
        }

        return hasPrivileges(auth, (long) companyId, permission.toString());
    }

    public boolean hasAnyPermissions(
            Authentication auth,
            Serializable companyId,
            String targetType,
            String... permissions
    ) {
        if ((auth == null) || (targetType == null) || companyId == null || permissions == null) {
            return false;
        }

        return hasPrivileges(auth, (long) companyId, permissions);
    }

    public boolean hasAnyPermissions(
            Authentication auth,
            String... permissions
    ) {
        if ((auth == null) || permissions == null) {
            return false;
        }

        return false;
    }



    private boolean hasPrivileges(Authentication auth, long companyId, String... permissions) {

        var authorities = auth.getAuthorities();

        for (var authority : authorities) {
            if (!(authority instanceof CompanyGrantedAuthority)) continue;

            var companyAuthority = ((CompanyGrantedAuthority) authority);

            if (companyAuthority.getCompanyId() != companyId) continue;

            if (Arrays.stream(permissions).anyMatch(p -> p.equalsIgnoreCase(companyAuthority.getAuthority()))) {
                return true;
            }
        }

        return false;
    }
}
