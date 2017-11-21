package javafxmvc.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafxmvc.modelo.dao.ClienteDAO;
import javafxmvc.modelo.dao.ProdutoDAO;
import javafxmvc.modelo.database.Database;
import javafxmvc.modelo.database.DatabaseFactory;
import javafxmvc.modelo.domain.Cliente;
import javafxmvc.modelo.domain.ItemDeVenda;
import javafxmvc.modelo.domain.Produto;
import javafxmvc.modelo.domain.Venda;

public class FXMLAnchorPaneProcessosVendasDialogController implements Initializable {

    @FXML
    private ComboBox comboBoxVendasCliente;
    @FXML
    private TextField textFieldVendasValor;
    @FXML
    private DatePicker DatePickerData;
    @FXML
    private TableView<ItemDeVenda> tableViewVendasProduto;
    @FXML
    private ComboBox comboBoxVendasProduto;
    @FXML
    private CheckBox checkBoxVendasPago;
    @FXML
    private Button buttonAdicionar;
    @FXML
    private Button buttonConfirmar;
    @FXML
    private Button buttonCancelar;
    
    private List<Cliente> listClientes;
    private List<Produto> listProdutos;
    private ObservableList<Cliente> observableListClientes;
    private ObservableList<Produto> observableListProdutos;
    private ObservableList<ItemDeVenda> observableListItensDeVenda;
    
    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final ProdutoDAO produtoDAO = new ProdutoDAO();
    
    private Stage dialogStage;
    private boolean buttonConfirmarClicked = false;
    private Venda venda;
    @FXML
    private TableColumn<ItemDeVenda, String> tableColumnItemDeVendaProduto;
    @FXML
    private TableColumn<ItemDeVenda, Integer> tableColumnItemDeVendaQuantidade;
    @FXML
    private TableColumn<ItemDeVenda, Double> tableColumnItemDeVendaValor;
    @FXML
    private TextField textFieldItemDeVendaQuantidade;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        clienteDAO.setConnection(connection);
        produtoDAO.setConnection(connection);
        carregarTableViewVendasProduto();
        carregarComboBoxClientes();
        carregarComboBoxProdutos();
        tableColumnItemDeVendaProduto.setCellValueFactory(new PropertyValueFactory<>("produto"));
        tableColumnItemDeVendaQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        tableColumnItemDeVendaValor.setCellValueFactory(new PropertyValueFactory<>("valor"));
    }    
    @FXML
    private void handleButtonAdicionar() {
        Produto produto;
        ItemDeVenda itemDeVenda = new ItemDeVenda();
        if (comboBoxVendasProduto.getSelectionModel().getSelectedItem() != null) {
            produto = (Produto) comboBoxVendasProduto.getSelectionModel().getSelectedItem();
            if (produto.getQuantidade() >= Integer.parseInt(textFieldItemDeVendaQuantidade.getText())) {
                itemDeVenda.setProduto((Produto) comboBoxVendasProduto.getSelectionModel().getSelectedItem());
                itemDeVenda.setQuantidade(Integer.parseInt(textFieldItemDeVendaQuantidade.getText()));
                itemDeVenda.setValor(itemDeVenda.getProduto().getPreco() * itemDeVenda.getQuantidade());
                venda.getItensDeVenda().add(itemDeVenda);
                venda.setValor(venda.getValor() + itemDeVenda.getValor());
                observableListItensDeVenda = FXCollections.observableArrayList(venda.getItensDeVenda());
                tableViewVendasProduto.setItems(observableListItensDeVenda);
                textFieldVendasValor.setText(String.format("%.2f", venda.getValor()));
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Problemas na escolha do produto!");
                alert.setContentText("Não existe a quantidade de produtos disponíveis no estoque!");
                alert.show();
            }
        }
    }
    @FXML
    private void handleButtonConfirmar() {
        if (validarEntradaDeDados()) {
            venda.setCliente((Cliente) comboBoxVendasCliente.getSelectionModel().getSelectedItem());
            venda.setPago(checkBoxVendasPago.isSelected());
            venda.setData(DatePickerData.getValue());
            buttonConfirmarClicked = true;
            dialogStage.close();
        }
    }
    public Stage getDialogStage() {
        return dialogStage;
    }
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    public Venda getVenda() {
        return venda;
    }
    public void setVenda(Venda venda) {
        this.venda = venda;
    }
    @FXML
    private void handleButtonCancelar() {
        getDialogStage().close();
    }
    private void carregarTableViewVendasProduto() {
        tableColumnItemDeVendaProduto.setCellValueFactory(new PropertyValueFactory<>("produto"));
        tableColumnItemDeVendaQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        tableColumnItemDeVendaValor.setCellValueFactory(new PropertyValueFactory<>("valor"));
        
        listProdutos = produtoDAO.listar();

        observableListProdutos = FXCollections.observableArrayList(listProdutos);
        tableViewVendasProduto.setItems(observableListItensDeVenda);
    }
    private boolean selecionarItemTableViewVendasProdutos(ItemDeVenda newValue) throws IOException {
        return true;
    }

    private void carregarComboBoxClientes() {
        
    }
    public boolean isButtonConfirmarClicked() {
        return buttonConfirmarClicked;
    }
    private boolean validarEntradaDeDados() {
        String errorMessage = "";
        if (comboBoxVendasCliente.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "Cliente inválido!\n";
        }
        if (DatePickerData.getValue() == null) {
            errorMessage += "Data inválida!\n";
        }
        if (observableListItensDeVenda == null) {
            errorMessage += "Itens de Venda inválidos!\n";
        }
        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Mostrando a mensagem de erro
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro no cadastro");
            alert.setHeaderText("Campos inválidos, por favor, corrija...");
            alert.setContentText(errorMessage);
            alert.show();
            return false;
        }
    }
    private void carregarComboBoxProdutos() {
        listProdutos = produtoDAO.listar();
        observableListProdutos = FXCollections.observableArrayList(listProdutos);
        comboBoxVendasProduto.setItems(observableListProdutos);
    }
}