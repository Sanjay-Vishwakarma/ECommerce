package com.sj.ecommerce.serviceImpl;

import com.sj.ecommerce.dto.ProductDto;
import com.sj.ecommerce.entity.Product;
import com.sj.ecommerce.helper.Response;
import com.sj.ecommerce.repository.ProductRepository;
import com.sj.ecommerce.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Response<List<ProductDto>> getAllProducts() {
        // Retrieve all products from the database
        List<Product> products = productRepository.findAll();

        // Map each product to ProductDto using modelMapper
        List<ProductDto> productDtos = products.stream()
                .map(product -> modelMapper.map(product, ProductDto.class))
                .collect(Collectors.toList());

        // Create and populate the response object
        Response<List<ProductDto>> response = new Response<>();
        response.setData(productDtos);  // Set the mapped product DTO list
        response.setCode("200");
        response.setMessage("Products retrieved successfully");
        response.setStatus("success");

        return response;
    }


    @Override
    public Response<ProductDto> addProduct(ProductDto productDto) {
        Response<ProductDto> response = new Response<>();
        try {
            Product product = modelMapper.map(productDto, Product.class);
            Product savedProduct = productRepository.save(product);
            ProductDto savedProductDto = modelMapper.map(savedProduct, ProductDto.class);

            response.setStatus("success");
            response.setMessage("Product added successfully.");
            response.setData(savedProductDto);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus("error");
            response.setMessage("Failed to add product.");
            response.setData(null);
        }
        return response;
    }

    @Override
    public Response<ProductDto> updateProduct(String productId, ProductDto productDto) {
        Response<ProductDto> response = new Response<>();
        try {
            Optional<Product> optionalProduct = productRepository.findById(productId);
            if (optionalProduct.isPresent()) {
                Product existingProduct = optionalProduct.get();
                existingProduct.setName(productDto.getName());
                existingProduct.setDescription(productDto.getDescription());
                existingProduct.setPrice(productDto.getPrice());
                existingProduct.setStock(productDto.getStock());

                Product updatedProduct = productRepository.save(existingProduct);
                ProductDto updatedProductDto = modelMapper.map(updatedProduct, ProductDto.class);

                response.setStatus("success");
                response.setMessage("Product updated successfully.");
                response.setData(updatedProductDto);
            } else {
                response.setStatus("error");
                response.setMessage("Product not found.");
                response.setData(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus("error");
            response.setMessage("Failed to update product.");
            response.setData(null);
        }
        return response;
    }

    @Override
    public Response<String> deleteProduct(String productId) {
        Response<String> response = new Response<>();
        try {
            if (productRepository.existsById(productId)) {
                productRepository.deleteById(productId);
                response.setStatus("success");
                response.setMessage("Product deleted successfully.");
                response.setData(null);
            } else {
                response.setStatus("error");
                response.setMessage("Product not found.");
                response.setData(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus("error");
            response.setMessage("Failed to delete product.");
            response.setData(null);
        }
        return response;
    }

    @Override
    public Response<ProductDto> getProductById(String productId) {
        Response<ProductDto> response = new Response<>();
        try {
            boolean b = productRepository.existsById(productId);
            if (b) {
                Optional<Product> byId = productRepository.findById(productId);
                ProductDto productDto = modelMapper.map(byId.get(), ProductDto.class);
                response.setStatus("success");
                response.setMessage("Product retrieved successfully.");
                response.setData(productDto);
            } else {
                response.setStatus("error");
                response.setMessage("Product not found.");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return response;
    }
}
