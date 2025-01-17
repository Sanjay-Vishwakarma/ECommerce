package com.sj.ecommerce.controller;

import com.sj.ecommerce.dto.*;
import com.sj.ecommerce.dto.Response;
import com.sj.ecommerce.request.LoginRequest;
import com.sj.ecommerce.service.CartService;
import com.sj.ecommerce.service.OrderService;
import com.sj.ecommerce.service.ProductService;
import com.sj.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
//@PreAuthorize("hasRole('USER')")
public class UserController {


    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CartService cartService;


    // 1. Register User
    @PostMapping("/register")
    Response<UserDto> createUser(@RequestBody UserDto userDto) {
        return userService.saveUser(userDto);
    }


    // 3. Get User Profile
    @GetMapping("/profile/{getUserById}")
    public Response<UserDto> getUserProfile(@PathVariable String getUserById) {
        return userService.getUserProfile(getUserById);
    }

    // 4. Update User Profile
    @PutMapping("/profile/update/{getUserById}")
    public Response<UserDto> updateUserProfile(@PathVariable String getUserById, @RequestBody UserDto userDTO) {
        return userService.updateUserProfile(getUserById, userDTO);
    }

    // admin
    @DeleteMapping("/delete/{userId}")
    Response<String> deleteUser(@PathVariable String userId) {
        return userService.deleteUser(userId);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getAllUsers")
    PageableResponse<UserDto> getAllUsers(
            @RequestParam(value = "pageNumber", defaultValue = "1", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "name", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {
        return userService.getAllUser(pageNumber, pageSize, sortBy, sortDir);
    }


}
