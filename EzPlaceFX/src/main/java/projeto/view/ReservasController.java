package projeto.view;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import projeto.App;
import projeto.dao.EspacoDAO;
import projeto.dao.ReservaDAO;
import projeto.dao.UsuarioDAO;
import projeto.model.Espaco;
import projeto.model.Reserva;
import projeto.model.Usuario;
import projeto.util.Validador;
import org.controlsfx.control.textfield.TextFields;

public class ReservasController implements Initializable {
    
    private static final int HORA_INICIO = 7;
    private static final int HORA_FIM = 22;
    private static final int ALTURA_LINHA_HORA = 30;

    @FXML private Button btnSemanaAnterior, btnProximaSemana, btnNovaReserva, cancelarButton, reagendarButton;
    @FXML private Label labelSemana, mensagemCancelamentoLabel;
    @FXML private ComboBox<Espaco> filtroEspacoComboBox;
    @FXML private GridPane gridCalendario;
    @FXML private TableView<Reserva> tabelaReservas, tabelaHistorico;
    @FXML private TableColumn<Reserva, String> colunaEspaco, colunaHistoricoUsuario, colunaHistoricoEspaco;
    @FXML private TableColumn<Reserva, LocalDate> colunaData, colunaHistoricoData;
    @FXML private TableColumn<Reserva, LocalTime> colunaHorario, colunaHistoricoHorario;
    @FXML private TableColumn<Reserva, Integer> colunaReservaId;
    @FXML private ComboBox<Usuario> moradorReservasComboBox;

