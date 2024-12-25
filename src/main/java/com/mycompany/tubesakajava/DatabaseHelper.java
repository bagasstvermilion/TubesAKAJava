package com.mycompany.tubesakajava;
<<<<<<< HEAD

=======
>>>>>>> 6dae274 (sql)
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
<<<<<<< HEAD
=======
import java.util.ArrayList;
import java.util.List;
>>>>>>> 6dae274 (sql)

public class DatabaseHelper {
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=GameSet;integratedSecurity=true;encrypt=false;trustServerCertificate=true";
    private static final String DRIVER_CLASS = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    
<<<<<<< HEAD
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
=======
    public static class GameData {
        public String name;
        public float rating;
        public String developer;
        public int year;
        
        public GameData(String name, float rating, String developer, int year) {
            this.name = name;
            this.rating = rating;
            this.developer = developer;
            this.year = year;
        }
    }
    
    public static List<GameData> getGameData() {
        List<GameData> games = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM Game")) {
            
            Class.forName(DRIVER_CLASS);
            
            while (resultSet.next()) {
                games.add(new GameData(
                    resultSet.getString("namaGame"),
                    resultSet.getFloat("ratingGame"),
                    resultSet.getString("developerGame"),
                    resultSet.getInt("tahunRilis")
                ));
            }
            
>>>>>>> 6dae274 (sql)
        } catch (Exception e) {
            System.out.println("Database Error: " + e.getMessage());
            e.printStackTrace();
        }
<<<<<<< HEAD
        return null;
    }
}
=======
        return games;
    }
}
>>>>>>> 6dae274 (sql)
