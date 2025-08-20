package com.gitHub.GabrielPerin.Spring_Stock_Controller.view;
import com.gitHub.GabrielPerin.Spring_Stock_Controller.model.Product;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.application.Application;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;


public class ClientInterface extends Application {
    // Pulls the Methods of the Productapiclient class to the interfaces button
    ProductApiClient apiClient = new ProductApiClient();
    private final ObservableList<Product> products = FXCollections.observableArrayList();

    //This Variable stores the Spring context
    private static ConfigurableApplicationContext context;
    //Injects the Spring context into javaFx, thus allowing JavaFX to access the Spring Application
    public static void setApplicationContext(ConfigurableApplicationContext ctx){
        context = ctx;
    }

    @Override
    public void start(Stage stage) {
        updateTable();
        Text code = new Text("Código Produto: ");
        TextField codeField = new TextField();
        codeField.setPromptText("123456");
        Text name = new Text("Nome: ");
        TextField nameField = new TextField();
        nameField.setPromptText("Exemplo: Notebook");

        Text quantity = new Text("Quantidade: ");
        TextField quantField = new TextField();
        quantField.setPromptText("15");

        Text price = new Text("Preço");
        TextField priceField = new TextField();
        priceField.setPromptText("Ex: 1500.00");

        Text status = new Text("Status");
        ComboBox statusComboBox = new ComboBox();
        statusComboBox.getItems().addAll("Disponível","Bloqueado");

        //Create Table for the Interface
        TableView<Product> tableView = new TableView<>();
        TableColumn<Product, Integer> idCol = new TableColumn<>("Código");
        idCol.setCellValueFactory(new PropertyValueFactory<>("code"));
        TableColumn<Product, String> nomeCol = new TableColumn<>("Nome");
        nomeCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Product, Integer> qtdCol = new TableColumn<>("Quantidade");
        qtdCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        TableColumn<Product, Double> precoCol = new TableColumn<>("Preço");
        precoCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        TableColumn<Product, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        tableView.getColumns().addAll(idCol, nomeCol, qtdCol, precoCol, statusCol);
        tableView.setItems(products);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        //Objects for the interface
        HBox codeBox = new HBox(8,code,codeField);
        HBox nameBox = new HBox(8,name, nameField);
        HBox quantBox = new HBox(8,quantity, quantField);
        HBox priceBox = new HBox(8,price, priceField);
        HBox statusBox = new HBox(8,status, statusComboBox);

        Button send = new Button("Adicionar");
        send.setPrefWidth(120);
        Button update = new Button("Atualizar");
        update.setPrefWidth(120);
        update.setDisable(true);
        Button delete = new Button("Excluir");
        delete.setPrefWidth(120);
        delete.setDisable(true);
        Button clear = new Button("Limpar Seleção");
        clear.setPrefWidth(120);
        clear.setDisable(true);
        TextField search = new TextField();
        search.setPromptText("Filtre aqui: Nome/Código do Produto");
        search.setPrefWidth(230);
        HBox buttonBox = new HBox(12,send,update,delete,clear,search);
        buttonBox.setPrefWidth(Double.MAX_VALUE);

        //Resource to select the item in the table

        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection,newSelection)->{
            if(newSelection != null){
                codeField.setText(String.valueOf(newSelection.getCode()));
                nameField.setText(newSelection.getName());
                quantField.setText(String.valueOf(newSelection.getQuantity()));
                priceField.setText(String.valueOf(newSelection.getPrice()));
                statusComboBox.setValue(newSelection.getStatus());
                send.setDisable(true);
                update.setDisable(false);
                delete.setDisable(false);
                clear.setDisable(false);
            } else{
                clear.setDisable(true);
                update.setDisable(true);
                delete.setDisable(true);
                send.setDisable(false);
            }
        });
        //Live Filter based by product Code or Name
        search.textProperty().addListener((Observable,oldSearch, newSearch)->{
            String filter = newSearch.toLowerCase();

            if(filter.isEmpty()){
                tableView.setItems(products);
            }else {
                ObservableList<Product> newList = FXCollections.observableArrayList();
                //Creates a New List of Products
                for (Product p : products){
                    if (p.getName().toLowerCase().contains(filter)||p.getCode().contains(filter)){
                    newList.add(p);
                    };
                }
                tableView.setItems(newList);
            }
        });

        VBox layoutCamps = new VBox(codeBox,nameBox, quantBox, priceBox, statusBox,buttonBox,tableView);
        layoutCamps.setSpacing(10);
        layoutCamps.setStyle("-fx-padding: 20;");

        //Action for send the info to Database
        send.setOnAction(_ ->{
            try{
                String getCodeField = codeField.getText();
                String getNameField = nameField.getText();
                int getQntField = Integer.parseInt(quantField.getText());
                double getPrice = Double.parseDouble(priceField.getText());
                String getStatus = (String)statusComboBox.getValue();
                //Create ProductDTO
                ProductDTO productDTO = new ProductDTO(getCodeField,getNameField, getQntField, getPrice,getStatus);
                // In case of success, send the information to the API
                boolean success = apiClient.addProduct(productDTO);

                //When the operation is performed,if the code was a success, it shows the success message at the terminal
                if(success){
                    System.out.println("Produto Adicionado com Sucesso!");
                    codeField.clear();
                    nameField.clear();
                    quantField.clear();
                    priceField.clear();
                    search.clear();
                    statusComboBox.setValue(null);
                    updateTable();
                } else {
                    System.out.println("Falha ao adicionar o Produto!");
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        });
        //Action for Delete a selected item from the table and Database
        delete.setOnAction(_->{
            ////Resource to select the item in the table
            Product selected = tableView.getSelectionModel().getSelectedItem();
            if(selected != null){
                try {
                    // Action condition to delete the item based on the selected product ID
                    boolean deleted = apiClient.deleteProduct(selected.getId());
                    if(deleted){
                        System.out.println("Produto deletado com Sucesso");
                        codeField.clear();
                        nameField.clear();
                        quantField.clear();
                        priceField.clear();
                        search.clear();
                        statusComboBox.setValue(null);
                    }else {
                        System.out.println("Falha ao deletar o Produto");
                    }
                    //Once the action is done, updates the table
                    updateTable();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }else {
                System.out.println("Escolha um item para excluir");
            }
        });

        update.setOnAction(_->{
            // Action condition to uptade info the item based on the selected product ID
            Product selected = tableView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                try {
                    //Take the infos from the table and set on the textField
                    String ucode = codeField.getText();
                    String uname = nameField.getText();
                    int uquantity = Integer.parseInt(quantField.getText());
                    double uprice = Double.parseDouble(priceField.getText());
                    String ustatus = (String) statusComboBox.getValue();

                    //create a new ProductDTO
                    ProductDTO updatedProduct = new ProductDTO(ucode,uname, uquantity, uprice, ustatus);
                    //Update the product based on the selected ID
                    boolean success = apiClient.updateProduct(selected.getId(), updatedProduct);

                    if (success) {
                        System.out.println("Produto atualizado com sucesso!");
                        codeField.clear();
                        nameField.clear();
                        quantField.clear();
                        priceField.clear();
                        search.clear();
                        statusComboBox.setValue(null);
                        updateTable();
                    } else {
                        System.out.println("Erro ao atualizar produto.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Selecione um produto para atualizar.");
            }
        });

        clear.setOnAction(_->{
            tableView.getSelectionModel().clearSelection();
            codeField.clear();
            nameField.clear();
            quantField.clear();
            priceField.clear();
            statusComboBox.setValue(null);
            send.setDisable(false);
            clear.setDisable(true);
            updateTable();

        });


        Scene scene = new Scene(layoutCamps, 800, 800);
        stage.setTitle("Controle de Estoque");
        stage.setScene(scene);
        stage.show();

    }

    public void updateTable() {
        try {
            products.clear();
            ProductDTO[] list = apiClient.getProducts();
            for (ProductDTO p : list) {
                Product product = new Product();
                product.setId(p.getId());
                product.setCode(p.getCode());
                product.setName(p.getName());
                product.setQuantity(p.getQuantity());
                product.setPrice(p.getPrice());
                product.setStatus(p.getStatus());
                products.add(product);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //This method changes the Spring Context to Null as soon as the Program Closes, thus closing the Spring application
    public void stop(){
        if (context != null){
            context.close();
        }
        Platform.exit();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
