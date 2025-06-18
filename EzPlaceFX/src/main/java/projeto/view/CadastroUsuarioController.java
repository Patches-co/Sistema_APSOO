package projeto.view;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import projeto.dao.UsuarioDAO;
import projeto.model.Usuario;
import projeto.util.Validador;

public class CadastroUsuarioController implements Initializable{

    @FXML private TextField nomeCompletoField;
    @FXML private TextField emailField;
    @FXML private PasswordField senhaField;
    @FXML private TextField unidadeField;
    @FXML private TextField cpfField;
    @FXML private TextField telefoneField;
    @FXML private Label mensagemLabel;
    @FXML private Label tipoUsuarioLabel;
    @FXML private ComboBox<String> tipoUsuarioComboBox;

    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Validador.aplicarMascara(cpfField, "###.###.###-##");
        Validador.aplicarMascara(telefoneField, "(##) #####-####");
    }

    public void configurarPara(Usuario usuarioLogado) {
        if ("administrador".equals(usuarioLogado.getTipoUsuario())) {
            tipoUsuarioLabel.setVisible(true);
            tipoUsuarioComboBox.setVisible(true);
            tipoUsuarioComboBox.setItems(FXCollections.observableArrayList("morador", "funcionario"));
            tipoUsuarioComboBox.setValue("morador");
        } else {
            tipoUsuarioLabel.setVisible(false);
            tipoUsuarioComboBox.setVisible(false);
        }
    }
    
    @FXML
    private void handleCadastrar() {
        String nome = nomeCompletoField.getText();
        String email = emailField.getText();
        String cpf = cpfField.getText();
        String senha = senhaField.getText();
        String unidade = unidadeField.getText();
        String telefone = telefoneField.getText();
        
        if (nome.isEmpty() || email.isEmpty() || cpf.isEmpty() || senha.isEmpty()) {
            exibirMensagem("Por favor, preencha todos os campos obrigatórios.", true);
            return;
        }
        
        if (!Validador.isEmailValido(email)) {
            exibirMensagem("Formato de e-mail inválido.", true);
            return;
        }
        
        if (!Validador.isCpfValido(cpf)) {
            exibirMensagem("CPF inválido. Verifique os dígitos.", true);
            return;
        }
        
        try {
            // --- LÓGICA CORRIGIDA E FINAL PARA DEFINIR O TIPO DE USUÁRIO ---
            String tipoDeUsuarioFinal;
            
            if (tipoUsuarioComboBox.isVisible()) {
                // Se a ComboBox está visível (admin logado), pega o valor selecionado.
                String valorSelecionado = tipoUsuarioComboBox.getValue();
                if (valorSelecionado == null) {
                    exibirMensagem("Por favor, selecione um tipo de usuário.", true);
                    return;
                }
                tipoDeUsuarioFinal = valorSelecionado.toLowerCase();
            } else {
                // Se estiver invisível (funcionário logado), o tipo é sempre 'morador'.
                tipoDeUsuarioFinal = "morador";
            }
            // --- FIM DA LÓGICA ---

            Usuario novoUsuario = new Usuario();
            novoUsuario.setNomeCompleto(nome);
            novoUsuario.setEmail(email);
            novoUsuario.setSenha(senha);
            novoUsuario.setUnidade(unidade);
            novoUsuario.setCPF(cpf);
            novoUsuario.setTelefone(telefone);
            
            // Atribui o tipo correto que acabamos de determinar
            novoUsuario.setTipoUsuario(tipoDeUsuarioFinal);
            
            usuarioDAO.salvar(novoUsuario);
            exibirMensagem("Usuário cadastrado com sucesso!", false);
            limparCampos();

        } catch (SQLException e) {
            // Este catch agora funciona porque o método salvar() no DAO declara 'throws SQLException'
            exibirMensagem("Erro ao salvar no banco: " + e.getMessage(), true);
            e.printStackTrace();
        }
    }
    
    private void exibirMensagem(String msg, boolean isError) {
        mensagemLabel.setText(msg);
        mensagemLabel.setTextFill(isError ? Color.RED : Color.GREEN);
    }

    private void limparCampos() {
        nomeCompletoField.clear();
        emailField.clear();
        senhaField.clear();
        unidadeField.clear();
        cpfField.clear();
        telefoneField.clear();
    }
}