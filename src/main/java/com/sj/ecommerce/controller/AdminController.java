package com.sj.ecommerce.controller;

import com.sj.ecommerce.dto.*;
import com.sj.ecommerce.dto.Response;
import com.sj.ecommerce.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @GetMapping("/system")
    public String system() {
        return "I'm a  admin.";
    }

    @GetMapping("/users/{userId}")
    Response<UserDto> getUser(@PathVariable String userId) {
        return userService.getUserProfile(userId);
    }

    @GetMapping("/users/getAllUsers")
    PageableResponse<UserDto> getAllUsers(
            @RequestParam(value = "pageNumber", defaultValue = "1", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "name", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {
        return userService.getAllUser(pageNumber, pageSize, sortBy, sortDir);
    }

    @DeleteMapping("/users/delete/{userId}")
    Response<String> deleteUser(@PathVariable String userId) {
        return userService.deleteUser(userId);
    }


    @PostMapping("/categories/addCategory")
    Response<CategoryDto> addCategory(@RequestBody CategoryDto categoryDto) {
        return categoryService.addCategory(categoryDto);
    }

    @PutMapping("/categories/updateCategory/{categoryId}")
    Response<CategoryDto> updateCategory(@RequestBody CategoryDto categoryDto, @PathVariable String categoryId) {
        return categoryService.updateCategory(categoryDto, categoryId);
    }

    @DeleteMapping("/categories/deleteCategory/{categoryId}")
    Response<String> deleteCategory(@PathVariable String categoryId) {
        return categoryService.deleteCategory(categoryId);
    }

    @PostMapping("/products/addProduct")
    public Response<ProductDto> addProductWithImages(
            @RequestParam String productData,  // Accepting ProductDto as a request body
            @RequestParam("images") MultipartFile[] images
    ) {
        return productService.addProductWithImages(productData, images);
    }

    // Update product details
    @PutMapping("/products/update/{productId}")
    public Response<ProductDto> updateProduct(
            @PathVariable String productId,
            @RequestBody ProductDto productDto) {
        return productService.updateProduct(productId, productDto);
    }

    // Delete a product
    @DeleteMapping("/products/delete/{productId}")
    public Response<String> deleteProduct(@PathVariable String productId) {
        return productService.deleteProduct(productId);
    }

    // Get all orders
    @GetMapping("/orders/getAllOrders")
    public PageableResponse<OrderDto> getAllOrders(
            @RequestParam(value = "pageNumber", defaultValue = "1", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "name", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {
        return orderService.getAllOrders(pageNumber, pageSize, sortBy, sortDir);
    }

    // Update order status
    @PutMapping("/orders/update/{orderId}")
    public Response<OrderDto> updateOrderStatus(@PathVariable String orderId, @RequestParam String status) {
        return orderService.updateOrderStatus(orderId, status);
    }

    // Delete an order
    @DeleteMapping("/orders/delete/{orderId}")
    public Response<String> deleteOrder(@PathVariable String orderId) {
        return orderService.deleteOrder(orderId);
    }

    // Add inventory
    @PostMapping("/inventory/addProduct")
    public Response<InventoryDto> addInventory(@RequestBody InventoryDto inventoryDto) {
        return inventoryService.addInventory(inventoryDto);
    }

    // Update inventory stock
    @PutMapping("/inventory/update/{productId}")
    public Response<InventoryDto> updateInventoryStock(
            @PathVariable String productId, @RequestBody InventoryDto inventoryDto) {
        return inventoryService.updateInventoryStock(productId, inventoryDto);
    }

    // Delete inventory
    @DeleteMapping("/inventory/delete/{productId}")
    public Response<String> deleteInventory(@PathVariable String productId) {
        return inventoryService.deleteInventory(productId);
    }

    // get all inventory
    @GetMapping("/inventory/getAllInventory")
    public PageableResponse<InventoryDto> getAllInventory(
            @RequestParam(value = "pageNumber", defaultValue = "1", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "name", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {
        return inventoryService.getAllInventory(pageNumber, pageSize, sortBy, sortDir);
    }

    /*
    * Notification Management: pending
        POST /admin/notifications – Broadcast a notification to all users.*/

}
