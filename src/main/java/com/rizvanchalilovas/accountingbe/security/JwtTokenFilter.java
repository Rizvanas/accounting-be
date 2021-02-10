package com.rizvanchalilovas.accountingbe.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtTokenFilter extends GenericFilterBean {

    private final JwtTokenProvider tokenProvider;

    @Autowired
    public JwtTokenFilter(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        String token = tokenProvider.resolveToken((HttpServletRequest) request);

        try {
            if (token != null && tokenProvider.tokenIsValid(token)) {
                Authentication authentication = tokenProvider.getAuthentication(token);

                if (authentication != null) {
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (JwtAuthenticationException ex) {
            SecurityContextHolder.clearContext();
            ((HttpServletResponse) response).sendError(ex.getHttpStatus().value());

            throw new JwtAuthenticationException("JWT token is invalid or expired.");
        }

        filterChain.doFilter(request, response);
    }
}
