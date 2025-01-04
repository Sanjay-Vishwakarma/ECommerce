package com.sj.ecommerce.dto;

import lombok.Data;

import java.util.Date;

@Data
public class NotificationDto {
    private String id;
    private String userId; // Reference to User
    private String message;
    private Boolean isRead;
    private Date timestamp;

}
