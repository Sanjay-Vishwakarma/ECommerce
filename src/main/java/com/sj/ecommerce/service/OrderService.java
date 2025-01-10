package com.sj.ecommerce.service;

import com.sj.ecommerce.dto.OrderDto;
import com.sj.ecommerce.dto.PageableResponse;
import com.sj.ecommerce.dto.Response;

public interface OrderService {

    PageableResponse<OrderDto> getAllOrders(int pageNumber, int pageSize, String sortBy, String sortDir);

    Response<OrderDto> updateOrderStatus(String orderId, String status);

    Response<String> deleteOrder(String orderId);

    Response<OrderDto> placeOrder(String userId);

    PageableResponse<OrderDto> getOrderHistory(String userId,int pageNumber, int pageSize, String sortBy, String sortDir);

    Response<OrderDto> getOrderById(String getOrderById);
}
