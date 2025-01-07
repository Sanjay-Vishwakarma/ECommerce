package com.sj.ecommerce.controller;


import com.sj.ecommerce.dto.ProductDto;
import com.sj.ecommerce.reponse.Response;
import com.sj.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductController {


    @Autowired
    private ProductService productService;


//    // Add a new product
//    @PostMapping("/products/addProduct")
//    public Response<ProductDto> addProduct(@RequestBody ProductDto productDto) {
//        return productService.addProduct(productDto);
//    }


}
