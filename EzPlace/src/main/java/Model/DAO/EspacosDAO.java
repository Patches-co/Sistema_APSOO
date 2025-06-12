/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DAO;

import Model.Espacos;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author João
 */
public class EspacosDAO {
    public void inserirEspaco(Espacos espaco) {
        String sql = "INSERT INTO espacos (id_espaco, id_cond, disponibilidade, data_ini, data_fim, time_ini, time_fim) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexaoPostgreeSQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, espaco.getId_espaco());
            stmt.setInt(2, espaco.getId_condominio());
            stmt.setBoolean(3, espaco.isDisponivel());
            stmt.setDate(4, (Date) espaco.getData_ini());
            stmt.setDate(5, (Date) espaco.getData_fim());
            stmt.setTime(6, espaco.getTime_ini());
            stmt.setTime(7, espaco.getTime_fim());

            stmt.executeUpdate();
            System.out.println("Espaço inserido com sucesso!");

        } catch (SQLException e) {
            System.out.println("Erro ao inserir espaço: " + e.getMessage());
        }
    }
}
