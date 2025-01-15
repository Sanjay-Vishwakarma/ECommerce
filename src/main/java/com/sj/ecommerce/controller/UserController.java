package com.sj.ecommerce.controller;

import com.sj.ecommerce.dto.*;
import com.sj.ecommerce.dto.Response;
import com.sj.ecommerce.request.LoginRequest;
import com.sj.ecommerce.service.CartService;
import com.sj.ecommerce.service.OrderService;
import com.sj.ecommerce.service.ProductService;
import com.sj.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    private OrderService orderService;


    // 1. Register User
    @PostMapping("/register")
    Response<UserDto> createUser(@RequestBody UserDto userDto) {
        return userService.saveUser(userDto);
    }

    // 2. Login User
    @PostMapping("/login")
    Response<?> loginUser(@RequestBody LoginRequest loginRequest) {
        return null;
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

    // 5. Get All Products
    @GetMapping("/products/getAllProducts")
    public PageableResponse<ProductDto> getAllProducts(
            @RequestParam(value = "pageNumber", defaultValue = "1", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "name", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {
        return productService.getAllProducts(pageNumber, pageSize, sortBy, sortDir);
    }

    // 6. Get Product by ID
    @GetMapping("/products/{productId}")
    public Response<ProductDto> getProductById(@PathVariable String productId) {
        return productService.getProductById(productId);
    }

    // 7. Add Product to Cart
    @PostMapping("/cart/addCart")
    public Response<CartDto> addToCart(@RequestBody CartDto cartDto) {
        return cartService.addToCart(cartDto);
    }

    // 8. Get Cart
    @GetMapping("/cart/getCart/{userId}")
    public Response<CartDto> getCart(@PathVariable String userId) {
        return cartService.getCart(userId);
    }

    // 9. Update Product Quantity in Cart
    @PutMapping("/cart/update/{productId}")
    public Response<CartDto> updateCartProductQuantity(
            @PathVariable String productId, @RequestBody CartDto request) {
        Response<CartDto> response = new Response<>();
        if (request.getCartItems() == null || request.getCartItems().isEmpty()) {
            response.setMessage("No items found");
            return response;
        }
        Integer quantity = request.getCartItems().getFirst().getQuantity();
        return cartService.updateCartProductQuantity(productId, quantity);
    }

    // 10. Remove Product from Cart
    @DeleteMapping("/cart/delete/{productId}")
    public ResponseEntity<Response<String>> removeFromCart(@PathVariable String productId, @RequestParam String userId) {
        try {
            // Call the service layer to remove the product from the cart
            Response<String> response = cartService.removeFromCart(userId, productId);
            // Return success response
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            // Handle exceptions and return error response
            Response<String> errorResponse = new Response<>();
            errorResponse.setStatus("error");
            errorResponse.setMessage("Failed to remove product from cart: " + e.getMessage());
            errorResponse.setData(null);
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 11. Place Order
    @PostMapping("/orders/placeOrder")
    public ResponseEntity<Response<OrderDto>> placeOrder(@RequestParam String userId) {
        try {
            // Call the service layer to place the order
            Response<OrderDto> orderDtoResponse = orderService.placeOrder(userId);
            return new ResponseEntity<>(orderDtoResponse, HttpStatus.CREATED);
        } catch (Exception e) {
            // Handle exceptions and return an error response
            Response<OrderDto> errorResponse = new Response<>();
            errorResponse.setStatus("error");
            errorResponse.setMessage("Failed to place order: " + e.getMessage());
            errorResponse.setData(null);
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 12. Get Order History
    @GetMapping("/orders/history")
    public PageableResponse<OrderDto> getOrderHistory(@RequestParam String userId,
                                                      @RequestParam(value = "pageNumber", defaultValue = "1", required = false) int pageNumber,
                                                      @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
                                                      @RequestParam(value = "sortBy", defaultValue = "name", required = false) String sortBy,
                                                      @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir) {
       return  orderService.getOrderHistory(userId,pageNumber, pageSize, sortBy, sortDir);

    }

    // 13. Get Order by ID
    @GetMapping("/orders/{getOrderById}")
    public Response<OrderDto> getOrderById(@PathVariable String getOrderById) {
        return orderService.getOrderById(getOrderById);
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
