/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projeto.view;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import projeto.App;
import projeto.dao.UsuarioDAO;
import projeto.model.Usuario;

/**
 *
 * @author Jvvpa
 */
public class LoginController {
    @FXML private TextField emailField;
    @FXML private PasswordField senhaField;
    @FXML private Label mensagemLabel;

    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    @FXML
    private void handleLogin() {
        String email = emailField.getText();
        String senha = senhaField.getText();

        if (email.isEmpty() || senha.isEmpty()) {
            mensagemLabel.setText("Por favor, preencha todos os campos.");
            return;
        }

        Usuario usuario = usuarioDAO.buscarPorEmail(email);

        if (usuario != null && usuario.getSenha().equals(senha)) {
            String tipoUsuario = usuario.getTipoUsuario();
            
            if ("administrador".equals(tipoUsuario) || "funcionario".equals(tipoUsuario)) {
                try {
                    abrirDashboard(usuario);
                    Stage stageLogin = (Stage) emailField.getScene().getWindow();
                    stageLogin.close();
                } catch (Exception e) {
                    mostrarAlertaDeErro(e);
                }
            } else {
                mensagemLabel.setText("Acesso negado, acesso exclusivo para funcionarios.");
            }
        } else {
            mensagemLabel.setText("Email ou senha incorretos.");
        }
    }
    
    private void abrirDashboard(Usuario usuarioLogado) throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/Dashboard.fxml"));
        Parent root = loader.load();

        DashboardController controller = loader.getController();
        controller.setUsuarioLogado(usuarioLogado);

        Stage stage = new Stage();
        stage.setTitle("EzPlace - Painel de Controle");
        
        Scene scene = new Scene(root, 1200, 800);
        
        URL cssURL = App.class.getResource("/styles/styles.css");
        if (cssURL != null) {
            scene.getStylesheets().add(cssURL.toExternalForm());
        } else {
            System.err.println("AVISO: Não foi possível encontrar o arquivo styles.css.");
        }
        
        stage.setScene(scene);
        stage.show();
    }
    
    private void mostrarAlertaDeErro(Exception e) {
        e.printStackTrace();

        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Erro Crítico na Aplicação");
        alert.setHeaderText("Ocorreu um erro inesperado ao tentar carregar a próxima tela.");
        alert.setContentText("A causa do erro foi: " + e.getMessage());

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String exceptionText = sw.toString();

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        
        alert.getDialogPane().setExpandableContent(textArea);
        alert.getDialogPane().setExpanded(true);
        alert.showAndWait();
    }
}
