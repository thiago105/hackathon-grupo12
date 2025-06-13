package grupo12.dao;

import java.sql.Connection;
import java.sql.DriverManager;

public class Dao {

    private static final String URL = "jdbc:mysql://localhost:3306/hackata?useTimezone=true&serverTimezone=UTC";
    private static final String USER = "root";
    // ⚠️ IMPORTANTE: Coloque sua senha correta aqui!
    private static final String PASSWORD = "";

    private Connection connection;

    public Dao() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            System.err.println("FALHA AO CONECTAR AO BANCO: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return this.connection;
    }
}