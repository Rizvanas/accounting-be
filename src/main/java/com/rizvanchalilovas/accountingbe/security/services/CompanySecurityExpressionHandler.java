package com.rizvanchalilovas.accountingbe.security.services;

import com.rizvanchalilovas.accountingbe.repositories.CategoryJpaRepository;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;

public class CompanySecurityExpressionHandler extends DefaultMethodSecurityExpressionHandler {
    private final AuthenticationTrustResolver trustResolver =
            new AuthenticationTrustResolverImpl();

    private final CategoryJpaRepository categoryRepository;

    public CompanySecurityExpressionHandler(CategoryJpaRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    protected MethodSecurityExpressionOperations createSecurityExpressionRoot(
            Authentication authentication, MethodInvocation invocation) {

        CompanySecurityExpressionRoot root = new CompanySecurityExpressionRoot(authentication, categoryRepository);

        root.setPermissionEvaluator(getPermissionEvaluator());
        root.setTrustResolver(this.trustResolver);
        root.setRoleHierarchy(getRoleHierarchy());

        return root;
    }
}
