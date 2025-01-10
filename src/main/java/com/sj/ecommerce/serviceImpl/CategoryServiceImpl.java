package com.sj.ecommerce.serviceImpl;

import com.sj.ecommerce.dto.CategoryDto;
import com.sj.ecommerce.dto.PageableResponse;
import com.sj.ecommerce.entity.Category;
import com.sj.ecommerce.helper.PageHelper;
import com.sj.ecommerce.dto.Response;
import com.sj.ecommerce.repository.CategoryRepository;
import com.sj.ecommerce.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {


    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CategoryRepository categoryRepository;

    private final PageHelper pageHelper;

    @Autowired
    public CategoryServiceImpl(PageHelper pageHelper) {
        this.pageHelper = pageHelper;
    }

    @Override
    public Response<CategoryDto> addCategory(CategoryDto categoryDto) {
        Response<CategoryDto> response = new Response<>();
        try {
            categoryRepository.save(modelMapper.map(categoryDto, Category.class));
            response.setData(modelMapper.map(categoryDto, CategoryDto.class));
            response.setMessage("Category added successfully");
            return response;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Response<CategoryDto> updateCategory(CategoryDto categoryDto, String categoryId) {
        Response<CategoryDto> response = new Response<>();
        try {
            // Check if the category exists
            if (categoryRepository.existsById(categoryId)) {
                // Retrieve the existing category
                Category existingCategory = categoryRepository.findById(categoryId).orElse(null);

                if (existingCategory != null) {
                    // Update the category details
                    existingCategory.setName(categoryDto.getName());
                    existingCategory.setDescription(categoryDto.getDescription());

                    // Save the updated category
                    Category updatedCategory = categoryRepository.save(existingCategory);

                    // Map the updated category entity to CategoryDto
                    CategoryDto updatedCategoryDto = modelMapper.map(updatedCategory, CategoryDto.class);

                    // Populate the response
                    response.setStatus("success");
                    response.setMessage("Category updated successfully.");
                    response.setData(updatedCategoryDto);
                } else {
                    response.setStatus("error");
                    response.setMessage("Category not found.");
                    response.setData(null);
                }
            } else {
                response.setStatus("error");
                response.setMessage("Category not found.");
                response.setData(null);
            }
        } catch (Exception e) {
            e.printStackTrace(); // Replace with proper logging in production
            response.setStatus("error");
            response.setMessage("An error occurred while updating the category.");
            response.setData(null);
        }
        return response;
    }

    @Override
    public Response<String> deleteCategory(String categoryId) {
        Response<String> response = new Response<>();
        try {
            // Check if the category exists
            if (categoryRepository.existsById(categoryId)) {
                // Delete the category
                categoryRepository.deleteById(categoryId);
                // Set success response
                response.setStatus("success");
                response.setMessage("Category deleted successfully.");
                response.setData(null); // No additional data to return
            } else {
                // If category not found
                response.setStatus("error");
                response.setMessage("Category not found.");
                response.setData(null);
            }
        } catch (Exception e) {
            e.printStackTrace(); // Replace with proper logging in production
            response.setStatus("error");
            response.setMessage("An error occurred while deleting the category.");
            response.setData(null);
        }
        return response;
    }

    @Override
    public PageableResponse<CategoryDto> getAllCategories(int pageNumber, int pageSize, String sortBy, String sortDir) {
        // Determine sorting direction
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        // Create Pageable object (adjusting page number to zero-based indexing)
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);

        Page<Category> all = categoryRepository.findAll(pageable);

        return pageHelper.getPageableResponse(all, CategoryDto.class);
    }

    @Override
    public Response<CategoryDto> getCategoryById(String categoryId) {
        Response<CategoryDto> response = new Response<>();
        try {
            Category category = categoryRepository.findById(categoryId).orElseThrow(() ->
                    new RuntimeException("Category not found by this id " + categoryId));
            CategoryDto categoryDto = modelMapper.map(category, CategoryDto.class);
            response.setData(categoryDto);
            response.setStatus("success");
            response.setMessage("Category fetched successfully.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return response;
    }


}
