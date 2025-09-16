package com.quantumsoft.hrms.Human_Resource_Website.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtTokenFilter.class);

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String authHeader = request.getHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                log.debug("No JWT token found in Authorization header.");
                filterChain.doFilter(request, response);
                return;
            }

            String token = authHeader.substring(7);
            log.debug("JWT token extracted: {}", token);

            if (!jwtTokenUtil.isTokenValid(token)) {
                log.warn("JWT token is not valid.");
                filterChain.doFilter(request, response);
                return;
            }

            String username = jwtTokenUtil.extractUsername(token);
            String role = jwtTokenUtil.extractRole(token);
            log.info("Token is valid. Username: {}, Role: {}", username, role);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                var authorities = jwtTokenUtil.getAuthorities(token);
                log.info("Loaded user details. Authorities: {}", authorities);

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, authorities
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                log.info("SecurityContext updated with authenticated user: {}", username);
            }
        } catch (Exception e) {
            log.error("Error in JWT token filter", e);
        }

        filterChain.doFilter(request, response);
    }
}
