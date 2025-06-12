/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DAO;

import Model.Funcionario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author João
 */
public class FuncionarioDAO {
    public void inserirFuncionario(Funcionario funcionario) {
        String sql = "INSERT INTO funcionario (id_user, id_cond, nome, senha, cpf, nivelAcesso) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexaoPostgreeSQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, funcionario.getId());
            stmt.setInt(2, funcionario.getId());
            stmt.setString(3, funcionario.getNome());
            stmt.setString(4, funcionario.getSenha());
            stmt.setString(5, funcionario.getCPF());
            stmt.setInt(6, funcionario.getNivelAcesso());

            stmt.executeUpdate();
            System.out.println("Funcionario inserido com sucesso!");

        } catch (SQLException e) {
            System.out.println("Erro ao inserir funcionario: " + e.getMessage());
        }
    }
    
    public Funcionario selecByLogin(Funcionario funcionario) throws SQLException {
        String sql = "SELECT * FROM funcionario WHERE cpf = ? AND senha = ?";
        try (Connection conn = ConexaoPostgreeSQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, funcionario.getCPF());
            stmt.setString(2, funcionario.getSenha());
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return funcionario;
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
