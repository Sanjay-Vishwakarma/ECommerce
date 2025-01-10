package com.sj.ecommerce.service;

import com.sj.ecommerce.dto.CategoryDto;
import com.sj.ecommerce.dto.PageableResponse;
import com.sj.ecommerce.dto.Response;

public interface CategoryService {
    
    Response<CategoryDto> addCategory(CategoryDto categoryDto);

    Response<CategoryDto> updateCategory(CategoryDto categoryDto,String categoryId);

    Response<String> deleteCategory(String categoryId);

    PageableResponse<CategoryDto> getAllCategories(int pageNumber, int pageSize, String sortBy, String sortDir);

    Response<CategoryDto> getCategoryById(String categoryId);
}
