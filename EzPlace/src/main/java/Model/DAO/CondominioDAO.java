/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DAO;

import Model.Condominio;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author João
 */
public class CondominioDAO {
    public void inserirCondominio(Condominio cond) {
        String sql = "INSERT INTO condominio (id_cond, nome, cep, tipo) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexaoPostgreeSQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, cond.getId_condominio());
            stmt.setString(2, cond.getNomeCondominio());
            stmt.setString(3, cond.getCep());
            stmt.setString(4, cond.getTipo());

            stmt.executeUpdate();
            System.out.println("Condominio inserido com sucesso!");

        } catch (SQLException e) {
            System.out.println("Erro ao inserir condominio: " + e.getMessage());
        }
    }
    
    
}
