package com.gitHub.GabrielPerin.Spring_Stock_Controller;

import com.gitHub.GabrielPerin.Spring_Stock_Controller.view.ClientInterface;
import javafx.application.Application;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

//This class has the function of connecting the Spring API backend and the JavaFx Interface
public class Main {
    public static void main(String[] args){
        //Initializes the Spring Application context
        ConfigurableApplicationContext context = SpringApplication.run(SpringStockControllerApplication.class, args);
        //Injects and throws the JavaFx context
        ClientInterface.setApplicationContext(context);
        Application.launch(ClientInterface.class,args);
    }
}
