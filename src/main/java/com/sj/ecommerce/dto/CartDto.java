package com.sj.ecommerce.dto;

import lombok.Data;

import java.util.List;

@Data
public class CartDto {
    private String id;
    private String userId; // Reference to User
    private List<CartItemDto> cartItems;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<CartItemDto> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItemDto> cartItems) {
        this.cartItems = cartItems;
    }
}
