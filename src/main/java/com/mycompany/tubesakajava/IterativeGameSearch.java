package com.mycompany.tubesakajava;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

public class IterativeGameSearch {
    private static long executionTime = 0;

    public static DefaultTableModel searchGamesByRating(float targetRating) {
    long startTime = System.nanoTime();
    List<DatabaseHelper.GameData> games = DatabaseHelper.getGameData();
    DefaultTableModel model = new DefaultTableModel();
    model.addColumn("Nama Game");
    model.addColumn("Rating Game");
    model.addColumn("Developer Game");
    model.addColumn("Tahun Rilis");
    
    if (games.isEmpty()) {
        return createEmptyModel("No games found");
    }
    
    if (targetRating <= 0) {
        for (DatabaseHelper.GameData game : games) {
            model.addRow(new Object[]{game.name, game.rating, game.developer, game.year});
        }
    } else {
        for (int i = 0; i < games.size(); i++) {
            DatabaseHelper.GameData game = games.get(i);
            if (game.rating >= targetRating) {
                model.addRow(new Object[]{game.name, game.rating, game.developer, game.year});
            }
        }
    }
    
    executionTime = (System.nanoTime() - startTime);
    System.out.println("Iterative execution time: " + executionTime + " ns");
    return model;
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