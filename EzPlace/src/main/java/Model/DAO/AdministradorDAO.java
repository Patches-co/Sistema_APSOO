/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DAO;

import Model.Administrador;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author João
 */
public class AdministradorDAO {
    public void inserirAdministrador(Administrador admin) {
        String sql = "INSERT INTO administrador (id_user, id_cond, nome, senha, cpf, nivelAcesso) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexaoPostgreeSQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, admin.getId());
            stmt.setInt(2, admin.getId());
            stmt.setString(3, admin.getNome());
            stmt.setString(4, admin.getSenha());
            stmt.setString(5, admin.getCPF());
            stmt.setInt(6, admin.getNivelAcesso());

            stmt.executeUpdate();
            System.out.println("Administrador inserido com sucesso!");

        } catch (SQLException e) {
            System.out.println("Erro ao inserir administrador: " + e.getMessage());
        }
    }
    
    public Administrador selecByLogin(Administrador adm) throws SQLException {
        String sql = "SELECT * FROM administrador WHERE cpf = ? AND senha = ?";
        try (Connection conn = ConexaoPostgreeSQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, adm.getCPF());
            stmt.setString(2, adm.getSenha());
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return adm;
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao vereficar login: " + e.getMessage());
            return null;
        }
        
    }
}
