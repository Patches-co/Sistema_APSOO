/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author João
 */
public class ConexaoPostgreeSQL {
    private static final String URL = "jdbc:postgresql://localhost:5432/ezplace";
    private static final String USUARIO = "postgres";
    private static final String SENHA = "Jv0802203vP@";
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }
    
    /*public static void main(String[] args) {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DriverManager.getConnection(URL, USUARIO, SENHA);
            System.out.println("Conexão realizada com sucesso!");
            
            String sql = "INSERT INTO morador (nome, email) VALUES (?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, "João Pacheco");
            stmt.setString(2, "jvvpacheco@gmail.com");
            
            int linhasAfetadas = stmt.executeUpdate();
            System.out.println("Inserção realizada! Linhas afetadas: " + linhasAfetadas);
        } catch (SQLException e) {
            System.out.println("Erro na conexão: " + e.getMessage());
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.out.println("Erro ao fechar: " + e.getMessage());
            }
        }
    }*/
}
