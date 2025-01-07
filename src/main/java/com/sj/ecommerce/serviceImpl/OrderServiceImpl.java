package com.sj.ecommerce.serviceImpl;

import com.sj.ecommerce.dto.CartDto;
import com.sj.ecommerce.dto.OrderDto;
import com.sj.ecommerce.dto.OrderItemDto;
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
        System.out.println("byUserId = " + byUserId);
        Cart cart = modelMapper.map(byUserId, Cart.class); // // Fetch the user's cart
        if (cart == null || cart.getCartItems().isEmpty()) {
            throw new IllegalArgumentException("Cart is empty or does not exist for user: " + userId);
        }

        // Calculate total price
        double totalPrice = cart.getCartItems().stream()
                .mapToDouble(item -> item.getQuantity() * item.getProductPrice())
                .sum();

        // Convert CartItems to OrderItems and map to OrderItemDto
        List<Order.OrderItem> orderItems = cart.getCartItems().stream()
                .map(cartItem -> {
                    Order.OrderItem orderItem = new Order.OrderItem();
                    orderItem.setProductId(cartItem.getProductId());
                    orderItem.setProductName(cartItem.getProductName());  // Ensure the name is copied
                    orderItem.setQuantity(cartItem.getQuantity());
                    orderItem.setTotalPrice(cartItem.getQuantity() * cartItem.getProductPrice());
                    return orderItem;
                })
                .toList();

        // Create the Order object
        Order order = new Order();
        order.setUserId(userId);
        order.setOrderItems(orderItems);
        order.setTotalAmount(totalPrice);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PLACED");

        // Save the order
        Order savedOrder = orderRepository.save(order);

        // Clear the cart after placing the order
        cartRepository.delete(cart); // // Delete only this specific cart

        // Map the saved order to DTO
        OrderDto orderDto = modelMapper.map(savedOrder, OrderDto.class);

        // Set orderItems in OrderDto from the Order
        orderDto.setOrderItemDto(savedOrder.getOrderItems().stream()
                .map(orderItem -> {
                    OrderItemDto itemDto = new OrderItemDto();
                    itemDto.setProductId(orderItem.getProductId());
                    itemDto.setProductName(orderItem.getProductName());  // Ensure the name is set
                    itemDto.setQuantity(orderItem.getQuantity());
                    itemDto.setTotalPrice(orderItem.getTotalPrice());
                    return itemDto;
                }).collect(Collectors.toList()));

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

        // Manually map the Order entities to OrderDto
        List<OrderDto> orderDtos = orders.stream()
                .map(order -> {
                    // Map the Order entity to OrderDto using ModelMapper
                    OrderDto orderDto = modelMapper.map(order, OrderDto.class);

                    // Manually map orderItems to orderItemDto
                    List<OrderItemDto> orderItemDtos = order.getOrderItems().stream()
                            .map(orderItem -> {
                                OrderItemDto itemDto = new OrderItemDto();
                                itemDto.setProductId(orderItem.getProductId());
                                itemDto.setProductName(orderItem.getProductName());
                                itemDto.setQuantity(orderItem.getQuantity());
                                itemDto.setTotalPrice(orderItem.getTotalPrice());
                                return itemDto;
                            })
                            .collect(Collectors.toList());

                    // Set the orderItems in the orderDto
                    orderDto.setOrderItemDto(orderItemDtos);

                    // Set the orderDate
                    if (order.getOrderDate() != null) {
                        orderDto.setOrderDate(order.getOrderDate());
                    }

                    return orderDto;
                })
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
        Response<OrderDto> response = new Response<>();
        try {
            // Fetch the Order entity by its ID
            Optional<Order> orderOptional = orderRepository.findById(orderId);

            // If Order is found, map it to OrderDto
            if (orderOptional.isPresent()) {
                Order order = orderOptional.get();

                // Map the Order entity to OrderDto using ModelMapper
                OrderDto orderDto = modelMapper.map(order, OrderDto.class);

                // Manually map the order items
                List<OrderItemDto> orderItemDtos = order.getOrderItems().stream()
                        .map(orderItem -> {
                            OrderItemDto itemDto = new OrderItemDto();
                            itemDto.setProductId(orderItem.getProductId());
                            itemDto.setProductName(orderItem.getProductName());
                            itemDto.setQuantity(orderItem.getQuantity());
                            itemDto.setTotalPrice(orderItem.getTotalPrice());
                            return itemDto;
                        })
                        .collect(Collectors.toList());

                // Set order items in the DTO
                orderDto.setOrderItemDto(orderItemDtos);

                // Set order date if not null
                orderDto.setOrderDate(order.getOrderDate());

                // Set response data
                response.setData(orderDto);
                response.setCode("200");  // Success code
                response.setMessage("Order fetched successfully");
                response.setStatus("SUCCESS");

            } else {
                // Handle the case where the Order is not found
                response.setData(null);
                response.setCode("404");  // Not Found code
                response.setMessage("Order not found");
                response.setStatus("ERROR");
            }
        } catch (Exception e) {
            // Handle exceptions and return a generic error response
            response.setData(null);
            response.setCode("500");  // Internal Server Error
            response.setMessage("An error occurred while fetching the order: " + e.getMessage());
            response.setStatus("ERROR");
        }

        return response;
    }


}
