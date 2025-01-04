package com.sj.ecommerce.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "notifications")
public class Notification {
    @Id
    private String id;
    private String userId; // Reference to User
    private String message;
    private Boolean isRead;
    private Date timestamp;
}
