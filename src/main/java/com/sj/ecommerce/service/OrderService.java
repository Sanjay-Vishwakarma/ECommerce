package com.sj.ecommerce.service;

import com.sj.ecommerce.dto.OrderDto;
import com.sj.ecommerce.reponse.Response;

import java.util.List;

public interface OrderService {

    Response<List<OrderDto>> getAllOrders();

    Response<OrderDto> updateOrderStatus(String orderId, String status);

    Response<String> deleteOrder(String orderId);

    Response<OrderDto> placeOrder(String userId);

    Response<List<OrderDto>> getOrderHistory(String userId);

    Response<OrderDto> getOrderById(String getOrderById);
}
