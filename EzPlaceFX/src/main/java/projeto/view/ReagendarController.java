/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projeto.view;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
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
public class ReagendarController implements Initializable {
    @FXML private Label infoReservaLabel;
    @FXML private DatePicker novaDataPicker;
    @FXML private ComboBox<Integer> horaComboBox;
    @FXML private ComboBox<Integer> minutoComboBox;
    @FXML private Label mensagemLabel;

    private Reserva reservaParaReagendar;
    private ReservaDAO reservaDAO = new ReservaDAO();
    private EspacoDAO espacoDAO = new EspacoDAO();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        popularComboBoxesDeHorario();
    }

    public void carregarDadosReserva(Reserva reserva) {
        this.reservaParaReagendar = reserva;
        infoReservaLabel.setText("Reagendando: " + reserva.getNomeEspaco());
        novaDataPicker.setValue(reserva.getDataReserva());
        horaComboBox.setValue(reserva.getHorarioInicio().getHour());
        minutoComboBox.setValue(reserva.getHorarioInicio().getMinute());
    }
    
    @FXML
    private void handleConfirmarReagendamento() {
        LocalDate novaData = novaDataPicker.getValue();
        Integer novaHora = horaComboBox.getValue();
        Integer novoMinuto = minutoComboBox.getValue();

        if (novaData == null || novaHora == null || novoMinuto == null) {
            Validador.mostrarAlerta("Campos Incompletos", "Por favor, selecione a nova data e horário.", AlertType.WARNING);
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
            
            LocalTime novoHorarioInicio = LocalTime.of(novaHora, novoMinuto);
            LocalTime novoHorarioFim = novoHorarioInicio.plusMinutes(espaco.getTempoMaximoReservaMin());

            String horariosDisponiveis = espaco.getHorariosDisponiveis();
            if (horariosDisponiveis != null && !horariosDisponiveis.isEmpty() && horariosDisponiveis.contains("-")) {
                LocalTime horarioAbertura = LocalTime.parse(horariosDisponiveis.split("-")[0].trim());
                LocalTime horarioFechamento = LocalTime.parse(horariosDisponiveis.split("-")[1].trim());
                if (novoHorarioInicio.isBefore(horarioAbertura) || novoHorarioFim.isAfter(horarioFechamento)) {
                    Validador.mostrarAlerta("Horário Inválido", "O novo horário está fora do período de funcionamento (" + horariosDisponiveis + ").", AlertType.WARNING);
                    return;
                }
            }

            if (!reservaDAO.verificarDisponibilidade(espaco.getId(), novaData, novoHorarioInicio, novoHorarioFim)) {
                 Validador.mostrarAlerta("Conflito de Horário", "Este novo horário já está ocupado.", AlertType.WARNING);
                 return;
            }

            reservaParaReagendar.setDataReserva(novaData);
            reservaParaReagendar.setHorarioInicio(novoHorarioInicio);
            reservaParaReagendar.setHorarioFim(novoHorarioFim);
            reservaDAO.atualizar(reservaParaReagendar);
            
            Validador.mostrarAlerta("Sucesso", "Reserva reagendada com sucesso!", AlertType.INFORMATION);
            
            Stage stage = (Stage) horaComboBox.getScene().getWindow();
            stage.close();

        } catch (SQLException e) {
            mensagemLabel.setText("Erro ao salvar reagendamento.");
            e.printStackTrace();
        }
    }
    
    private void popularComboBoxesDeHorario() {
        horaComboBox.getItems().addAll(IntStream.rangeClosed(0, 23).boxed().collect(Collectors.toList()));
        minutoComboBox.getItems().addAll(0, 15, 30, 45);
    }
}
