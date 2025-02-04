package com.sj.ecommerce.controller;


//import io.swagger.annotations.Api;

import com.sj.ecommerce.dto.*;
import com.sj.ecommerce.entity.User;
import com.sj.ecommerce.exceptions.BadApiRequestException;
import com.sj.ecommerce.repository.UserRepository;
import com.sj.ecommerce.security.JwtHelper;
import com.sj.ecommerce.service.UserService;
import com.sj.ecommerce.serviceImpl.TokenBlacklistService;
import io.swagger.annotations.Api;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
@Api(value = "AuthController", description = "APIs for Authentication!!")
public class AuthController {

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private AuthenticationManager manager;
    @Autowired
    private JwtHelper helper;

    @Autowired
    private TokenBlacklistService tokenBlacklistService;


    @Autowired
    private UserRepository userRepository;

    private Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request) {
        // Authenticate the user
        this.doAuthenticate(request.getEmail(), request.getPassword());

        // Load user details
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        System.out.println("userDetails = " + userDetails);

        // Generate JWT token
        String token = this.helper.generateToken(userDetails);
        String refreshToken = this.helper.generateRefreshToken(userDetails);

        // Retrieve user from the database
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + request.getEmail()));

        // Map User to UserDto
        UserDto userDto = UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .roles(user.getRoles().stream()  // Convert Set<Role> to Set<RoleDto>
                        .map(role -> new RoleDto(role.getRoleId(), role.getRoleName()))
                        .collect(Collectors.toSet()))
                .build();

        // Create the JWT response
        JwtResponse response = JwtResponse.builder()
                .jwtToken(token)
                .refreshToken(refreshToken)
                .user(userDto)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    private void doAuthenticate(String email, String password) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, password);
        try {
            manager.authenticate(authentication);
        } catch (BadCredentialsException e) {
            throw new BadApiRequestException(" Invalid Username or Password  !!");
        }
    }


    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");

        if (refreshToken == null || refreshToken.isEmpty()) {
            return ResponseEntity.badRequest().body("Refresh token is required");
        }

        // Validate the refresh token
        if (!helper.validateRefreshToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired refresh token");
        }

        // Extract the username from the refresh token
        String username = helper.getUsernameFromRefreshToken(refreshToken);

        // Load user details and generate a new access token
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        String newAccessToken = helper.generateToken(userDetails);

        Map<String, String> response = new HashMap<>();
        response.put("accessToken", newAccessToken);

        return ResponseEntity.ok(response);
    }


    // LOGOUT API
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authHeader) {
        // Check if the token is present in the Authorization header
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or missing Authorization header");
        }

        // Extract the token
        String token = authHeader.substring(7);

        // Option 1: If you have a token blacklist mechanism (e.g., database or in-memory list), invalidate it here
        // Example: Add the token to a blacklist (future requests with this token will be rejected)
        tokenBlacklistService.addToBlacklist(token);

        // Option 2: If you rely on client-side token removal, simply respond with a success message
        return ResponseEntity.ok("Logout successful. Please discard your token.");
    }


    @GetMapping("/current")
    public ResponseEntity<UserDto> getCurrentUser(Principal principal) {
        String name = principal.getName();
        return new ResponseEntity<>(modelMapper.map(userDetailsService.loadUserByUsername(name), UserDto.class), HttpStatus.OK);
    }

}
