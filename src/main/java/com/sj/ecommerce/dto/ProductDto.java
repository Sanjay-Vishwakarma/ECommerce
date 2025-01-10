package com.sj.ecommerce.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProductDto {
    private String id;
    private String name;
    private String description;
    private Double price;
    private Integer stock; // Current stock - quantity
    private String categoryId; // Reference to Category
    private List<String> imageUrls;

}
