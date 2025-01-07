package com.sj.ecommerce.controller;


import com.sj.ecommerce.dto.CategoryDto;
import com.sj.ecommerce.reponse.Response;
import com.sj.ecommerce.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /*
    * Manages product categories.APIs:
    GET /categories – Get all categories.
    GET /categories/{categoryId} – Get a specific category.
    POST /categories – Add a new category.
    PUT /categories/{categoryId} – Update a category.
    DELETE /categories/{categoryId} – Delete a category.*/

    @GetMapping("/getAllCatogories")
    Response<List<CategoryDto>> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/getCategoryById/{categoryId}")
    Response<CategoryDto> getCategoryById(@PathVariable String categoryId) {
        return categoryService.getCategoryById(categoryId);
    }


}
