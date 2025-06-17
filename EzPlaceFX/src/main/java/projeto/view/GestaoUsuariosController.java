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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import projeto.App;
import projeto.dao.UsuarioDAO;
import projeto.model.Usuario;
/**
 *
 * @author Jvvpa
 */

public class GestaoUsuariosController implements Initializable {
    @FXML private TableView<Usuario> tabelaUsuarios;
    @FXML private TableColumn<Usuario, Integer> colunaId;
    @FXML private TableColumn<Usuario, String> colunaNome;
    @FXML private TableColumn<Usuario, String> colunaEmail;
    @FXML private TableColumn<Usuario, String> colunaUnidade;
    @FXML private TableColumn<Usuario, String> colunaCpf;
    @FXML private TableColumn<Usuario, String> colunaTelefone;
    @FXML private Button cadastrarUsuarioButton;
    @FXML private Button editarButton;
    @FXML private Button removerButton;
    @FXML private Label mensagemUsuarioLabel;
    
    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private Usuario usuarioLogado;
    
    public void setUsuarioLogado(Usuario usuario) {
        this.usuarioLogado = usuario;
    }
    
    public void initData(Usuario usuario) {
        this.usuarioLogado = usuario;
        carregarUsuarios();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colunaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nomeCompleto"));
        colunaEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colunaUnidade.setCellValueFactory(new PropertyValueFactory<>("unidade"));
        colunaCpf.setCellValueFactory(new PropertyValueFactory<>("CPF"));
        colunaTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
    }    
    
    private void carregarUsuarios() {
        tabelaUsuarios.getItems().clear();
        tabelaUsuarios.getItems().addAll(usuarioDAO.listarTodos());
    }
    
    @FXML
    private void handleCadastrarUsuario() {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/CadastroUsuario.fxml"));
            Parent root = loader.load();
            CadastroUsuarioController controller = loader.getController();
            controller.configurarPara(this.usuarioLogado);
            Stage stage = new Stage();
            stage.setTitle("Cadastrar Novo Usuário");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(tabelaUsuarios.getScene().getWindow());
            stage.setScene(new Scene(root));
            stage.showAndWait();
            carregarUsuarios();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRemoverUsuario() {
        Usuario usuarioSelecionado = tabelaUsuarios.getSelectionModel().getSelectedItem();
        
        if (usuarioSelecionado == null) {
            exibirMensagem(mensagemUsuarioLabel, "Por favor, selecione um usuário na tabela para remover.", true);
            return;
        }
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmação de Exclusão");
        alert.setHeaderText("Você tem certeza que deseja remover o usuário?");
        alert.setContentText("Usuário: " + usuarioSelecionado.getNomeCompleto());
        
        Optional<ButtonType> result = alert.showAndWait();
        
        if (result.isPresent() && result.get() == ButtonType.OK){
            try {
                usuarioDAO.deletar(usuarioSelecionado.getId());
                exibirMensagem(mensagemUsuarioLabel, "Usuário removido com sucesso!", false);
                carregarUsuarios();
            } catch (SQLException e) {
                exibirMensagem(mensagemUsuarioLabel, "Erro ao remover usuário do banco de dados.", true);
                e.printStackTrace();
            }
        }
    }
    
    @FXML
    private void handleEditarUsuario() {
        Usuario usuarioSelecionado = tabelaUsuarios.getSelectionModel().getSelectedItem();
        if (usuarioSelecionado == null) {
            exibirMensagem(mensagemUsuarioLabel, "Por favor, selecione um usuário na tabela para editar.", true);
            return;
        }
        
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/EditarUsuario.fxml"));
            Parent root = loader.load();
            
            EditarUsuarioController controller = loader.getController();
            controller.carregarDadosUsuario(usuarioSelecionado);
            
            Stage stage = new Stage();
            stage.setTitle("Editar Usuário");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(tabelaUsuarios.getScene().getWindow());
            stage.setScene(new Scene(root));
            stage.showAndWait();
            carregarUsuarios();
        } catch (IOException e) {
            e.printStackTrace();
            exibirMensagem(mensagemUsuarioLabel, "Erro ao abrir a tela de edição.", true);
        }
    }
    
    private void exibirMensagem(Label label, String msg, boolean isError) {
        if (label != null) {    
            label.setText(msg);
            label.setTextFill(isError ? Color.RED : Color.GREEN);
        }
    }
}
