package com.mycompany.tubesakajava;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

public class RecursiveGameSearch {
    private static long executionTime = 0;

    public static DefaultTableModel searchGamesByRating(float targetRating) {
        long startTime = System.nanoTime();
        List<DatabaseHelper.GameData> allGames = DatabaseHelper.getGameData();
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Nama Game");
        model.addColumn("Rating Game");
        model.addColumn("Developer Game");
        model.addColumn("Tahun Rilis");
        
        if (allGames.isEmpty()) {
            return createEmptyModel("No games found");
        }
        
        if (targetRating <= 0) {
            for (DatabaseHelper.GameData game : allGames) {
                model.addRow(new Object[]{game.name, game.rating, game.developer, game.year});
            }
            return model;
        }
        
        List<DatabaseHelper.GameData> matchingGames = new ArrayList<>();
        recursiveHelper(allGames, targetRating, allGames.size(), matchingGames);
        
        if (matchingGames.isEmpty()) {
            return createEmptyModel("No matching games found");
        }
        
        for (DatabaseHelper.GameData game : matchingGames) {
            model.addRow(new Object[]{game.name, game.rating, game.developer, game.year});
        }
        
        executionTime = (System.nanoTime() - startTime); // Remove division by 1_000_000
        System.out.println("Recursive execution time: " + executionTime + " ns");
        return model;
    }

    private static List<DatabaseHelper.GameData> recursiveHelper(List<DatabaseHelper.GameData> games, float targetRating, int n, List<DatabaseHelper.GameData> results) {
        if (n <= 0) {
            return results;
        } else {
            DatabaseHelper.GameData currentGame = games.get(n - 1);
            if (currentGame.rating >= targetRating) {
                results.add(0, currentGame);
            }
            return recursiveHelper(games, targetRating, n - 1, results);
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
