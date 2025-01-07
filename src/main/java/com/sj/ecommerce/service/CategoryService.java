package com.sj.ecommerce.service;

import com.sj.ecommerce.dto.CategoryDto;
import com.sj.ecommerce.helper.Response;

import java.util.List;

public interface CategoryService {
    
    Response<CategoryDto> addCategory(CategoryDto categoryDto);

    Response<CategoryDto> updateCategory(CategoryDto categoryDto,String categoryId);

    Response<String> deleteCategory(String categoryId);

    Response<List<CategoryDto>> getAllCategories();

    Response<CategoryDto> getCategoryById(String categoryId);
}
