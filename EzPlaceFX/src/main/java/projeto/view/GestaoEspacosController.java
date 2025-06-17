/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projeto.view;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import projeto.App;
import projeto.dao.EspacoDAO;
import projeto.model.Espaco;
/**
 *
 * @author Jvvpa
 */


public class GestaoEspacosController implements Initializable {

    @FXML private TableView<Espaco> tabelaEspacos;
    @FXML private TableColumn<Espaco, Integer> colunaIdEspaco;
    @FXML private TableColumn<Espaco, String> colunaNomeEspaco;
    @FXML private TableColumn<Espaco, String> colunaHorarios;
    @FXML private TableColumn<Espaco, Integer> colunaDuracao;
    
    @FXML private Button cadastrarEspacoButton;
    @FXML private Button editarEspacoButton;
    @FXML private Button removerEspacoButton;
    @FXML private Label mensagemEspacoLabel;

    private EspacoDAO espacoDAO = new EspacoDAO();
    
    public void initData() {
        carregarEspacos();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colunaIdEspaco.setCellValueFactory(new PropertyValueFactory<>("id"));
        colunaNomeEspaco.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaHorarios.setCellValueFactory(new PropertyValueFactory<>("horariosDisponiveis"));
        colunaDuracao.setCellValueFactory(new PropertyValueFactory<>("tempoMaximoReservaMin"));
    }

    private void carregarEspacos() {
        tabelaEspacos.getItems().clear();
        tabelaEspacos.getItems().addAll(espacoDAO.listarTodos());
    }
    
    @FXML
    private void handleCadastrarEspaco() {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/CadastroEspaco.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Cadastrar Novo Espaço");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(tabelaEspacos.getScene().getWindow());
            stage.setScene(new Scene(root));
            stage.showAndWait();
            
            carregarEspacos();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEditarEspaco() {
        Espaco espacoSelecionado = tabelaEspacos.getSelectionModel().getSelectedItem();
        if (espacoSelecionado == null) {
            exibirMensagem("Selecione um espaço para editar.", true);
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/EditarEspaco.fxml"));
            Parent root = loader.load();
            EditarEspacoController controller = loader.getController();
            controller.carregarDadosEspaco(espacoSelecionado);
            Stage stage = new Stage();
            stage.setTitle("Editar Espaço");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(tabelaEspacos.getScene().getWindow());
            stage.setScene(new Scene(root));
            stage.showAndWait();
            carregarEspacos();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRemoverEspaco() {
        Espaco espacoSelecionado = tabelaEspacos.getSelectionModel().getSelectedItem();
        if (espacoSelecionado == null) {
            exibirMensagem("Selecione um espaço para remover.", true);
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmação de Exclusão");
        alert.setHeaderText("Remover '" + espacoSelecionado.getNome() + "'?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                espacoDAO.deletar(espacoSelecionado.getId());
                exibirMensagem("Espaço removido com sucesso!", false);
                carregarEspacos();
            } catch (SQLException e) {
                exibirMensagem("Erro ao remover o espaço.", true);
                e.printStackTrace();
            }
        }
    }

    private void exibirMensagem(String msg, boolean isError) {
        mensagemEspacoLabel.setText(msg);
        mensagemEspacoLabel.setTextFill(isError ? Color.RED : Color.GREEN);
    }
}
