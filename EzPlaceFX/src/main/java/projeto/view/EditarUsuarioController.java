/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projeto.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import projeto.dao.UsuarioDAO;
import projeto.model.Usuario;
/**
 *
 * @author Jvvpa
 */
public class EditarUsuarioController {
    @FXML private TextField nomeField;
    @FXML private TextField emailField;
    @FXML private TextField unidadeField;
    @FXML private TextField cpfField;
    @FXML private TextField telefoneField;
    @FXML private Label mensagemLabel;

    private Usuario usuarioParaEditar;
    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    public void carregarDadosUsuario(Usuario usuario) {
        this.usuarioParaEditar = usuario;
        nomeField.setText(usuario.getNomeCompleto());
        emailField.setText(usuario.getEmail());
        unidadeField.setText(usuario.getUnidade());
        cpfField.setText(usuario.getCPF());
        telefoneField.setText(usuario.getTelefone());
    }
    
    @FXML
    private void handleSalvar() {
        // Atualiza o objeto com os novos dados do formulário
        usuarioParaEditar.setNomeCompleto(nomeField.getText());
        usuarioParaEditar.setEmail(emailField.getText());
        usuarioParaEditar.setUnidade(unidadeField.getText()); // Usando seu método
        usuarioParaEditar.setCPF(cpfField.getText());         // Usando seu método
        usuarioParaEditar.setTelefone(telefoneField.getText());

        try {
            usuarioDAO.atualizar(usuarioParaEditar);
            // Fecha a janela de edição
            Stage stage = (Stage) nomeField.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            mensagemLabel.setText("Erro ao salvar alterações.");
            e.printStackTrace();
        }
    }
}
