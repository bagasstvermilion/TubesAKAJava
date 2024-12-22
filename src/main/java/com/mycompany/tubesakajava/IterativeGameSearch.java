package com.mycompany.tubesakajava;
import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;

public class IterativeGameSearch {
    private static long executionTime = 0;
    private static class GameData {
        String name;
        float rating;
        String developer;
        int year;
        int originalIndex;
        
        GameData(String name, float rating, String developer, int year, int index) {
            this.name = name;
            this.rating = rating;
            this.developer = developer;
            this.year = year;
            this.originalIndex = index;
        }
    }
    
    public static DefaultTableModel searchGamesByRating(float targetRating) {
        long startTime = System.nanoTime();
        ResultSet resultSet = DatabaseHelper.getGameData();
        List<GameData> games = new ArrayList<>();
        DefaultTableModel model = new DefaultTableModel();
        
        try {
            if (resultSet == null) {
                System.out.println("No ResultSet returned from database");
                return createEmptyModel("none");
            }
            
            model.addColumn("Nama Game");
            model.addColumn("Rating Game");
            model.addColumn("Developer Game");
            model.addColumn("Tahun Rilis");
            
            int index = 0;
            while (resultSet.next()) {
                games.add(new GameData(
                    resultSet.getString("namaGame"),
                    resultSet.getFloat("ratingGame"),
                    resultSet.getString("developerGame"),
                    resultSet.getInt("tahunRilis"),
                    index++
                ));
            }

            if (targetRating <= 0) {
                for (GameData game : games) {
                    model.addRow(new Object[]{
                        game.name,
                        game.rating,
                        game.developer,
                        game.year
                    });
                }
                return model;
            }
            
            List<GameData> matchingGames = new ArrayList<>();
            for (GameData game : games) {
                if (game.rating >= targetRating) {
                    matchingGames.add(game);
                }
            }
            
            if (matchingGames.isEmpty()) {
                return createEmptyModel("none");
            }

            Collections.sort(matchingGames, new Comparator<GameData>() {
                @Override
                public int compare(GameData g1, GameData g2) {
                    if (g1.rating == g2.rating) {
                        return Integer.compare(g1.originalIndex, g2.originalIndex);
                    }
                    return Float.compare(g2.rating, g1.rating);
                }
            });
            
            for (GameData game : matchingGames) {
                model.addRow(new Object[]{
                    game.name,
                    game.rating,
                    game.developer,
                    game.year
                });
            }
            
            executionTime = (System.nanoTime() - startTime) / 1_000_000;
            System.out.println("Iterative execution time: " + executionTime + " ms");
            
            return model;
            
        } catch (Exception e) {
            System.out.println("Error in iterative search: " + e.getMessage());
            e.printStackTrace();
            return createEmptyModel("Error: " + e.getMessage());
        }
    }
    
    private static DefaultTableModel createEmptyModel(String message) {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Result");
        model.addRow(new Object[]{message});
        return model;
    }
    
    public static long getExecutionTime() {
        return executionTime;
    }
}