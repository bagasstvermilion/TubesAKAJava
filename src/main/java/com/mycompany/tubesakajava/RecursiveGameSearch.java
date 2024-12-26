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
    recursiveHelper(allGames, 0, targetRating, matchingGames);
    
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

    private static void recursiveHelper(List<DatabaseHelper.GameData> games, int index, float targetRating, List<DatabaseHelper.GameData> matchingGames) {
        if (index >= games.size()) {
            return;
        }

        DatabaseHelper.GameData game = games.get(index);
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