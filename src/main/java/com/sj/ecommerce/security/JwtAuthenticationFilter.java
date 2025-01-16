package com.sj.ecommerce.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        logger.info("Processing request URI: {}", requestURI);

        // Skip filtering for public paths
        if (isPublicPath(requestURI)) {
            logger.debug("Public path, skipping filter for URI: {}", requestURI);
            filterChain.doFilter(request, response);
            return;
        }

        // Existing token validation logic...
        String requestHeader = request.getHeader(AUTH_HEADER);
        String username = null;
        String token = null;

        if (requestHeader != null && requestHeader.startsWith(BEARER_PREFIX)) {
            token = requestHeader.substring(BEARER_PREFIX.length());
            logger.debug("Extracted token: {}", token);

            try {
                username = jwtHelper.getUsernameFromToken(token);
            } catch (Exception e) {
                logger.error("Error while parsing token", e);
            }
        } else {
            logger.warn("Invalid or missing Authorization header");
        }

        // Token validation and setting SecurityContext
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtHelper.validateToken(token, userDetails)) {
                List<GrantedAuthority> authorities = jwtHelper.getRolesFromToken(token).stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

                logger.info("Authenticated user: {} with roles: {}", username, authorities);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                logger.warn("Token validation failed for user: {}", username);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid or expired token");
                response.getWriter().flush();
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private boolean isPublicPath(String requestURI) {
        return requestURI.startsWith("/swagger-ui") ||
                requestURI.startsWith("/v3/api-docs") ||
                requestURI.startsWith("/swagger-resources") ||
                requestURI.startsWith("/webjars") ||
                requestURI.startsWith("/v2/api-docs");
    }

}
