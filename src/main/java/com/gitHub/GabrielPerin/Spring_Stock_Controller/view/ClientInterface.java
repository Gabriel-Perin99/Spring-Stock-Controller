package com.gitHub.GabrielPerin.Spring_Stock_Controller.view;
import com.gitHub.GabrielPerin.Spring_Stock_Controller.model.Product;
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


public class ClientInterface extends Application {
    // Pulls the Methods of the Productapiclient class to the interfaces button
    ProductApiClient apiClient = new ProductApiClient();
    private final ObservableList<Product> products = FXCollections.observableArrayList();

    @Override
    public void start(Stage stage) {

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
        TableColumn<Product, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
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
        //Resource to select the item in the table
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection,newSelection)->{
            if(newSelection != null){
                nameField.setText(newSelection.getName());
                quantField.setText(String.valueOf(newSelection.getQuantity()));
                priceField.setText(String.valueOf(newSelection.getPrice()));
                statusComboBox.setValue(newSelection.getStatus());
            }
        });
        //Objects for the interface
        HBox nameBox = new HBox(8,name, nameField);
        HBox quantBox = new HBox(8,quantity, quantField);
        HBox priceBox = new HBox(8,price, priceField);
        HBox statusBox = new HBox(8,status, statusComboBox);

        Button send = new Button("Adicionar");
        Button update = new Button("Atualizar");
        Button delete = new Button("Excluir");
        HBox buttonBox = new HBox(8,send,update,delete);

        VBox layoutCamps = new VBox(nameBox, quantBox, priceBox, statusBox,buttonBox,tableView);
        layoutCamps.setSpacing(10);
        layoutCamps.setStyle("-fx-padding: 20;");

        //Action for send the info to Database
        send.setOnAction(_ ->{
            try{
                String getNameField = nameField.getText();
                int getQntField = Integer.parseInt(quantField.getText());
                double getPrice = Double.parseDouble(priceField.getText());
                String getStatus = (String)statusComboBox.getValue();
                //Create ProductDTO
                ProductDTO productDTO = new ProductDTO(getNameField, getQntField, getPrice,getStatus);
                // In case of success, send the information to the API
                boolean success = apiClient.addProduct(productDTO);

                //When the operation is performed,if the code was a success, it shows the success message at the terminal
                if(success){
                    System.out.println("Produto Adicionado com Sucesso!");
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
                    String uname = nameField.getText();
                    int uquantity = Integer.parseInt(quantField.getText());
                    double uprice = Double.parseDouble(priceField.getText());
                    String ustatus = (String) statusComboBox.getValue();

                    //create a new ProductDTO
                    ProductDTO updatedProduct = new ProductDTO(uname, uquantity, uprice, ustatus);
                    //Update the product based on the selected ID
                    boolean success = apiClient.updateProduct(selected.getId(), updatedProduct);

                    if (success) {
                        System.out.println("Produto atualizado com sucesso!");
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


    public static void main(String[] args) {
        launch();
    }
}
