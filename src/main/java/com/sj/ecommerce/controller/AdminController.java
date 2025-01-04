package com.sj.ecommerce.controller;

import com.sj.ecommerce.dto.*;
import com.sj.ecommerce.reponse.Response;
import com.sj.ecommerce.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
//@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private InventoryService inventoryService;



   /* User Management:
    GET /admin/users – Get a list of all users.
    GET /admin/users/{userId} – Get details of a specific user.
    DELETE /admin/users/{userId} – Delete a user.
    PUT /admin/users/{userId} – Update user roles or status.*/

    @GetMapping("/users/getAllUsers")
    Response<List<UserDto>> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/users/{userId}")
    Response<UserDto> getUser(@PathVariable String userId) {
        return userService.getUserProfile(userId);
    }

    @DeleteMapping("/users/{userId}")
    Response<String> deleteUser(@PathVariable String userId) {
        return userService.deleteUser(userId);
    }

   /* Category Management:
    POST /admin/categories – Add a new category.
    PUT /admin/categories/{categoryId} – Update an existing category.
    DELETE /admin/categories/{categoryId} – Remove a category.*/


    @PostMapping("/categories/addCategory")
    Response<CategoryDto> addCategory(@RequestBody CategoryDto categoryDto) {
        return categoryService.addCategory(categoryDto);
    }

    @PutMapping("/categories/getCategory/{categoryId}")
    Response<CategoryDto> updateCategory(@RequestBody CategoryDto categoryDto, @PathVariable String categoryId) {
        return categoryService.updateCategory(categoryDto, categoryId);
    }

    @DeleteMapping("/categories/deleteCategory/{categoryId}")
    Response<String> deleteCategory(@PathVariable String categoryId) {
        return categoryService.deleteCategory(categoryId);
    }

    /*
    * Product Management:
        POST /admin/products – Add a new product.
        PUT /admin/products/{productId} – Update product details.
        DELETE /admin/products/{productId} – Delete a product.*/

    // Add a new product
    @PostMapping("/products/addProduct")
    public Response<ProductDto> addProduct(@RequestBody ProductDto productDto) {
        return productService.addProduct(productDto);
    }

    // Update product details
    @PutMapping("/products/{productId}")
    public Response<ProductDto> updateProduct(
            @PathVariable String productId,
            @RequestBody ProductDto productDto) {
        return productService.updateProduct(productId, productDto);
    }

    // Delete a product
    @DeleteMapping("/products/{productId}")
    public Response<String> deleteProduct(@PathVariable String productId) {
        return productService.deleteProduct(productId);
    }


    /* Order Management:
        GET /admin/orders – Get all orders.
        PUT /admin/orders/{orderId} – Update order status (e.g., "Shipped", "Delivered").
        DELETE /admin/orders/{orderId} – Cancel an order.*/

    // Get all orders
    @GetMapping("/orders/getAllOrders")
    public Response<List<OrderDto>> getAllOrders() {
        return orderService.getAllOrders();
    }

    // Update order status
    @PutMapping("/orders/{orderId}")
    public Response<OrderDto> updateOrderStatus(@PathVariable String orderId, @RequestParam String status) {
        return orderService.updateOrderStatus(orderId, status);
    }

    // Delete an order
    @DeleteMapping("/orders/{orderId}")
    public Response<String> deleteOrder(@PathVariable String orderId) {
       return orderService.deleteOrder(orderId);
    }

    /*
    *  Inventory Management:
        POST /admin/inventory – Add inventory for a product.
        PUT /admin/inventory/{productId} – Update inventory stock.
        DELETE /admin/inventory/{productId} – Delete inventory for a product.*/

    // Add inventory
    @PostMapping("/inventory/addProduct")
    public Response<InventoryDto> addInventory(@RequestBody InventoryDto inventoryDto) {
        return inventoryService.addInventory(inventoryDto);
    }

    // Update inventory stock
    @PutMapping("/inventory/{productId}")
    public Response<InventoryDto> updateInventoryStock(
            @PathVariable String productId, @RequestBody InventoryDto inventoryDto) {
       return inventoryService.updateInventoryStock(productId, inventoryDto);

    }

    // Delete inventory
    @DeleteMapping("/inventory/{productId}")
    public Response<String> deleteInventory(@PathVariable String productId) {
        return inventoryService.deleteInventory(productId);
    }

    /*
    * Notification Management: pending
        POST /admin/notifications – Broadcast a notification to all users.*/

}
