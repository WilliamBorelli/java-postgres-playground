package com.example;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class AppBd {
    private static final String PASSWORD = "";
    private static final String USERNAME = "gitpod";
    private static final String JDBC_URL = "jdbc:postgresql://localhost/postgres";

    public static void main(String[] args) {
        new AppBd();
    }
 
    public AppBd(){
        try(var conn = getConnection()){  
            listarEstados();
            carregarDriverJDBC(conn);
            localizarEstado(conn, "SP");
            listarDadosTabela(conn,"cliente");
        }catch (SQLException e) {
            System.err.println("Não foi possível conectar ao Bando de Dados" + e.getMessage());
        }
    }

private void listarDadosTabela(Connection conn, String tabela) {
    var sql = "select * from" + tabela;
    System.out.println(sql);
    try {
        var statement = conn.createStatement();
        var result = statement.executeQuery(sql);
        while(result.next()){
            int cols = result.getMetaData().getColumnCount();
            for (int i = 1; i <= cols; i++) {
                System.out.printf("%s | ", result.getString(i));
            }
            System.out.println();
        }
    } catch (SQLException e) {
        System.out.println("Erro na execução da consulta");
    }
}

    private void localizarEstado(Connection conn, String uf) {
        try{
            var sql ="SELECT * FROM estado WHERE uf = ?";
            var statement = conn.prepareStatement(sql);
            //var sql ="SELECT * FROM estado WHERE uf = '"+ uf + "'"; //Sucetível a SQL Injection

            System.out.println(sql);
            statement.setString(1, uf);
           var result = statement.executeQuery();
           if(result.next()){
            System.out.printf("Id: %d Nome: %s UF: %s\n", result.getInt("id"), result.getString("nome"), result.getString("uf"));
           }
        }catch(SQLException e){
            System.err.println("Erro ao executar consulta SQL" + e.getMessage());
        }
        
    }

    private void listarEstados() {
        try(var conn = getConnection()){ 
        System.out.println("Conexão realizada com sucesso");
            var statement = conn.createStatement();
            var result = statement.executeQuery("SELECT * FROM estado");
            while(result.next()){
                System.out.printf("Id: %d Nome %s UF: %s\n", result.getInt("id"), result.getString("nome"), result.getString("uf"));
            }
        }  catch (SQLException e) {
             System.err.println("Não foi possível executar a consulta ao banco"+ e.getMessage());
        }
    }

    private Connection getConnection() throws SQLException{
        return DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
    }

    private void carregarDriverJDBC(Connection conn) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch(ClassNotFoundException e) {
            System.err.println("Não foi possível carregar a biblioteca para acesso ao banco de dados"+ e.getMessage());
        }
    }
}
