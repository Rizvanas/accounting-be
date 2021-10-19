package com.rizvanchalilovas.accountingbe.security.services;

import com.rizvanchalilovas.accountingbe.models.Category;
import com.rizvanchalilovas.accountingbe.repositories.CategoryJpaRepository;
import com.rizvanchalilovas.accountingbe.security.CompanyGrantedAuthority;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

import java.util.Arrays;

public class CompanySecurityExpressionRoot
        extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {

    private final CategoryJpaRepository categoryRepository;

    public CompanySecurityExpressionRoot(Authentication authentication, CategoryJpaRepository categoryRepository) {
        super(authentication);
        this.categoryRepository = categoryRepository;
    }

    public boolean hasRequiredPermissions(Long companyId, String... permissions) {
        if (companyId == null) {
            throw new IllegalArgumentException("[companyId]: cannot be null");
        }

        if (permissions == null) {
            throw new IllegalArgumentException("[permissions]: cannot be null");
        }

        return hasPrivileges(companyId, permissions);
    }

    public boolean isResponsibleUser(Long companyId, Long categoryId) {
        Category category = null;
        try {
            category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new NotFoundException("Category could not be found"));

            if (category.getManager() == null) {
                return true;
            }

            var loggedInEmail = authentication.getName();
            var categoryManagerEmail = category.getManager().getUser().getEmail();

            return category.getCompany().getId().equals(companyId) &&
                    loggedInEmail.equals(categoryManagerEmail);

        } catch (NotFoundException e) {
            return false;
        }
    }

    @Override
    public void setFilterObject(Object o) {
    }

    @Override
    public Object getFilterObject() {
        return null;
    }

    @Override
    public void setReturnObject(Object o) {

    }

    @Override
    public Object getReturnObject() {
        return null;
    }

    @Override
    public Object getThis() {
        return null;
    }

    private boolean hasPrivileges(long companyId, String... permissions) {

        var authorities = authentication.getAuthorities();

        for (var authority : authorities) {
            if (!(authority instanceof CompanyGrantedAuthority)) continue;

            var companyAuthority = ((CompanyGrantedAuthority) authority);

            if (companyAuthority.getCompanyId() != companyId) continue;

            if (Arrays.stream(permissions).anyMatch(p -> p.equalsIgnoreCase(companyAuthority.getName()))) {
                return true;
            }
        }

        return false;
    }
}