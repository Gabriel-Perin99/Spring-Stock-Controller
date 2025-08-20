package com.gitHub.GabrielPerin.Spring_Stock_Controller.repository;

import com.gitHub.GabrielPerin.Spring_Stock_Controller.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

//// This class is generic and, due to Spring, will manage the Model Product class, performing CRUD BASIC OPERATIONS
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByName(String name);
}
