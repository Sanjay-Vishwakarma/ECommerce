package com.sj.ecommerce.controller;


import com.sj.ecommerce.dto.PageableResponse;
import com.sj.ecommerce.dto.ProductDto;
import com.sj.ecommerce.dto.Response;
import com.sj.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/products")
public class ProductController {


    @Autowired
    private ProductService productService;


    @PostMapping("/addProduct")
    public Response<ProductDto> addProductWithImages(
            @RequestParam String productData,  // Accepting ProductDto as a request body
            @RequestParam("images") MultipartFile[] images) {
        return productService.addProductWithImages(productData, images);
    }

    // Update product details
    @PutMapping("/update/{productId}")
    public Response<ProductDto> updateProduct(
            @PathVariable String productId,
            @RequestBody ProductDto productDto) {
        return productService.updateProduct(productId, productDto);
    }

    // Delete a product
    @DeleteMapping("/delete/{productId}")
    public Response<String> deleteProduct(@PathVariable String productId) {
        return productService.deleteProduct(productId);
    }


    // 6. Get Product by ID
    @GetMapping("/{productId}")
    public Response<ProductDto> getProductById(@PathVariable String productId) {
        return productService.getProductById(productId);
    }


    // 5. Get All Products
    @GetMapping("/getAllProducts")
    public PageableResponse<ProductDto> getAllProducts(
            @RequestParam(value = "pageNumber", defaultValue = "1", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "name", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {
        return productService.getAllProducts(pageNumber, pageSize, sortBy, sortDir);
    }


}