    private EspacoDAO espacoDAO = new EspacoDAO();
    private ReservaDAO reservaDAO = new ReservaDAO();
    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private Usuario usuarioLogado;
    private LocalDate dataReferencia;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.dataReferencia = LocalDate.now();
        configurarTabelas();
        filtroEspacoComboBox.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldV, espaco) -> atualizarVisualizacaoCalendario());
        desenharEstruturaCalendario();
    }
    
    public void initData(Usuario usuario) {
        this.usuarioLogado = usuario;
        carregarComboBoxes();
        atualizarVisualizacaoCompleta();
        configurarBuscaComboBox();
    }
    
    private void configurarTabelas() {
        colunaEspaco.setCellValueFactory(new PropertyValueFactory<>("nomeEspaco"));
        colunaData.setCellValueFactory(new PropertyValueFactory<>("dataReserva"));
        colunaHorario.setCellValueFactory(new PropertyValueFactory<>("horarioInicio"));
        colunaReservaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colunaHistoricoUsuario.setCellValueFactory(new PropertyValueFactory<>("nomeUsuario"));
        colunaHistoricoEspaco.setCellValueFactory(new PropertyValueFactory<>("nomeEspaco"));
        colunaHistoricoData.setCellValueFactory(new PropertyValueFactory<>("dataReserva"));
        colunaHistoricoHorario.setCellValueFactory(new PropertyValueFactory<>("horarioInicio"));
    }

    private void configurarListeners() {
        filtroEspacoComboBox.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldV, espaco) -> atualizarVisualizacaoCalendario());
            
        moradorReservasComboBox.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, moradorSelecionado) -> {
                System.out.println("--- [DEBUG Listener] Seleção no ComboBox mudou! ---");
                if (moradorSelecionado != null && moradorSelecionado instanceof Usuario) {
                    System.out.println("--- [DEBUG Listener] Morador selecionado: " + moradorSelecionado.getNomeCompleto() + " (ID: " + moradorSelecionado.getId() + ") ---");
                    carregarReservasDoMorador(moradorSelecionado);
                } else {
                    System.out.println("--- [DEBUG Listener] Seleção é nula ou não é um objeto Usuario. Limpando tabela. ---");
                    if(tabelaReservas != null) tabelaReservas.getItems().clear();
                }
            }
        );
    }
    
    private void desenharEstruturaCalendario() {
        gridCalendario.getColumnConstraints().clear();
        gridCalendario.getRowConstraints().clear();
        gridCalendario.getColumnConstraints().add(new ColumnConstraints(60));
        for (int i = 0; i < 7; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setHgrow(Priority.ALWAYS);
            gridCalendario.getColumnConstraints().add(col);
        }
        gridCalendario.getRowConstraints().add(new RowConstraints(30));
        for (int i = HORA_INICIO; i <= HORA_FIM; i++) {
            gridCalendario.getRowConstraints().add(new RowConstraints(ALTURA_LINHA_HORA));
        }
        for (int i = HORA_INICIO; i <= HORA_FIM; i++) {
            Label labelHora = new Label(String.format("%02d:00", i));
            GridPane.setHalignment(labelHora, HPos.RIGHT);
            gridCalendario.add(labelHora, 0, (i - HORA_INICIO) + 1);
        }
    }
    
    private void atualizarVisualizacaoCalendario() {
        gridCalendario.getChildren().removeIf(node -> {
            Integer col = GridPane.getColumnIndex(node);
            return col != null && col > 0;
        });
        
        LocalDate inicioSemana = dataReferencia.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        LocalDate fimSemana = inicioSemana.plusDays(6);
        
        labelSemana.setText(inicioSemana.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " - " + fimSemana.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        
        String[] diasNomes = {"Dom", "Seg", "Ter", "Qua", "Qui", "Sex", "Sáb"};
        for (int i = 0; i < 7; i++) {
            LocalDate diaAtual = inicioSemana.plusDays(i);
            Label labelDia = new Label(diasNomes[i] + "\n" + diaAtual.format(DateTimeFormatter.ofPattern("dd/MM")));
            labelDia.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
            labelDia.setMaxWidth(Double.MAX_VALUE);
            GridPane.setHalignment(labelDia, HPos.CENTER);
            gridCalendario.add(labelDia, i + 1, 0);
        }
        
        preencherGradeComPaineisClicaveis(inicioSemana);
        preencherReservasNaGrade(inicioSemana, fimSemana);
    }

    private void preencherGradeComPaineisClicaveis(LocalDate inicioSemana) {
        for (int col = 1; col <= 7; col++) {
            for (int row = 1; row <= (HORA_FIM - HORA_INICIO) + 1; row++) {
                AnchorPane painelClicavel = new AnchorPane();
                painelClicavel.getStyleClass().add("celula-calendario");
                
                final int colunaFinal = col;
                final int linhaFinal = row;
                
                painelClicavel.setOnMouseClicked(event -> {
                    LocalDate dataSelecionada = inicioSemana.plusDays(colunaFinal - 1);
                    LocalTime horaSelecionada = LocalTime.of(HORA_INICIO + linhaFinal - 1, 0);
                    abrirFormulario(dataSelecionada, horaSelecionada);
                });
                gridCalendario.add(painelClicavel, col, row, 1, 1);
            }
        }
    }
    
    private void preencherReservasNaGrade(LocalDate inicioSemana, LocalDate fimSemana) {
        Espaco espacoFiltrado = filtroEspacoComboBox.getSelectionModel().getSelectedItem();
        List<Reserva> reservasDaSemana = reservaDAO.listarPorPeriodo(inicioSemana, fimSemana);
        
        for (Reserva reserva : reservasDaSemana) {
            if (espacoFiltrado != null && reserva.getIdEspaco() != espacoFiltrado.getId()) {
                continue;
            }
            
            int coluna = reserva.getDataReserva().getDayOfWeek().getValue();
            if (coluna == 7) { coluna = 0; }
            coluna = coluna + 1;

            int linha = reserva.getHorarioInicio().getHour() - HORA_INICIO + 1;
            
            long duracaoEmMinutos = java.time.Duration.between(reserva.getHorarioInicio(), reserva.getHorarioFim()).toMinutes();
            
            int rowSpan = (int) Math.ceil(duracaoEmMinutos / 60.0);
            if (rowSpan == 0) rowSpan = 1;

            if (linha >= 1) {
                AnchorPane painelReserva = new AnchorPane();
                painelReserva.setStyle("-fx-background-color: #3f51b5; -fx-border-color: #1a237e; -fx-background-radius: 5; -fx-opacity: 0.9;");
                
                Text textoReserva = new Text(reserva.getNomeEspaco() + "\n(" + reserva.getNomeUsuario() + ")");
                textoReserva.setFill(Color.WHITE);
                textoReserva.setWrappingWidth(90);
                AnchorPane.setTopAnchor(textoReserva, 5.0);
                AnchorPane.setLeftAnchor(textoReserva, 5.0);
                painelReserva.getChildren().add(textoReserva);
                
                gridCalendario.add(painelReserva, coluna, linha, 1, rowSpan);
            }
        }
    }

    @FXML
    private void handleSemanaAnterior() {
        dataReferencia = dataReferencia.minusWeeks(1);
        atualizarVisualizacaoCalendario();
    }

    @FXML
    private void handleProximaSemana() {
        dataReferencia = dataReferencia.plusWeeks(1);
        atualizarVisualizacaoCalendario();
    }

    @FXML
    private void handleAbrirFormularioReserva() {
        abrirFormulario(LocalDate.now(), LocalTime.now());
    }
    
    private void abrirFormulario(LocalDate data, LocalTime hora) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/FormularioReserva.fxml"));
            Parent root = loader.load();
            FormularioReservaController controller = loader.getController();
            controller.preencherDados(data, hora);
            
            Stage stage = new Stage();
            stage.setTitle("Fazer Nova Reserva");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(gridCalendario.getScene().getWindow());
            stage.setScene(new Scene(root));
            stage.showAndWait();
            
            atualizarVisualizacaoCompleta();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void atualizarVisualizacaoCompleta(){
        atualizarVisualizacaoCalendario();
        carregarHistoricoGeral();
        if(moradorReservasComboBox.getValue() != null){
            carregarReservasDoMorador(moradorReservasComboBox.getValue());
        }
    }
    
    private void carregarComboBoxes() {
        filtroEspacoComboBox.getItems().setAll(espacoDAO.listarTodos());
    }

    private void carregarReservasDoMorador(Usuario morador) {
        if (morador != null) {
            System.out.println("[DEBUG carregarReservas] Carregando reservas para: " + morador.getNomeCompleto());
            tabelaReservas.getItems().clear();
            List<Reserva> minhasReservas = reservaDAO.listarPorUsuario(morador.getId());
            System.out.println("[DEBUG carregarReservas] DAO retornou " + minhasReservas.size());
            tabelaReservas.getItems().addAll(minhasReservas);
            System.out.println("[DEBUG carregarReservas] Tabela de reservas atualizada. ---");
        }
    }
    
    private void carregarHistoricoGeral() {
        if (tabelaHistorico != null) {
            tabelaHistorico.getItems().clear();
            tabelaHistorico.getItems().addAll(reservaDAO.listarTodas());
        }
    }

    

    private void handleAbrirFormularioReserva(LocalDate data, LocalTime hora) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/FormularioReserva.fxml"));
            Parent root = loader.load();
            FormularioReservaController controller = loader.getController();
            controller.preencherDados(data, hora);
            
            Stage stage = new Stage();
            stage.setTitle("Fazer Nova Reserva");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(gridCalendario.getScene().getWindow());
            stage.setScene(new Scene(root));
            stage.showAndWait();
            
            atualizarVisualizacaoCompleta();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleCancelarReserva() {
        Usuario moradorSelecionado = moradorReservasComboBox.getSelectionModel().getSelectedItem();
        Reserva reservaSelecionada = tabelaReservas.getSelectionModel().getSelectedItem();
        if (reservaSelecionada == null || moradorSelecionado == null) {
            Validador.mostrarAlerta("Seleção Necessária", "Selecione um morador e uma reserva para cancelar.", AlertType.WARNING);
            return;
        }
        Alert alertConf = new Alert(AlertType.CONFIRMATION);
        alertConf.setTitle("Confirmação de Cancelamento");
        alertConf.setHeaderText("Cancelar esta reserva?");
        alertConf.setContentText(reservaSelecionada.getNomeEspaco() + " em " + reservaSelecionada.getDataReserva());
        Optional<ButtonType> result = alertConf.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                reservaDAO.deletar(reservaSelecionada.getId());
                Validador.mostrarAlerta("Sucesso", "Reserva cancelada com sucesso!", AlertType.INFORMATION);
                atualizarVisualizacaoCompleta();
            } catch (SQLException e) {
                Validador.mostrarAlerta("Erro de Banco de Dados", "Ocorreu um erro ao cancelar.", AlertType.ERROR);
                e.printStackTrace();
            }
        }
    }
    
    @FXML
    private void handleReagendar() {
        Usuario moradorSelecionado = moradorReservasComboBox.getSelectionModel().getSelectedItem();
        Reserva reservaSelecionada = tabelaReservas.getSelectionModel().getSelectedItem();
        if (reservaSelecionada == null || moradorSelecionado == null) {
            Validador.mostrarAlerta("Seleção Necessária", "Por favor, selecione uma reserva para reagendar.", AlertType.WARNING);
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/Reagendar.fxml"));
            Parent root = loader.load();
            ReagendarController controller = loader.getController();
            controller.carregarDadosReserva(reservaSelecionada);
            Stage stage = new Stage();
            stage.setTitle("Reagendar Reserva");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(tabelaReservas.getScene().getWindow());
            stage.setScene(new Scene(root));
            stage.showAndWait();
            atualizarVisualizacaoCompleta();
        } catch (IOException e) {
            Validador.mostrarAlerta("Erro de Carregamento", "Ocorreu um erro ao abrir a tela de reagendamento.", AlertType.ERROR);
            e.printStackTrace();
        }
    }
    private void configurarBuscaComboBox() {
        StringConverter<Usuario> conversor = new StringConverter<>() {
            @Override
            public String toString(Usuario u) {
                return u == null ? null : u.getNomeCompleto();
            }

            @Override
            public Usuario fromString(String string) {
                if (string == null || string.isEmpty()) {
                    return null;
                }
                List<Usuario> resultados = usuarioDAO.buscarMoradoresPorNome(string);
                if (resultados.size() == 1) {
                    return resultados.get(0);
                }
                return null;
            }
        };
        
        moradorReservasComboBox.setConverter(conversor);
        moradorReservasComboBox.setEditable(true);
        
        AutoCompletionBinding<Usuario> binding = TextFields.bindAutoCompletion(
            moradorReservasComboBox.getEditor(),
            suggestionRequest -> usuarioDAO.buscarMoradoresPorNome(suggestionRequest.getUserText()),
            conversor
        );
        
        binding.setOnAutoCompleted(event -> {
            Usuario moradorSelecionado = event.getCompletion();
            moradorReservasComboBox.setValue(moradorSelecionado);
            if (moradorSelecionado != null) {
                carregarReservasDoMorador(moradorSelecionado);
            }
        });
    }
}