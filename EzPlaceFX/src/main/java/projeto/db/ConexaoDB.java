package projeto.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoDB {

    private static final String URL = "jdbc:postgresql://localhost:5432/ezplace_db";
    private static final String USUARIO = "postgres";
    private static final String SENHA = "postgres";

    public static Connection getConexao() {
        try {
            Connection conn = DriverManager.getConnection(URL, USUARIO, SENHA);
            return conn;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}