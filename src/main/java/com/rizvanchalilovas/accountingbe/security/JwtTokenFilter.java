package com.rizvanchalilovas.accountingbe.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private final ObjectMapper mapper;
    private final JwtTokenProvider tokenProvider;

    @Autowired
    public JwtTokenFilter(ObjectMapper mapper, JwtTokenProvider tokenProvider) {
        this.mapper = mapper;
        this.tokenProvider = tokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {


        String token = tokenProvider.resolveToken(request);

        try {
            if (tokenProvider.tokenIsValid(token)) {
                Authentication authentication = tokenProvider.getAuthentication(token);

                if (authentication != null) {
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
            filterChain.doFilter(request, response);
        } catch (JwtAuthenticationException ex) {
            SecurityContextHolder.clearContext();

            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(ex.getHttpStatus().value());

            Map<String, Object> errorDetails = new HashMap<>();
            errorDetails.put("message", "Invalid token");
            errorDetails.put("status", ex.getHttpStatus().value());

            mapper.writeValue(response.getWriter(), errorDetails);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        var path = request.getRequestURI();

        return path.equals("/api/auth/login") ||
                path.equals("/api/auth/register");
    }
}
