/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projeto.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import projeto.db.ConexaoDB;
import projeto.model.Espaco;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jvvpa
 */
public class EspacoDAO {
    
    public void salvar(Espaco espaco) {
        String sql = "INSERT INTO espacos (nome, horarios_disponiveis, regras_uso, duracao_reserva_min) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexaoDB.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, espaco.getNome());
            pstmt.setString(2, espaco.getHorariosDisponiveis());
            pstmt.setString(3, espaco.getRegrasUso());
            pstmt.setInt(4, espaco.getTempoMaximoReservaMin());

            pstmt.executeUpdate();
            System.out.println("Espaço '" + espaco.getNome() + "' salvo com sucesso!");

        } catch (SQLException e) {
            System.err.println("Erro ao salvar o espaço: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // public List<Espaco> listarTodos()
    public List<Espaco> listarTodos() {
        String sql = "SELECT * FROM espacos ORDER BY nome";
        List<Espaco> espacos = new ArrayList<>();

        try (Connection conn = ConexaoDB.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Espaco espaco = new Espaco();
                espaco.setId(rs.getInt("id"));
                espaco.setNome(rs.getString("nome"));
                espaco.setHorariosDisponiveis(rs.getString("horarios_disponiveis"));
                espaco.setRegrasUso(rs.getString("regras_uso"));
                espaco.setTempoMaximoReservaMin(rs.getInt("duracao_reserva_min"));
                espaco.setLimiteMensalPessoa(rs.getInt("limite_mensal_pessoa"));
                
                espacos.add(espaco);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar todos os espaços: " + e.getMessage());
            e.printStackTrace();
        }
        return espacos;
    }
    // public Espaco buscarPorId(int id)
    public Espaco buscarPorId(int id) {
        String sql = "SELECT * FROM espacos WHERE id = ?";
        Espaco espaco = null;
        try (Connection conn = ConexaoDB.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                espaco = new Espaco();
                espaco.setId(rs.getInt("id"));
                espaco.setNome(rs.getString("nome"));
                espaco.setHorariosDisponiveis(rs.getString("horarios_disponiveis"));
                espaco.setRegrasUso(rs.getString("regras_uso"));
                espaco.setTempoMaximoReservaMin(rs.getInt("duracao_reserva_min"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return espaco;
    }
    // public void atualizar(Espaco espaco)
    public void atualizar(Espaco espaco) throws SQLException {
        String sql = "UPDATE espacos SET nome = ?, horarios_disponiveis = ?, regras_uso = ?, duracao_reserva_min = ? WHERE id = ?";
        
        try (Connection conn = ConexaoDB.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, espaco.getNome());
            pstmt.setString(2, espaco.getHorariosDisponiveis());
            pstmt.setString(3, espaco.getRegrasUso());
            pstmt.setInt(4, espaco.getTempoMaximoReservaMin());
            pstmt.setInt(5, espaco.getId());
            
            pstmt.executeUpdate();
        }
    }
    
    // public void deletar(int id)
    public void deletar(int idEspaco) throws SQLException {
        String sql = "DELETE FROM espacos WHERE id = ?";
        
        try (Connection conn = ConexaoDB.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idEspaco);
            pstmt.executeUpdate();
        }
    }
}
