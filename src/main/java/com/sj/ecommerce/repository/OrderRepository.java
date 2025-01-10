package com.sj.ecommerce.repository;

import com.sj.ecommerce.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {

    List<Order> findByUserId(String userId); // Assuming you want to fetch orders for a specific user

    Page<Order> findByUserId(String userId, Pageable pageable);


}
