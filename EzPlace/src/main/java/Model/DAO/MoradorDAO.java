/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DAO;

import Model.Morador;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author João
 */
public class MoradorDAO {
    
    public void insertMorador(Morador morador){
        
        String sql = "INSERT INTO morador (nome, senha, cpf, email, telefone, dataN, id, id_cond) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = ConexaoPostgreeSQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, morador.getNome());
            stmt.setString(2, morador.getSenha());
            stmt.setString(3, morador.getCPF());
            stmt.setString(4, morador.getEmail());
            stmt.setString(5, morador.getTelefone());
            stmt.setDate(6, (Date) morador.getDataNascimento());
            stmt.setInt(7, morador.getId());
            stmt.setInt(8, morador.getId_condominio());
            
            stmt.executeUpdate();
            System.out.println("Morador inserido com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao inserir morador: " + e.getMessage());
        }
    }
}
