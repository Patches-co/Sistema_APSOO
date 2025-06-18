/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projeto.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import projeto.db.ConexaoDB;
import projeto.model.Reserva;
/**
 *
 * @author Jvvpa
 */
public class ReservaDAO {
    public boolean verificarDisponibilidade(int idEspaco, LocalDate data, LocalTime horarioInicio, LocalTime horarioFim) {
        String sql = "SELECT COUNT(*) FROM reservas WHERE id_espaco = ? AND data_reserva = ? AND horario_inicio < ? AND horario_fim > ?";
        
        try (Connection conn = ConexaoDB.getConexao();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idEspaco);
            pstmt.setDate(2, Date.valueOf(data));
            pstmt.setTime(3, Time.valueOf(horarioFim));
            pstmt.setTime(4, Time.valueOf(horarioInicio));
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) == 0;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Erro ao verificar disponibilidade: " + e.getMessage());
        }
        return false;
    }
    
    public void salvar(Reserva reserva) throws SQLException {
        String sql = "INSERT INTO reservas (data_reserva, horario_inicio, horario_fim, id_usuario, id_espaco) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = ConexaoDB.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Converte de java.time para java.sql
            pstmt.setDate(1, Date.valueOf(reserva.getDataReserva()));
            pstmt.setTime(2, Time.valueOf(reserva.getHorarioInicio()));
            pstmt.setTime(3, Time.valueOf(reserva.getHorarioFim()));
            pstmt.setInt(4, reserva.getIdUsuario());
            pstmt.setInt(5, reserva.getIdEspaco());

            pstmt.executeUpdate();
        }
    }
    
    public void deletar(int idReserva) throws SQLException {
        String sql = "DELETE FROM reservas WHERE id = ?";
        try (Connection conn = ConexaoDB.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idReserva);
            pstmt.executeUpdate();
        }
    }
    
    public void atualizar(Reserva reserva) throws SQLException {
        String sql = "UPDATE reservas SET data_reserva = ?, horario_inicio = ?, horario_fim = ? WHERE id = ?";
        
        try (Connection conn = ConexaoDB.getConexao();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDate(1, java.sql.Date.valueOf(reserva.getDataReserva()));
            pstmt.setTime(2, java.sql.Time.valueOf(reserva.getHorarioInicio()));
            pstmt.setTime(3, java.sql.Time.valueOf(reserva.getHorarioFim()));
            pstmt.setInt(4, reserva.getId());
            
            pstmt.executeUpdate();
        }
    }
    
    public List<Reserva> listarTodas() {
        String sql = "SELECT r.*, u.nome_completo as nome_usuario, e.nome as nome_espaco " +
                     "FROM reservas r " +
                     "JOIN usuarios u ON r.id_usuario = u.id " +
                     "JOIN espacos e ON r.id_espaco = e.id " +
                     "ORDER BY r.data_reserva DESC, r.horario_inicio DESC";
        
        List<Reserva> reservas = new ArrayList<>();
        try (Connection conn = ConexaoDB.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Reserva res = new Reserva();
                res.setId(rs.getInt("id"));
                res.setDataReserva(rs.getDate("data_reserva").toLocalDate());
                res.setHorarioInicio(rs.getTime("horario_inicio").toLocalTime());
                res.setHorarioFim(rs.getTime("horario_fim").toLocalTime());
                res.setIdUsuario(rs.getInt("id_usuario"));
                res.setIdEspaco(rs.getInt("id_espaco"));
                
                res.setNomeUsuario(rs.getString("nome_usuario"));
                res.setNomeEspaco(rs.getString("nome_espaco"));
                
                reservas.add(res);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservas;
    }
    
    public List<Reserva> listarPorPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        String sql = "SELECT r.*, u.nome_completo as nome_usuario, e.nome as nome_espaco " +
                     "FROM reservas r " +
                     "JOIN usuarios u ON r.id_usuario = u.id " +
                     "JOIN espacos e ON r.id_espaco = e.id " +
                     "WHERE r.data_reserva BETWEEN ? AND ? " +
                     "ORDER BY r.data_reserva, r.horario_inicio";
        
        List<Reserva> reservas = new ArrayList<>();
        try (Connection conn = ConexaoDB.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDate(1, Date.valueOf(dataInicio));
            pstmt.setDate(2, Date.valueOf(dataFim));
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Reserva res = new Reserva();
                res.setId(rs.getInt("id"));
                res.setDataReserva(rs.getDate("data_reserva").toLocalDate());
                res.setHorarioInicio(rs.getTime("horario_inicio").toLocalTime());
                res.setHorarioFim(rs.getTime("horario_fim").toLocalTime());
                res.setIdUsuario(rs.getInt("id_usuario"));
                res.setNomeUsuario(rs.getString("nome_usuario"));
                res.setNomeEspaco(rs.getString("nome_espaco"));
                
                res.setIdEspaco(rs.getInt("id_espaco"));
                
                reservas.add(res);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservas;
    }
    
    public List<Reserva> listarPorUsuario(int idUsuario) {
        String sql = "SELECT r.*, u.nome_completo as nome_usuario, e.nome as nome_espaco " +
                     "FROM reservas r " +
                     "JOIN usuarios u ON r.id_usuario = u.id " +
                     "JOIN espacos e ON r.id_espaco = e.id " +
                     "WHERE r.id_usuario = ? ORDER BY r.data_reserva DESC, r.horario_inicio";
        
        List<Reserva> reservas = new ArrayList<>();
        try (Connection conn = ConexaoDB.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idUsuario);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Reserva res = new Reserva();
                
                res.setId(rs.getInt("id"));
                res.setDataReserva(rs.getDate("data_reserva").toLocalDate());
                res.setHorarioInicio(rs.getTime("horario_inicio").toLocalTime());
                res.setHorarioFim(rs.getTime("horario_fim").toLocalTime());
                res.setIdUsuario(rs.getInt("id_usuario"));
                res.setIdEspaco(rs.getInt("id_espaco"));
                res.setNomeUsuario(rs.getString("nome_usuario"));
                res.setNomeEspaco(rs.getString("nome_espaco"));
                
                reservas.add(res);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservas;
    }
    
    public int contarReservasDoUsuarioNoPeriodo(int idUsuario, LocalDate inicioPeriodo, LocalDate fimPeriodo) {
        String sql = "SELECT COUNT(*) FROM reservas WHERE id_usuario = ? AND data_reserva BETWEEN ? AND ?";
        try (Connection conn = ConexaoDB.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idUsuario);
            pstmt.setDate(2, java.sql.Date.valueOf(inicioPeriodo));
            pstmt.setDate(3, java.sql.Date.valueOf(fimPeriodo));
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}

