package com.gitHub.GabrielPerin.Spring_Stock_Controller.view;
//This class creates the ProductDto object that will be sending to the API
public class ProductDTO {
    private long id;
    private String code;
    private String name;
    private int quantity;
    private double price;
    private String status;

    //Empity
    public ProductDTO(){}

    //Object for PUT and DELETE method
    public ProductDTO(long id,String code,String name,int quantity,double price,String status){
        this.id = id;
        this.code = code;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.status = status;

    }
    //Object for CREATE method
    public ProductDTO(String code,String name,int quantity,double price,String status){
        this.code = code;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.status = status;
    }
    //Getters and Setter
    public long getId(){
        return id;
    }
    public void setId(long id){
        this.id=id;
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
        this.name=name;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity){
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


