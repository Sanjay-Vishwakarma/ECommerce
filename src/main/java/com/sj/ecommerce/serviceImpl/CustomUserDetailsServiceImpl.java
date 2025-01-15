package com.sj.ecommerce.serviceImpl;

import com.sj.ecommerce.entity.User;
import com.sj.ecommerce.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class CustomUserDetailsServiceImpl implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsServiceImpl.class);  // Logger

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));

        // Log the roles as a comma-separated string
        logger.info("Loaded user: () with roles: () ", user.getEmail(),
                user.getRoles().stream()
                        .map(role -> role.getRoleName())  // Get roleName for each role
                        .collect(Collectors.joining(", ")));  // Join roles in a string

        return new CustomUserDetails(user);  // Return the custom user details
    }
}
