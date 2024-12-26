package com.mycompany.tubesakajava;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=GameSet;integratedSecurity=true;encrypt=false;trustServerCertificate=true";
    private static final String DRIVER_CLASS = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static int dataLimit = -1;

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

    public static void setDataLimit(int limit) {
        dataLimit = limit;
    }

    public static int getDataLimit() {
        return dataLimit;
    }

    public static List<GameData> getGameData() {
        List<GameData> games = new ArrayList<>();
        try {
            Class.forName(DRIVER_CLASS);
            String query = (dataLimit > 0) 
                ? "SELECT TOP " + dataLimit + " * FROM Game" 
                : "SELECT * FROM Game";

            try (Connection connection = DriverManager.getConnection(URL);
                 Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {
                
                while (resultSet.next()) {
                    games.add(new GameData(
                        resultSet.getString("namaGame"),
                        resultSet.getFloat("ratingGame"),
                        resultSet.getString("developerGame"),
                        resultSet.getInt("tahunRilis")
                    ));
                }
            }
        } catch (Exception e) {
            System.out.println("Database Error: " + e.getMessage());
            e.printStackTrace();
        }
        return games;
    }
}