/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DAO;

import Model.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author João
 */
public class UsuarioDAO {
    public void inserirUsuario(Usuario usuario) {
        String sql = "INSERT INTO usuario (id_user, id_cond, nome, senha, cpf) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConexaoPostgreeSQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, usuario.getId());
            stmt.setInt(2, usuario.getId_condominio());
            stmt.setString(3, usuario.getNome());
            stmt.setString(4, usuario.getSenha());
            stmt.setString(5, usuario.getCPF());

            stmt.executeUpdate();
            System.out.println("Usuário inserido com sucesso!");

        } catch (SQLException e) {
            System.out.println("Erro ao inserir usuário: " + e.getMessage());
        }
    }
    
    public boolean selecByLogin(String cpf, String senha) throws SQLException {
        String sql = "SELECT * FROM usuario WHERE login = ? AND senha = ?";
        try (Connection conn = ConexaoPostgreeSQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, cpf);
            stmt.setString(2, senha);
            
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.out.println("Erro ao vereficar login: " + e.getMessage());
            return false;
        }
        
    }
}
