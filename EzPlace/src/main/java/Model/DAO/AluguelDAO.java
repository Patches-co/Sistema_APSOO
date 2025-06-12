/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DAO;

import Model.Aluguel;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author João
 */
public class AluguelDAO {
    public void inserirAluguel(Aluguel aluguel) {
        String sql = "INSERT INTO aluguel (id_reserva, id_espaco, id_cond, id_morador, disponibilidade, data, time_ini, time_fim, valor) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexaoPostgreeSQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, aluguel.getId_reserva());
            stmt.setInt(2, aluguel.getId_espaco());
            stmt.setInt(3, aluguel.getId_condominio());
            stmt.setInt(4, aluguel.getMorador().getId());
            stmt.setBoolean(5, aluguel.isDisponivel());
            stmt.setDate(6, (Date) aluguel.getData());
            stmt.setTime(7, aluguel.getTime_ini());
            stmt.setTime(8, aluguel.getTime_fim());
            stmt.setDouble(9, aluguel.getValor());

            stmt.executeUpdate();
            System.out.println("Aluguel inserido com sucesso!");

        } catch (SQLException e) {
            System.out.println("Erro ao inserir aluguel: " + e.getMessage());
        }
    }
}
