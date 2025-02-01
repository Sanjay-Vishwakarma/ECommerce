package com.sj.ecommerce.controller;

import com.sj.ecommerce.dto.OrderDto;
import com.sj.ecommerce.dto.PageableResponse;
import com.sj.ecommerce.dto.Response;
import com.sj.ecommerce.service.OrderService;
import com.sj.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    // 11. Place Order
    @PostMapping("/placeOrder")
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
    @GetMapping("/history")
    public PageableResponse<OrderDto> getOrderHistory(@RequestParam String userId,
                                                      @RequestParam(value = "pageNumber", defaultValue = "1", required = false) int pageNumber,
                                                      @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
                                                      @RequestParam(value = "sortBy", defaultValue = "name", required = false) String sortBy,
                                                      @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir) {
        return orderService.getOrderHistory(userId, pageNumber, pageSize, sortBy, sortDir);

    }

    // Update order status
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{orderId}")
    public Response<OrderDto> updateOrderStatus(@PathVariable String orderId, @RequestParam String status) {
        return orderService.updateOrderStatus(orderId, status);
    }


    // 13. Get Order by ID
    @GetMapping("/{getOrderById}")
    public Response<OrderDto> getOrderById(@PathVariable String getOrderById) {
        return orderService.getOrderById(getOrderById);
    }


    // Delete an order
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{orderId}")
    public Response<String> deleteOrder(@PathVariable String orderId) {
        return orderService.deleteOrder(orderId);
    }


    // Get all orders - admin
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getAllOrders")
    public PageableResponse<OrderDto> getAllOrders(
            @RequestParam(value = "pageNumber", defaultValue = "1", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "name", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {
        return orderService.getAllOrders(pageNumber, pageSize, sortBy, sortDir);
    }

}
