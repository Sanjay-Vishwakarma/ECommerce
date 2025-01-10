package com.sj.ecommerce.serviceImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sj.ecommerce.dto.PageableResponse;
import com.sj.ecommerce.dto.ProductDto;
import com.sj.ecommerce.entity.Product;
import com.sj.ecommerce.helper.PageHelper;
import com.sj.ecommerce.dto.Response;
import com.sj.ecommerce.repository.ProductRepository;
import com.sj.ecommerce.service.ProductService;
import com.sj.ecommerce.utils.CloudinaryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper modelMapper;

    private final PageHelper pageHelper;

    @Autowired
    public ProductServiceImpl(PageHelper pageHelper) {
        this.pageHelper = pageHelper;
    }

    @Autowired
    public CloudinaryService cloudinaryService;

    @Override
    public PageableResponse<ProductDto> getAllProducts(int pageNumber, int pageSize, String sortBy, String sortDir) {
        // Determine sorting direction
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        // Create Pageable object (adjusting page number to zero-based indexing)
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);

        // Fetch paginated product data
        Page<Product> productPage = productRepository.findAll(pageable);

        // Use Helper to convert Page<Product> to PageableResponse<ProductDto>
        return pageHelper.getPageableResponse(productPage, ProductDto.class);
    }


/*
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
    */

    @Override
    public Response<ProductDto> addProductWithImages(String productData, MultipartFile[] images) {
        Response<ProductDto> response = new Response<>();
        try {
            // Parse productData JSON to ProductDto
            ObjectMapper objectMapper = new ObjectMapper();
            ProductDto productDto = objectMapper.readValue(productData, ProductDto.class);

            // Upload images to Cloudinary and collect URLs
            List<String> imageUrls = Arrays.stream(images)
                    .map(image -> cloudinaryService.uploadImage(image))
                    .collect(Collectors.toList());

            // Set image URLs in ProductDto
            productDto.setImageUrls(imageUrls);

            // Map ProductDto to Product entity
            Product product = modelMapper.map(productDto, Product.class);

            // Save product to the database
            Product savedProduct = productRepository.save(product);

            // Map saved Product to ProductDto
            ProductDto savedProductDto = modelMapper.map(savedProduct, ProductDto.class);

            response.setStatus("success");
            response.setMessage("Product added successfully.");
            response.setData(savedProductDto);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus("error");
            response.setMessage("Failed to add product: " + e.getMessage());
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
