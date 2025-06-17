package projeto.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoDB {

    private static final String URL = "jdbc:postgresql://localhost:5432/ezplace_db";
    private static final String USUARIO = "postgres";
    private static final String SENHA = "postgres"; // USE SUA SENHA AQUI

    public static Connection getConexao() {
        // A LINHA MAIS IMPORTANTE DA NOSSA DEPURAÇÃO:
        System.out.println("--- [DEBUG] Tentando conectar na URL: " + URL + " ---");

        try {
            Connection conn = DriverManager.getConnection(URL, USUARIO, SENHA);
            System.out.println("--- [DEBUG] Conexão com o banco estabelecida com sucesso! ---");
            return conn;
        } catch (SQLException e) {
            System.err.println("!!! [DEBUG] FALHA NA CONEXÃO !!!");
            e.printStackTrace();
            return null;
        }
    }
}