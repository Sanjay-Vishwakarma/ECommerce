package com.sj.ecommerce.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Data
@Document(collection = "carts")
public class Cart {
    @Id
    private String id;

    @NotNull(message = "User ID cannot be null.")
    private String userId; // Reference to User

    @NotNull(message = "Cart items cannot be null.")
    private List<CartItem> cartItems; // Embedded documents


    @Data
    public static class CartItem {

        @NotNull(message = "Product ID cannot be null.")
        private String productId; // Reference to Product

        @NotNull(message = "Quantity cannot be null.")
        @Min(value = 1, message = "Quantity must be at least 1.")
        private Integer quantity;

        private Double productPrice;

        private String productName;

    }
}
