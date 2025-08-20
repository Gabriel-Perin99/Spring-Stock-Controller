package com.gitHub.GabrielPerin.Spring_Stock_Controller.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;

@Entity
public class Product {
    //Spring will automatically generate an ID in the database..
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotEmpty(message = "Informe o código do produto")
    private String code;

    //Don't let the name be null
    @NotEmpty(message = "Informe um nome!")
    private String name;

    private int quantity;
    private double price;
    private String status;

    public Product(String code ,String name, int quantity, double price,String status){
        this.code = code;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.status = status;
    }
    //Empty Constructor
    public Product(){}

    //Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id){
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public int getQuantity(){
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

