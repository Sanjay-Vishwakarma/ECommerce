package com.sj.ecommerce.service;

import com.sj.ecommerce.dto.CartDto;
import com.sj.ecommerce.dto.Response;

public interface CartService {
    Response<CartDto> addToCart(CartDto cartDto);

    Response<CartDto> getCart(String userId);

    Response<CartDto> updateCartProductQuantity(String productId, Integer quantity);

    Response<String> removeFromCart(String userId, String productId) ;
}
