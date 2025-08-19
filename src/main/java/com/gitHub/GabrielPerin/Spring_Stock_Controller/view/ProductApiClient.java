package com.gitHub.GabrielPerin.Spring_Stock_Controller.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gitHub.GabrielPerin.Spring_Stock_Controller.model.Product;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

class ProductApiClient {
    //Variable that takes the API URL
    private static final String BASE_URL = "http://localhost:8080/Product";
    //Variable for http requests
    private final HttpClient client;
    //Variable for mapper
    private  final ObjectMapper mapper;

     public ProductApiClient(){
         client = HttpClient.newHttpClient();
         mapper = new ObjectMapper();
     }
    //Interface Post Method-> API
    public boolean addProduct(ProductDTO product) throws Exception{
         //Transforms the productDTO generated in the clientinterface into JSON
         String json = mapper.writeValueAsString(product);
        //Send the object via HTTP Post to API
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .header("Content-Type","application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        //Take the transaction code and check it out if it was a success
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.statusCode() == 201 || response.statusCode() == 200;
    }
    // Method to search for database products;
    public ProductDTO[] getProducts() throws  Exception{
         HttpRequest request = HttpRequest.newBuilder()
                 .uri(URI.create(BASE_URL))
                 .GET()
                 .build();

         HttpResponse<String> response = client.send(request , HttpResponse.BodyHandlers.ofString());

         if(response.statusCode() == 200){
             return mapper.readValue(response.body(),ProductDTO[].class);
         } else {
             throw new RuntimeException("Erro ao busca produtos, status"+ response.statusCode());
         }
    }
    //Method PUT interface -> API
    public boolean updateProduct(long id, ProductDTO product) throws Exception{
         String json = mapper.writeValueAsString(product);
        //Create a Update request based on the Product ID
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + id))
                .header("Content-Type","application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return  response.statusCode()==200;
    }
    //Method DELETE Interface -> API;
    public boolean deleteProduct (Long id) throws  Exception{
         //Create a DELETE request based on a ID
         HttpRequest request = HttpRequest.newBuilder()
                 .uri(URI.create(BASE_URL + "/" + id))
                 .DELETE()
                 .build();
         HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
         return response.statusCode() == 200 || response.statusCode()==204;
    }
}
