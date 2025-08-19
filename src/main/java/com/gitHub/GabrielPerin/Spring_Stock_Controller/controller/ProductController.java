package com.gitHub.GabrielPerin.Spring_Stock_Controller.controller;

import com.gitHub.GabrielPerin.Spring_Stock_Controller.model.Product;
import com.gitHub.GabrielPerin.Spring_Stock_Controller.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

//This class deals with HTTP requests and returns the appropriate response to each client.
@RestController
@RequestMapping("/Product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public List<Product> listAll(){
        return productService.listAll();
    }

    @PostMapping
    public Product save(@RequestBody Product product){
        return productService.save(product);
    }
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        productService.delete(id);
    }
    @PutMapping("/{id}")
    public Product update (@PathVariable Long id, @RequestBody Product product){
        return productService.update(id, product);
    }
    @GetMapping("/{id}")
    public Optional<Product> findById(@PathVariable Long id){
        return productService.findById(id);
    }
}
