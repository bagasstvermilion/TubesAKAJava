package com.mycompany.tubesakajava;
import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;

public class RecursiveGameSearch {
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
        try {
            ResultSet resultSet = DatabaseHelper.getGameData();
            if (resultSet == null) {
                return createEmptyModel("none");
            }
            
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Nama Game");
            model.addColumn("Rating Game");
            model.addColumn("Developer Game");
            model.addColumn("Tahun Rilis");

            List<GameData> allGames = new ArrayList<>();
            int index = 0;
            while (resultSet.next()) {
                allGames.add(new GameData(
                    resultSet.getString("namaGame"),
                    resultSet.getFloat("ratingGame"),
                    resultSet.getString("developerGame"),
                    resultSet.getInt("tahunRilis"),
                    index++
                ));
            }

            if (targetRating <= 0) {
                for (GameData game : allGames) {
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
            recursiveHelper(allGames, 0, targetRating, matchingGames);
            
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
            System.out.println("Recursive execution time: " + executionTime + " ms");
            
            return model;
            
        } catch (Exception e) {
            System.out.println("Error in recursive search: " + e.getMessage());
            e.printStackTrace();
            return createEmptyModel("Error: " + e.getMessage());
        }
    }
    
    private static void recursiveHelper(List<GameData> games, int index, float targetRating, 
        List<GameData> matchingGames) {
        if (index >= games.size()) {
            return;
        }

        GameData game = games.get(index);
        if (game.rating >= targetRating) {
            matchingGames.add(game);
        }
        
        recursiveHelper(games, index + 1, targetRating, matchingGames);
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