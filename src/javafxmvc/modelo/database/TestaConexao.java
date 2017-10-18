package javafxmvc.modelo.database;

import java.sql.Connection;
import java.sql.SQLException;

public class TestaConexao {
    public static void main(String[] args) throws SQLException {
        try (Connection connection = new DatabaseMySQL().conectar()){
            System.out.println("Conexão Aberta");
        }
    }
}