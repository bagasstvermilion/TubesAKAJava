package com.mycompany.tubesakajava;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseHelper {
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=GameSet;integratedSecurity=true;encrypt=false;trustServerCertificate=true";
    private static final String DRIVER_CLASS = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    
    public static ResultSet getGameData() {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        
        try {
            Class.forName(DRIVER_CLASS);
            System.out.println("Driver loaded successfully");
            
            connection = DriverManager.getConnection(URL);
            System.out.println("Connection status: " + (connection != null ? "Connected" : "Failed to connect"));
            
            if (connection != null) {
                String query = "SELECT * FROM Game";
                statement = connection.createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY
                );
                resultSet = statement.executeQuery(query);
                System.out.println("Query executed successfully");
                return resultSet;
            }
        } catch (Exception e) {
            System.out.println("Database Error: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
