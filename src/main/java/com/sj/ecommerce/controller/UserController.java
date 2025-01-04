package com.sj.ecommerce.controller;

import com.sj.ecommerce.dto.ProductDto;
import com.sj.ecommerce.reponse.Response;
import com.sj.ecommerce.dto.UserDto;
import com.sj.ecommerce.request.LoginRequest;
import com.sj.ecommerce.service.ProductService;
import com.sj.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/user")
//@PreAuthorize("hasRole('USER')")
public class UserController {


    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    Response<UserDto> resp = null;

    // 1. Register User
    @PostMapping("/register")
    Response<UserDto> createUser(@RequestBody UserDto userDto) {
        return resp = userService.saveUser(userDto);
    }

    // 2. Login User
    @PostMapping("/login")
    Response<?> loginUser(@RequestBody LoginRequest loginRequest) {
        return null;
    }

    // 3. Get User Profile
    @GetMapping("/profile/{id}")
    public Response<UserDto> getUserProfile(@PathVariable String id) {
        return resp = userService.getUserProfile(id);
    }

    // 4. Update User Profile
    @PutMapping("/profile/{id}")
    public Response<UserDto> updateUserProfile(@PathVariable String id,@RequestBody UserDto userDTO) {
        return resp = userService.updateUserProfile(id,userDTO);
    }

    // 5. Get All Products
    @GetMapping("/products/getAllProducts")
    public Response<List<ProductDto>> getAllProducts() {
        return productService.getAllProducts();
    }

}
