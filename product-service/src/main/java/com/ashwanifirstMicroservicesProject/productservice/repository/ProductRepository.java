package com.ashwanifirstMicroservicesProject.productservice.repository;



import com.ashwanifirstMicroservicesProject.productservice.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, String> {
}
