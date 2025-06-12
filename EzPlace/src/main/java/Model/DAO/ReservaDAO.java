/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DAO;

import Model.Reserva;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author João
 */
public class ReservaDAO {
    public void inserirReserva(Reserva reserva) {
        String sql = "INSERT INTO reserva (id_reserva, id_espaco, id_cond, id_morador, disponibilidade, data, time_ini, time_fim) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexaoPostgreeSQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, reserva.getId_reserva());
            stmt.setInt(2, reserva.getId_espaco());
            stmt.setInt(3, reserva.getId_condominio());
            stmt.setInt(4, reserva.getMorador().getId());
            stmt.setBoolean(5, reserva.isDisponivel());
            stmt.setDate(6, (Date) reserva.getData());
            stmt.setTime(7, reserva.getTime_ini());
            stmt.setTime(8, reserva.getTime_fim());

            stmt.executeUpdate();
            System.out.println("Reserva inserida com sucesso!");

        } catch (SQLException e) {
            System.out.println("Erro ao inserir reserva: " + e.getMessage());
        }
    }
}
