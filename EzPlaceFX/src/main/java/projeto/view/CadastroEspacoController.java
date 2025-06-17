/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projeto.view;

/**
 *
 * @author Jvvpa
 */
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import projeto.dao.EspacoDAO;
import projeto.model.Espaco;

public class CadastroEspacoController {

    @FXML private TextField nomeEspacoField;
    @FXML private TextField horariosField;
    @FXML private TextField tempoMaximoField;
    @FXML private TextArea regrasArea;
    @FXML private Label mensagemLabel;

    private EspacoDAO espacoDAO = new EspacoDAO();

    @FXML
    private void handleCadastrarEspaco() {
        String nome = nomeEspacoField.getText();
        String horarios = horariosField.getText();
        String tempoMaximoStr = tempoMaximoField.getText();
        String regras = regrasArea.getText();

        if (nome.isEmpty() || horarios.isEmpty() || tempoMaximoStr.isEmpty()) {
            exibirMensagem("Por favor, preencha todos os campos obrigatórios.", true);
            return;
        }

        try {
            int tempoMaximo = Integer.parseInt(tempoMaximoStr);

            Espaco novoEspaco = new Espaco();
            novoEspaco.setNome(nome);
            novoEspaco.setHorariosDisponiveis(horarios);
            novoEspaco.setTempoMaximoReservaMin(tempoMaximo);
            novoEspaco.setRegrasUso(regras);

            espacoDAO.salvar(novoEspaco);

            exibirMensagem("Espaço '" + nome + "' cadastrado com sucesso!", false);
            limparCampos();

        } catch (NumberFormatException e) {
            exibirMensagem("Erro: O tempo máximo deve ser um número inteiro.", true);
        } catch (Exception e) {
            exibirMensagem("Erro ao salvar o espaço: " + e.getMessage(), true);
        }
    }

    private void exibirMensagem(String msg, boolean isError) {
        mensagemLabel.setText(msg);
        mensagemLabel.setTextFill(isError ? Color.RED : Color.GREEN);
    }

    private void limparCampos() {
        nomeEspacoField.clear();
        horariosField.clear();
        tempoMaximoField.clear();
        regrasArea.clear();
    }
}