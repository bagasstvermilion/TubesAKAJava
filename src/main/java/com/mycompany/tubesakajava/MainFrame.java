package com.mycompany.tubesakajava;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.category.CategoryDataset;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MainFrame extends JFrame {
    private JTable table;
    private JLabel iterativeTimeLabel;
    private JLabel recursiveTimeLabel;
    private JFrame chartFrame;
    private DefaultCategoryDataset dataset;
    private List<Long> iterativeHistory;
    private List<Long> recursiveHistory;
    private JLabel dataCountLabel;

    public MainFrame() {
        setTitle("Data Game");
        setBounds(100, 100, 800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());

        // Inisialisasi dataset dan history
        dataset = new DefaultCategoryDataset();
        iterativeHistory = new ArrayList<>();
        recursiveHistory = new ArrayList<>();

        // Komponen GUI utama
        setupTopPanel();
        setupTable();

        // Load data awal
        loadInitialData();
    }

    private void setupTopPanel() {
    JPanel topPanel = new JPanel(new GridBagLayout());
    topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    GridBagConstraints gbc = new GridBagConstraints();

    // Title
    JLabel titleLabel = new JLabel("Analisis Performa Pencarian Game");
    titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    gbc.insets = new Insets(0, 0, 20, 0);
    topPanel.add(titleLabel, gbc);

    // Button Panel
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
    JButton iterativeButton = createStyledButton("Cari (Iteratif)");
    JButton recursiveButton = createStyledButton("Cari (Rekursif)");
    JButton refreshButton = createStyledButton("Refresh Data");
    JButton showChartButton = createStyledButton("Tampilkan Grafik");

    iterativeButton.addActionListener(e -> handleIterativeSearch());
    recursiveButton.addActionListener(e -> handleRecursiveSearch());
    refreshButton.addActionListener(e -> handleRefresh());
    showChartButton.addActionListener(e -> showChartInNewWindow());

    buttonPanel.add(iterativeButton);
    buttonPanel.add(recursiveButton);
    buttonPanel.add(refreshButton);
    buttonPanel.add(showChartButton);

    // Add data limit controls
    setupDataLimitControls(buttonPanel);

    gbc.gridy = 1;
    gbc.insets = new Insets(0, 0, 10, 0);
    topPanel.add(buttonPanel, gbc);

    // Time Panel with better spacing
    JPanel timePanel = new JPanel(new GridBagLayout());
    GridBagConstraints timeGbc = new GridBagConstraints();
    timeGbc.insets = new Insets(5, 20, 5, 20);
    
    // Iterative Time Label
    iterativeTimeLabel = createStyledTimeLabel("Waktu Iteratif (rata-rata): - ns");
    timeGbc.gridx = 0;
    timeGbc.gridy = 0;
    timeGbc.anchor = GridBagConstraints.WEST;
    timePanel.add(iterativeTimeLabel, timeGbc);
    
    // Recursive Time Label
    recursiveTimeLabel = createStyledTimeLabel("Waktu Rekursif (rata-rata): - ns");
    timeGbc.gridx = 1;
    timePanel.add(recursiveTimeLabel, timeGbc);
    
    // Data Count Label
    dataCountLabel = createStyledTimeLabel("Jumlah Data: 0");
    timeGbc.gridx = 2;
    timePanel.add(dataCountLabel, timeGbc);

    gbc.gridy = 2;
    gbc.insets = new Insets(10, 0, 10, 0);
    topPanel.add(timePanel, gbc);

    getContentPane().add(topPanel, BorderLayout.NORTH);
}


    private void setupDataLimitControls(JPanel buttonPanel) {
        String[] limitOptions = {"All Data", "10 Records", "100 Records", "1000 Records", "5000 Records"};
        JComboBox<String> dataLimitCombo = new JComboBox<>(limitOptions);
        dataLimitCombo.setFont(new Font("Arial", Font.BOLD, 14));
        
        dataLimitCombo.addActionListener(e -> {
            String selected = (String) dataLimitCombo.getSelectedItem();
            int limit;
            switch (selected) {
                case "10 Records":
                    limit = 10;
                    break;
                case "100 Records":
                    limit = 100;
                    break;
                case "1000 Records":
                    limit = 1000;
                    break;
                case "5000 Records":
                    limit = 5000;
                    break;
                default:
                    limit = -1; // No limit
                    break;
            }
            DatabaseHelper.setDataLimit(limit);
            handleRefresh();
        });
        
        buttonPanel.add(dataLimitCombo);
    }

    private void setupTable() {
        table = new JTable();
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));

        JLabel tableTitle = new JLabel("Daftar Game");
        tableTitle.setFont(new Font("Arial", Font.BOLD, 18));
        tableTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        tablePanel.add(tableTitle, BorderLayout.NORTH);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        getContentPane().add(tablePanel, BorderLayout.CENTER);
    }

    private void handleIterativeSearch() {
    String input = JOptionPane.showInputDialog(this, "Masukkan minimum rating:", "Input Rating", JOptionPane.QUESTION_MESSAGE);
    if (input != null && !input.trim().isEmpty()) {
        try {
            float minRating = Float.parseFloat(input);

            iterativeHistory.clear();
            int repeatCount = 10;

            for (int i = 0; i < repeatCount; i++) {
                long startTime = System.nanoTime();
                DefaultTableModel model = IterativeGameSearch.searchGamesByRating(minRating);
                long endTime = System.nanoTime();

                long executionTime = (endTime - startTime); // Remove division by 1_000_000
                iterativeHistory.add(executionTime);

                if (i == repeatCount - 1 && model != null) {
                    table.setModel(model);
                    updateDataCount();
                }
            }

            long averageTime = iterativeHistory.stream().mapToLong(Long::longValue).sum() / repeatCount;
            iterativeTimeLabel.setText("Waktu Iteratif (rata-rata): " + averageTime + " ns");

            updateDataset();

            JOptionPane.showMessageDialog(this, "Pencarian Iteratif selesai sebanyak " + repeatCount + " kali!", "Informasi", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Masukkan rating valid!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

private void handleRecursiveSearch() {
    String input = JOptionPane.showInputDialog(this, "Masukkan minimum rating:", "Input Rating", JOptionPane.QUESTION_MESSAGE);
    if (input != null && !input.trim().isEmpty()) {
        try {
            float minRating = Float.parseFloat(input);

            recursiveHistory.clear();
            int repeatCount = 10;

            for (int i = 0; i < repeatCount; i++) {
                long startTime = System.nanoTime();
                DefaultTableModel model = RecursiveGameSearch.searchGamesByRating(minRating);
                long endTime = System.nanoTime();

                long executionTime = (endTime - startTime); // Remove division by 1_000_000
                recursiveHistory.add(executionTime);

                if (i == repeatCount - 1 && model != null) {
                    table.setModel(model);
                    updateDataCount();
                }
            }

            long averageTime = recursiveHistory.stream().mapToLong(Long::longValue).sum() / repeatCount;
            recursiveTimeLabel.setText("Waktu Rekursif (rata-rata): " + averageTime + " ns");

            updateDataset();

            JOptionPane.showMessageDialog(this, "Pencarian Rekursif selesai sebanyak " + repeatCount + " kali!", "Informasi", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Masukkan rating valid!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

    private void handleRefresh() {
        loadInitialData();
    }

    private void loadInitialData() {
        try {
            DefaultTableModel model = IterativeGameSearch.searchGamesByRating(0);
            if (model != null) {
                table.setModel(model);
                updateDataCount();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data awal: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateDataCount() {
    // Ubah dari menggunakan table model ke menggunakan DatabaseHelper langsung
    int rowCount = DatabaseHelper.getGameData().size();
    dataCountLabel.setText("Jumlah Data: " + rowCount);
}

    private void showChartInNewWindow() {
        if (chartFrame == null || !chartFrame.isVisible()) {
            chartFrame = new JFrame("Grafik Perbandingan Waktu Eksekusi");
            chartFrame.setSize(800, 600);
            chartFrame.setLocationRelativeTo(this);

            updateDataset();

            JFreeChart chart = ChartFactory.createLineChart(
                "Perbandingan Waktu Eksekusi",
                "Pencarian ke-",
                "Waktu (ms)",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
            );

            CategoryPlot plot = chart.getCategoryPlot();
            LineAndShapeRenderer renderer = new LineAndShapeRenderer(true, true);
            renderer.setSeriesPaint(0, Color.BLUE);
            renderer.setSeriesPaint(1, Color.RED);
            plot.setRenderer(renderer);

            ChartPanel chartPanel = new ChartPanel(chart);
            chartFrame.add(chartPanel);

            chartFrame.setVisible(true);
        }
    }

    private void updateDataset() {
        dataset.clear();

        int dataPoints = Math.max(iterativeHistory.size(), recursiveHistory.size());
        List<Long> smoothedIterative = smoothData(iterativeHistory);
        List<Long> smoothedRecursive = smoothData(recursiveHistory);

        for (int i = 0; i < dataPoints; i++) {
            String category = "Pencarian " + (i + 1);
            if (i < smoothedIterative.size()) {
                dataset.addValue(smoothedIterative.get(i), "Iteratif", category);
            }
            if (i < smoothedRecursive.size()) {
                dataset.addValue(smoothedRecursive.get(i), "Rekursif", category);
            }
        }
    }

    private List<Long> smoothData(List<Long> data) {
        if (data.isEmpty()) return data;

        List<Long> smoothed = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            long sum = 0;
            int count = 0;

            for (int j = Math.max(0, i - 2); j <= Math.min(data.size() - 1, i + 2); j++) {
                sum += data.get(j);
                count++;
            }

            smoothed.add(sum / count);
        }
        return smoothed;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        return button;
    }

    private JLabel createStyledTimeLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 16)); // Slightly reduced font size
        label.setOpaque(true);
        label.setBackground(Color.WHITE);
        label.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(Color.LIGHT_GRAY),
        BorderFactory.createEmptyBorder(5, 10, 5, 10)
    ));
    return label;
}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}