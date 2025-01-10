package com.sj.ecommerce.service;

import com.sj.ecommerce.dto.PageableResponse;
import com.sj.ecommerce.dto.ProductDto;
import com.sj.ecommerce.dto.Response;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {

    PageableResponse<ProductDto> getAllProducts(int pageNumber, int pageSize, String sortBy, String sortDir);

//    Response<ProductDto> addProduct(ProductDto productDto);

    Response<ProductDto> addProductWithImages(String productDto, MultipartFile[] images);

    Response<ProductDto> updateProduct(String productId, ProductDto productDto);

    Response<String> deleteProduct(String productId);

    Response<ProductDto> getProductById(String productId);
}
