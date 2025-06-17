/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projeto.view;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import projeto.dao.EspacoDAO;
import projeto.dao.ReservaDAO;
import projeto.model.Espaco;
import projeto.model.Reserva;
import projeto.util.Validador;
/**
 *
 * @author Jvvpa
 */
public class ReagendarController {
    @FXML private Label infoReservaLabel;
    @FXML private DatePicker novaDataPicker;
    @FXML private TextField novoHorarioField;
    @FXML private Label mensagemLabel;

    private Reserva reservaParaReagendar;
    private ReservaDAO reservaDAO = new ReservaDAO();
    private EspacoDAO espacoDAO = new EspacoDAO();

    public void carregarDadosReserva(Reserva reserva) {
        this.reservaParaReagendar = reserva;
        infoReservaLabel.setText("Reagendando: " + reserva.getNomeEspaco() + " para " + reserva.getNomeUsuario());
        novaDataPicker.setValue(reserva.getDataReserva());
        novoHorarioField.setText(reserva.getHorarioInicio().toString());
    }
    
    @FXML
    private void handleConfirmarReagendamento() {
        LocalDate novaData = novaDataPicker.getValue();
        String novoHorarioStr = novoHorarioField.getText();
        // Verifica campos vazios
        if (novaData == null || novoHorarioStr.isEmpty()) {
            mensagemLabel.setText("Por favor, preencha a nova data e horário.");
            return;
        }
        // Verifica se o horário ja está ocupado
        if (novaData.isBefore(LocalDate.now())) {
            Validador.mostrarAlerta("Data Inválida", "Não é possível reagendar para uma data que já passou.", AlertType.WARNING);
            return;
        }

        try {
            Espaco espaco = espacoDAO.buscarPorId(reservaParaReagendar.getIdEspaco());
            if (espaco == null) {
                mensagemLabel.setText("Espaco não encontrado.");
                return;
            }
            
            LocalTime novoHorarioInicio = LocalTime.parse(novoHorarioStr);
            LocalTime novoHorarioFim = novoHorarioInicio.plusMinutes(espaco.getTempoMaximoReservaMin());

            String horariosDisponiveis = espaco.getHorariosDisponiveis();
            if (horariosDisponiveis != null && !horariosDisponiveis.isEmpty() && horariosDisponiveis.contains("-")) {
                String[] partes = horariosDisponiveis.split("-");
                LocalTime horarioAbertura = LocalTime.parse(partes[0].trim());
                LocalTime horarioFechamento = LocalTime.parse(partes[1].trim());

                if (novoHorarioInicio.isBefore(horarioAbertura) || novoHorarioFim.isAfter(horarioFechamento)) {
                    mensagemLabel.setText("Erro: Fora do horário de funcionamento (" + horariosDisponiveis + ").");
                    return;
                }
            }

            if (!reservaDAO.verificarDisponibilidade(espaco.getId(), novaData, novoHorarioInicio, novoHorarioFim)) {
                 mensagemLabel.setText("Este novo horário já está ocupado.");
                 return;
            }

            reservaParaReagendar.setDataReserva(novaData);
            reservaParaReagendar.setHorarioInicio(novoHorarioInicio);
            reservaParaReagendar.setHorarioFim(novoHorarioFim);

            reservaDAO.atualizar(reservaParaReagendar);
            
            Stage stage = (Stage) novoHorarioField.getScene().getWindow();
            stage.close();

        } catch (DateTimeParseException e) {
            mensagemLabel.setText("Formato de hora inválido. Use HH:mm.");
        } catch (SQLException e) {
            mensagemLabel.setText("Erro ao salvar reagendamento.");
            e.printStackTrace();
        }
    }
}
