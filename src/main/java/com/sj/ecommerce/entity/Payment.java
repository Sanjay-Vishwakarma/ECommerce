package com.sj.ecommerce.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "payments")
public class Payment {
    @Id
    private String id;
    private String orderId; // Reference to Order
    private String userId; // Reference to User
    private Double amount;
    private String method; // e.g., "CREDIT_CARD", "PAYPAL"
    private String status; // e.g., "SUCCESS", "FAILED"
}
