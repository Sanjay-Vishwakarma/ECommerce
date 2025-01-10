package com.sj.ecommerce.controller;


import com.sj.ecommerce.dto.CategoryDto;
import com.sj.ecommerce.dto.PageableResponse;
import com.sj.ecommerce.dto.Response;
import com.sj.ecommerce.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;


    @GetMapping("/getAllCatogories")
    PageableResponse<CategoryDto> getAllCategories(
            @RequestParam(value = "pageNumber", defaultValue = "1", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "name", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {
        return categoryService.getAllCategories(pageNumber, pageSize, sortBy, sortDir);
    }

    @GetMapping("/getCategoryById/{categoryId}")
    Response<CategoryDto> getCategoryById(@PathVariable String categoryId) {
        return categoryService.getCategoryById(categoryId);
    }


}
