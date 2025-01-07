package com.sj.ecommerce.serviceImpl;

import com.sj.ecommerce.dto.CartDto;
import com.sj.ecommerce.dto.OrderDto;
import com.sj.ecommerce.entity.Cart;
import com.sj.ecommerce.reponse.Response;
import com.sj.ecommerce.repository.CartRepository;
import com.sj.ecommerce.repository.OrderRepository;
import com.sj.ecommerce.service.OrderService;
import com.sj.ecommerce.entity.Order;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartRepository cartRepository;

    @Override
    public Response<List<OrderDto>> getAllOrders() {
        // Fetch all orders from the repository
        List<Order> orders = orderRepository.findAll();
        // Convert orders to DTOs
        List<OrderDto> orderDtos = orders.stream()
                .map(order -> modelMapper.map(order, OrderDto.class))
                .collect(Collectors.toList());
        // Prepare and return response
        Response<List<OrderDto>> response = new Response<>();
        response.setData(orderDtos);
        response.setCode("200");
        response.setMessage("Orders retrieved successfully");
        response.setStatus("success");
        return response;
    }

    @Override
    public Response<OrderDto> updateOrderStatus(String orderId, String status) {
        // Fetch the order from the repository
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        Response<OrderDto> response = new Response<>();
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            order.setStatus(status);

            // Save the updated order
            orderRepository.save(order);

            // Convert the updated order entity to DTO
            OrderDto orderDto = modelMapper.map(order, OrderDto.class);

            response.setData(orderDto);
            response.setCode("200");
            response.setMessage("Order status updated successfully");
            response.setStatus("success");
        } else {
            response.setCode("404");
            response.setMessage("Order not found with id: " + orderId);
            response.setStatus("error");
        }
        return response;
    }

    @Override
    public Response<String> deleteOrder(String orderId) {
        Response<String> response = new Response<>();
        // Check if the order exists and delete it
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isPresent()) {
            orderRepository.delete(orderOpt.get());
            response.setCode("200");
            response.setMessage("Order canceled successfully");
            response.setStatus("success");
        } else {
            response.setCode("404");
            response.setMessage("Order not found with id: " + orderId);
            response.setStatus("error");
        }
        return response;
    }

    @Override
    public Response<OrderDto> placeOrder(String userId) {
        // Fetch the user's cart
        CartDto byUserId = cartRepository.findByUserId(userId);
        Cart cart = modelMapper.map(byUserId, Cart.class);
        if (cart == null || cart.getCartItems().isEmpty()) {
            throw new IllegalArgumentException("Cart is empty or does not exist for user: " + userId);
        }

        // Calculate total price
        double totalPrice = cart.getCartItems().stream()
                .mapToDouble(item -> item.getQuantity() * item.getProductPrice())
                .sum();

        // Convert CartItems to OrderItems
        List<Order.OrderItem> orderItems = cart.getCartItems().stream()
                .map(cartItem -> {
                    Order.OrderItem orderItem = new Order.OrderItem();
                    orderItem.setProductId(cartItem.getProductId());
                    orderItem.setQuantity(cartItem.getQuantity());
                    orderItem.setTotalPrice(cartItem.getQuantity() * cartItem.getProductPrice());
                    return orderItem;
                })
                .toList();


        Order order = new Order();
        order.setUserId(userId);
        order.setOrderItems(orderItems);
        order.setTotalAmount(totalPrice);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PLACED");

        Order savedOrder = orderRepository.save(order);

        // Clear the cart after placing the order
        cartRepository.delete(cart);

        // Map the saved order to DTO
        OrderDto orderDto = modelMapper.map(savedOrder, OrderDto.class);

        // Return the response
        Response<OrderDto> response = new Response<>();
        response.setStatus("success");
        response.setMessage("Order placed successfully.");
        response.setData(orderDto);

        return response;
    }

    @Override
    public Response<List<OrderDto>> getOrderHistory(String userId) {
        // Fetch orders for the specific user
        List<Order> orders = orderRepository.findByUserId(userId);
        // Map the Order entities to OrderDTOs using ModelMapper
        List<OrderDto> orderDtos = orders.stream()
                .map(order -> modelMapper.map(order, OrderDto.class))
                .collect(Collectors.toList());

        // Create a Response object and set the data
        Response<List<OrderDto>> response = new Response<>();
        response.setData(orderDtos);
        response.setCode("200");  // You can set appropriate code
        response.setMessage("Order history fetched successfully");
        response.setStatus("SUCCESS");

        return response;
    }

    @Override
    public Response<OrderDto> getOrderById(String orderId) {
        try {
            // Fetch the Order entity by its ID
            Optional<Order> orderOptional = orderRepository.findById(orderId);

            // If Order is found, map it to OrderDto
            if (orderOptional.isPresent()) {
                Order order = orderOptional.get();

                // Map the Order entity to OrderDto using ModelMapper
                OrderDto orderDto = modelMapper.map(order, OrderDto.class);

                // Create and return a successful response
                Response<OrderDto> response = new Response<>();
                response.setData(orderDto);
                response.setCode("200");  // Appropriate success code
                response.setMessage("Order fetched successfully");
                response.setStatus("SUCCESS");

                return response;
            } else {
                // Handle the case where the Order is not found
                Response<OrderDto> response = new Response<>();
                response.setData(null);
                response.setCode("404");  // Not Found code
                response.setMessage("Order not found");
                response.setStatus("ERROR");

                return response;
            }
        } catch (Exception e) {
            // Handle exceptions and return a generic error response
            Response<OrderDto> response = new Response<>();
            response.setData(null);
            response.setCode("500");  // Internal Server Error
            response.setMessage("An error occurred while fetching the order: " + e.getMessage());
            response.setStatus("ERROR");

            return response;
        }
    }


}
