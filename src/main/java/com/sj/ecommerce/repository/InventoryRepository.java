package com.sj.ecommerce.repository;

import com.sj.ecommerce.entity.Inventory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventoryRepository extends MongoRepository<Inventory, String> {

    // Additional custom queries can be defined if necessary
}
