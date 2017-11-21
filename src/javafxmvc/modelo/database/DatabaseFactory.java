package javafxmvc.modelo.database;

import java.sql.Connection;

public class DatabaseFactory {
    public static Database getDatabase(String nome){
        if(nome.equals("postgresql")){
            return new DatabasePostgreSQL();
        } else if(nome.equals("mysql")){
            return new DatabaseMySQL();
        }
        return null;
    }

    public static class getDatabase implements Database {

        public getDatabase(String mysql) {
        }

        @Override
        public Connection conectar() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void desconectar(Connection conn) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }
}