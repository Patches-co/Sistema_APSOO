/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projeto.view;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import projeto.App;
import projeto.model.Usuario;
/**
 *
 * @author Jvvpa
 */
public class DashboardController implements Initializable{
    @FXML private Label nomeUsuarioLabel;
    @FXML private Label tipoUsuarioLabel;
    @FXML private Button btnReservas;
    @FXML private Button btnGestaoUsuarios;
    @FXML private Button btnGestaoEspacos;
    @FXML private AnchorPane painelConteudo;

    private Usuario usuarioLogado;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
    public void setUsuarioLogado(Usuario usuario) {
        this.usuarioLogado = usuario;
        
        nomeUsuarioLabel.setText(usuario.getNomeCompleto());
        tipoUsuarioLabel.setText(usuario.getTipoUsuario());
        
        if (!"administrador".equals(usuario.getTipoUsuario())) {
            btnGestaoUsuarios.setVisible(false);
            btnGestaoEspacos.setVisible(false);
        }
        
        mostrarTelaReservas();
    }
    
    private void carregarTela(String nomeTela) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/" + nomeTela + ".fxml"));
            Parent novaTela = loader.load();
            
            if (loader.getController() instanceof GestaoUsuariosController) {
                ((GestaoUsuariosController) loader.getController()).initData(usuarioLogado);
            } else if (loader.getController() instanceof GestaoEspacosController) {
                ((GestaoEspacosController) loader.getController()).initData();
            } else if (loader.getController() instanceof ReservasController) {
                ((ReservasController) loader.getController()).initData(usuarioLogado);
            }
            
            painelConteudo.getChildren().setAll(novaTela);
        } catch (Exception e) {
            mostrarAlertaDeErro(e, "Erro ao carregar a tela: " + nomeTela);
        }
    }
    
    @FXML
    private void mostrarTelaReservas() {
        carregarTela("ViewReservas");
    }

    @FXML
    private void mostrarTelaGestaoUsuarios() {
        carregarTela("ViewGestaoUsuarios");
    }

    @FXML
    private void mostrarTelaGestaoEspacos() {
        carregarTela("ViewGestaoEspacos");
    }
    
    private void mostrarAlertaDeErro(Exception e, String headerText) {
        e.printStackTrace();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro Crítico");
        alert.setHeaderText(headerText);
        alert.setContentText(e.getMessage());
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        TextArea textArea = new TextArea(sw.toString());
        textArea.setEditable(false);
        textArea.setWrapText(true);
        alert.getDialogPane().setExpandableContent(textArea);
        alert.getDialogPane().setExpanded(true);
        alert.showAndWait();
    }
}
