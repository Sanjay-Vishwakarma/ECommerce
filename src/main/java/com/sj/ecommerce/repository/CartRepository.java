package com.sj.ecommerce.repository;

import com.sj.ecommerce.dto.CartDto;
import com.sj.ecommerce.entity.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CartRepository extends MongoRepository<Cart, String> {


    CartDto findByUserId(String userId);

    @Query("{'productId': ?0}")
    CartDto findByProductId(Long productId);
}
