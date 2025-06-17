/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projeto.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import projeto.dao.EspacoDAO;
import projeto.model.Espaco;
/**
 *
 * @author Jvvpa
 */
public class EditarEspacoController {
    @FXML private TextField nomeField;
    @FXML private TextField horariosField;
    @FXML private TextField duracaoField;
    @FXML private TextArea regrasArea;
    @FXML private Label mensagemLabel;

    private Espaco espacoParaEditar;
    private EspacoDAO espacoDAO = new EspacoDAO();

    public void carregarDadosEspaco(Espaco espaco) {
        this.espacoParaEditar = espaco;
        nomeField.setText(espaco.getNome());
        horariosField.setText(espaco.getHorariosDisponiveis());
        duracaoField.setText(String.valueOf(espaco.getTempoMaximoReservaMin()));
        regrasArea.setText(espaco.getRegrasUso());
    }
    
    @FXML
    private void handleSalvar() {
        try {
            espacoParaEditar.setNome(nomeField.getText());
            espacoParaEditar.setHorariosDisponiveis(horariosField.getText());
            espacoParaEditar.setTempoMaximoReservaMin(Integer.parseInt(duracaoField.getText()));
            espacoParaEditar.setRegrasUso(regrasArea.getText());

            espacoDAO.atualizar(espacoParaEditar);

            Stage stage = (Stage) nomeField.getScene().getWindow();
            stage.close();
        } catch (NumberFormatException e) {
            mensagemLabel.setText("A duração deve ser um número.");
        } catch (Exception e) {
            mensagemLabel.setText("Erro ao salvar alterações.");
            e.printStackTrace();
        }
    }
}
