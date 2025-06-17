package projeto.view;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import projeto.dao.EspacoDAO;
import projeto.dao.ReservaDAO;
import projeto.dao.UsuarioDAO;
import projeto.model.Espaco;
import projeto.model.Reserva;
import projeto.model.Usuario;
import projeto.service.EmailService;
import projeto.util.Validador;

public class FormularioReservaController implements Initializable {
    private static final int LIMITE_MENSAL_POR_MORADOR = 5;

    @FXML private ListView<Espaco> listaEspacos;
    @FXML private Label nomeEspacoLabel;
    @FXML private ComboBox<Usuario> moradorComboBox;
    @FXML private DatePicker datePicker;
    @FXML private TextField horarioField;
    @FXML private Button reservarButton;
    @FXML private Label mensagemReservaLabel;

    private EspacoDAO espacoDAO = new EspacoDAO();
    private ReservaDAO reservaDAO = new ReservaDAO();
    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        carregarEspacos();
        carregarMoradoresComboBox();
        listaEspacos.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                if (newValue != null) {
                    nomeEspacoLabel.setText(newValue.getNome());
                }
            });
    }
    
    public void preencherDados(LocalDate data, LocalTime hora) {
        datePicker.setValue(data);
        horarioField.setText(hora.format(DateTimeFormatter.ofPattern("HH:mm")));
    }

    private void carregarEspacos() {
        listaEspacos.getItems().setAll(espacoDAO.listarTodos());
    }

    private void carregarMoradoresComboBox() {
        moradorComboBox.getItems().setAll(usuarioDAO.listarMoradores());
    }

    @FXML
    private void handleReservar() {
        Espaco espacoSelecionado = listaEspacos.getSelectionModel().getSelectedItem();
        Usuario moradorParaReserva = moradorComboBox.getSelectionModel().getSelectedItem();
        LocalDate data = datePicker.getValue();
        String horarioStr = horarioField.getText();

        if (espacoSelecionado == null || moradorParaReserva == null || data == null || horarioStr.isEmpty()) {
            Validador.mostrarAlerta("Campos Incompletos", "Por favor, preencha todos os campos.", AlertType.WARNING);
            return;
        }

        try {
            int contagemReservas = reservaDAO.contarReservasDoUsuarioNoPeriodo(moradorParaReserva.getId(), data.withDayOfMonth(1), data.withDayOfMonth(data.lengthOfMonth()));
            if (contagemReservas >= LIMITE_MENSAL_POR_MORADOR) {
                Validador.mostrarAlerta("Limite Atingido", "O morador " + moradorParaReserva.getNomeCompleto() + " já atingiu o limite de " + LIMITE_MENSAL_POR_MORADOR + " reservas mensais.", AlertType.WARNING);
                return;
            }

            LocalTime horarioInicio = LocalTime.parse(horarioStr);
            LocalTime horarioFim = horarioInicio.plusMinutes(espacoSelecionado.getTempoMaximoReservaMin());
            String horariosDisponiveis = espacoSelecionado.getHorariosDisponiveis();
            if (horariosDisponiveis != null && !horariosDisponiveis.isEmpty() && horariosDisponiveis.contains("-")) {
                LocalTime horarioAbertura = LocalTime.parse(horariosDisponiveis.split("-")[0].trim());
                LocalTime horarioFechamento = LocalTime.parse(horariosDisponiveis.split("-")[1].trim());
                if (horarioInicio.isBefore(horarioAbertura) || horarioFim.isAfter(horarioFechamento)) {
                    Validador.mostrarAlerta("Horário Inválido", "O horário solicitado está fora do período de funcionamento (" + horariosDisponiveis + ").", AlertType.WARNING);
                    return;
                }
            }
            
            if (reservaDAO.verificarDisponibilidade(espacoSelecionado.getId(), data, horarioInicio, horarioFim)) {
                Reserva novaReserva = new Reserva();
                novaReserva.setIdEspaco(espacoSelecionado.getId());
                novaReserva.setIdUsuario(moradorParaReserva.getId());
                novaReserva.setDataReserva(data);
                novaReserva.setHorarioInicio(horarioInicio);
                novaReserva.setHorarioFim(horarioFim);
                
                reservaDAO.salvar(novaReserva);

                String assunto = "Confirmação de Reserva - EzPlace";
                String corpo = "Olá, " + moradorParaReserva.getNomeCompleto() + ".\n\n" + "Uma reserva para o espaço '" + espacoSelecionado.getNome() + "' foi confirmada em seu nome com sucesso.\n\n" + "Data: " + novaReserva.getDataReserva() + "\n" + "Horário: de " + novaReserva.getHorarioInicio() + " até " + novaReserva.getHorarioFim() + "\n\n" + "Atenciosamente,\nEquipe EzPlace.";
                EmailService.enviarEmail(moradorParaReserva.getEmail(), assunto, corpo);
                
                Validador.mostrarAlerta("Sucesso", "Reserva confirmada para " + moradorParaReserva.getNomeCompleto() + "!", AlertType.INFORMATION);
                
                Stage stage = (Stage) reservarButton.getScene().getWindow();
                stage.close();
                
            } else {
                Validador.mostrarAlerta("Conflito de Horário", "Horário indisponível (já reservado).", AlertType.WARNING);
            }
        } catch (DateTimeParseException e) {
            Validador.mostrarAlerta("Erro de Formato", "Formato de hora inválido. Use HH:mm (ex: 14:00).", AlertType.ERROR);
        } catch (SQLException e) {
            Validador.mostrarAlerta("Erro de Banco de Dados", "Ocorreu um erro ao salvar a reserva.", AlertType.ERROR);
            e.printStackTrace();
        }
    }
}