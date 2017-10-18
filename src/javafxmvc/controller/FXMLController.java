package javafxmvc.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;

public class FXMLController implements Initializable {
    @FXML
    private MenuItem menuItemCadastroClientes;
    @FXML
    private MenuItem menuProcessoVendas;
    @FXML
    private MenuItem menuItemGraficosVendasporMes;
    @FXML
    private MenuItem menuItemRelatioriosEstoque;
    @FXML
    private AnchorPane anchorPane;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }  
    @FXML
    public void handleMenuItemCadastrosClientes() throws IOException {
    AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource("/javafxmvc/view/FXMLAnchorPaneCadastrosClientes.fxml"));
    anchorPane.getChildren().setAll(a);
    }
}