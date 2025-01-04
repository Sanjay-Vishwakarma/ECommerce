package com.sj.ecommerce.service;

import com.sj.ecommerce.dto.CategoryDto;
import com.sj.ecommerce.reponse.Response;

public interface CategoryService {
    
    Response<CategoryDto> addCategory(CategoryDto categoryDto);

    Response<CategoryDto> updateCategory(CategoryDto categoryDto,String categoryId);

    Response<String> deleteCategory(String categoryId);
}
