package com.rizvanchalilovas.accountingbe.config;

import com.rizvanchalilovas.accountingbe.repositories.CategoryJpaRepository;
import com.rizvanchalilovas.accountingbe.security.CompanyPermissionEvaluator;
import com.rizvanchalilovas.accountingbe.security.services.CompanySecurityExpressionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {

    @Autowired
    private CategoryJpaRepository categoryRepository;

    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        CompanySecurityExpressionHandler expressionHandler =
                new CompanySecurityExpressionHandler(categoryRepository);
        expressionHandler.setPermissionEvaluator(new CompanyPermissionEvaluator());
        return expressionHandler;
    }
}