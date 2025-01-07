package com.sj.ecommerce.service;

import com.sj.ecommerce.dto.ProductDto;
import com.sj.ecommerce.helper.Response;

import java.util.List;

public interface ProductService {

    Response<List<ProductDto>> getAllProducts();

    Response<ProductDto> addProduct(ProductDto productDto);

    Response<ProductDto> updateProduct(String productId, ProductDto productDto);

    Response<String> deleteProduct(String productId);

    Response<ProductDto> getProductById(String productId);
}
