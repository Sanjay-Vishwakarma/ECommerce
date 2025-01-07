package com.sj.ecommerce.dto;

import lombok.Data;

import java.util.List;

@Data
public class CartDto {
    private String id;
    private String userId; // Reference to User
    private List<CartItemDto> items;

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

    public List<CartItemDto> getItems() {
        return items;
    }

    public void setItems(List<CartItemDto> items) {
        this.items = items;
    }

}
