package com.example.api.repository;

import com.example.api.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;


public interface ProductRepository extends MongoRepository<Product, Integer> {


}
