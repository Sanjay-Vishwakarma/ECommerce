package com.sj.ecommerce.serviceImpl;

import com.sj.ecommerce.dto.OrderDto;
import com.sj.ecommerce.reponse.Response;
import com.sj.ecommerce.repository.OrderRepository;
import com.sj.ecommerce.service.OrderService;
import com.sj.ecommerce.entity.Order;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private OrderRepository orderRepository;

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
}
