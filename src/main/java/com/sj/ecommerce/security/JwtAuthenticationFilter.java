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

    private final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Extract the Authorization header
        String requestHeader = request.getHeader("Authorization");
        System.out.println("requestHeader = " + requestHeader);

        String username = null;
        String token = null;

        System.out.println("request = " + request);
        // Check if the token is present and starts with "Bearer "
        if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
            token = requestHeader.substring(7); // Extract the token
            System.out.println("token = " + token);
            try {
                username = jwtHelper.getUsernameFromToken(token); // Get username from the token
                System.out.println("token = " + token);
            } catch (Exception e) {
                logger.error("Error while parsing token: {}", e.getMessage());
            }
        } else {
            logger.warn("Invalid Authorization header");
        }

        // Validate the token and set the authentication in the security context
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Validate the token using the helper method
            if (jwtHelper.validateToken(token, userDetails)) {
                // Extract roles from the token and map them to GrantedAuthority
                List<GrantedAuthority> authorities = jwtHelper.getRolesFromToken(token).stream()
                        .map(SimpleGrantedAuthority::new)  // Create authorities from role strings
                        .collect(Collectors.toList());

                // Log the authorities for debugging
                logger.info("Authorities for user {}: {}", username, authorities);

                // Create authentication object with the user details and authorities
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Set the authentication in the security context
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                logger.warn("Token validation failed for user: {}", username);
            }
        }

        filterChain.doFilter(request, response);  // Continue filter chain
    }
}
