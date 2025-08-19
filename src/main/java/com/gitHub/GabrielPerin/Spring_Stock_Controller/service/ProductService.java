package com.gitHub.GabrielPerin.Spring_Stock_Controller.service;

import com.gitHub.GabrielPerin.Spring_Stock_Controller.model.Product;
import com.gitHub.GabrielPerin.Spring_Stock_Controller.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
//This class aims to perform CRUD operations, which were once done manually, automatically and internal
@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public List<Product> listAll(){
        return productRepository.findAll();
    }
    public Product save (Product product){
        return productRepository.save(product);
    }
    public void delete(Long id){
        productRepository.deleteById(id);
    }
    public Product update(long id, Product product){
        if(productRepository.existsById(id)){
            product.setId(id);
            return productRepository.save(product);
        }else{
            throw new RuntimeException("Não foi encontrado nenhum produto");
        }
    }
    public Optional<Product> findById(Long id){
        return productRepository.findById(id);
    }
}
