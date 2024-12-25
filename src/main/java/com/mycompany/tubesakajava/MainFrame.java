<<<<<<< HEAD
package com.mycompany.tubesakajava;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MainFrame extends JFrame {
    private JTable table;
    private JLabel iterativeTimeLabel;
    private JLabel recursiveTimeLabel;

    public MainFrame() {
        setTitle("Data Game");
        setBounds(100, 100, 1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        getContentPane().setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new GridBagLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));  // Add padding
        GridBagConstraints gbc = new GridBagConstraints();

        JButton iterativeButton = createStyledButton("Cari (Iteratif)");
        JButton recursiveButton = createStyledButton("Cari (Rekursif)");
        JButton refreshButton = createStyledButton("Refresh Data");

        JLabel titleLabel = new JLabel("Analisis Performa Pencarian Game");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = new Insets(0, 0, 20, 0);
        topPanel.add(titleLabel, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.add(iterativeButton);
        buttonPanel.add(recursiveButton);
        buttonPanel.add(refreshButton);
        
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 20, 0);
        topPanel.add(buttonPanel, gbc);

        JPanel timePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 0));
        timePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        iterativeTimeLabel = createStyledTimeLabel("Waktu Iteratif: - ms");
        recursiveTimeLabel = createStyledTimeLabel("Waktu Rekursif: - ms");
        
        timePanel.add(iterativeTimeLabel);
        timePanel.add(recursiveTimeLabel);

        gbc.gridy = 2;
        topPanel.add(timePanel, gbc);

        getContentPane().add(topPanel, BorderLayout.NORTH);

        table = new JTable();
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(25);
        DefaultTableModel initialModel = new DefaultTableModel();
        initialModel.addColumn("Nama Game");
        initialModel.addColumn("Rating Game");
        initialModel.addColumn("Developer Game");
        initialModel.addColumn("Tahun Rilis");
        table.setModel(initialModel);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        iterativeButton.addActionListener(e -> {
            try {
                String input = JOptionPane.showInputDialog(this, 
                    "Masukkan minimum rating:",
                    "Input Rating",
                    JOptionPane.QUESTION_MESSAGE);
                
                if (input != null && !input.trim().isEmpty()) {
                    float minRating = Float.parseFloat(input);
                    DefaultTableModel model = IterativeGameSearch.searchGamesByRating(minRating);
                    
                    if (model != null && model.getRowCount() > 0) {
                        table.setModel(model);
                        iterativeTimeLabel.setText("Waktu Iteratif: " + IterativeGameSearch.getExecutionTime() + " ms");
                        System.out.println("Table updated with " + model.getRowCount() + " rows");
                    } else {
                        JOptionPane.showMessageDialog(this,
                            "Tidak ada data yang ditemukan untuk rating >= " + minRating,
                            "Tidak Ada Data",
                            JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                    "Masukkan rating yang valid (angka)",
                    "Error Input",
                    JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                    "Error: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        recursiveButton.addActionListener(e -> {
            try {
                String input = JOptionPane.showInputDialog(this,
                    "Masukkan minimum rating:",
                    "Input Rating",
                    JOptionPane.QUESTION_MESSAGE);
                
                if (input != null && !input.trim().isEmpty()) {
                    float minRating = Float.parseFloat(input);
                    DefaultTableModel model = RecursiveGameSearch.searchGamesByRating(minRating);
                    
                    if (model != null && model.getRowCount() > 0) {
                        table.setModel(model);
                        recursiveTimeLabel.setText("Waktu Rekursif: " + RecursiveGameSearch.getExecutionTime() + " ms");
                        System.out.println("Table updated with " + model.getRowCount() + " rows");
                    } else {
                        JOptionPane.showMessageDialog(this,
                            "Tidak ada data yang ditemukan untuk rating >= " + minRating,
                            "Tidak Ada Data",
                            JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                    "Masukkan rating yang valid (angka)",
                    "Error Input",
                    JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                    "Error: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        refreshButton.addActionListener(e -> {
            try {
                DefaultTableModel model = IterativeGameSearch.searchGamesByRating(0); // Get all games
                if (model != null) {
                    table.setModel(model);
                    System.out.println("Table refreshed with " + model.getRowCount() + " rows");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                    "Error refreshing data: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        try {
            DefaultTableModel model = IterativeGameSearch.searchGamesByRating(0);
            if (model != null) {
                table.setModel(model);
                System.out.println("Initial data loaded with " + model.getRowCount() + " rows");
            }
        } catch (Exception e) {
            System.out.println("Error loading initial data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(150, 40));
        return button;
    }

    private JLabel createStyledTimeLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        label.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY, 2),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        label.setOpaque(true);
        label.setBackground(Color.WHITE);
        return label;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            try {
                MainFrame frame = new MainFrame();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
=======
package com.mycompany.tubesakajava;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
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

        JLabel titleLabel = new JLabel("Analisis Performa Pencarian Game");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = new Insets(0, 0, 20, 0);
        topPanel.add(titleLabel, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        JButton iterativeButton = createStyledButton("Cari (Iteratif)");
        JButton recursiveButton = createStyledButton("Cari (Rekursif)");
        JButton refreshButton = createStyledButton("Refresh Data");
        JButton showChartButton = createStyledButton("Tampilkan Grafik");

        iterativeButton.addActionListener(e -> handleIterativeSearch());
        recursiveButton.addActionListener(e -> handleRecursiveSearch());
        refreshButton.addActionListener(e -> handleRefresh());
        showChartButton.addActionListener(e -> {
            updateDataset();
            showChartInNewWindow();
        });

        buttonPanel.add(iterativeButton);
        buttonPanel.add(recursiveButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(showChartButton);

        gbc.gridy = 1;
        topPanel.add(buttonPanel, gbc);

        JPanel timePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 0));
        timePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        iterativeTimeLabel = createStyledTimeLabel("Waktu Iteratif: - ms");
        recursiveTimeLabel = createStyledTimeLabel("Waktu Rekursif: - ms");

        timePanel.add(iterativeTimeLabel);
        timePanel.add(recursiveTimeLabel);

        gbc.gridy = 2;
        topPanel.add(timePanel, gbc);

        getContentPane().add(topPanel, BorderLayout.NORTH);
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
        String input = JOptionPane.showInputDialog(this, "Masukkan minimal rating (contoh: 4.0):", "Minimal Rating", JOptionPane.QUESTION_MESSAGE);
        if (input != null && !input.trim().isEmpty()) {
            try {
                float minRating = Float.parseFloat(input);
                long startTime = System.currentTimeMillis();
                DefaultTableModel model = IterativeGameSearch.searchGamesByRating(minRating);
                long endTime = System.currentTimeMillis();

                if (model != null) {
                    table.setModel(model);
                }

                iterativeHistory.add(endTime - startTime);
                iterativeTimeLabel.setText("Waktu Iteratif: " + (endTime - startTime) + " ms");

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Masukkan rating yang valid!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleRecursiveSearch() {
        String input = JOptionPane.showInputDialog(this, "Masukkan minimal rating (contoh: 4.0):", "Minimal Rating", JOptionPane.QUESTION_MESSAGE);
        if (input != null && !input.trim().isEmpty()) {
            try {
                float minRating = Float.parseFloat(input);
                long startTime = System.currentTimeMillis();
                DefaultTableModel model = RecursiveGameSearch.searchGamesByRating(minRating);
                long endTime = System.currentTimeMillis();

                if (model != null) {
                    table.setModel(model);
                }

                recursiveHistory.add(endTime - startTime);
                recursiveTimeLabel.setText("Waktu Rekursif: " + (endTime - startTime) + " ms");

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Masukkan rating yang valid!", "Error", JOptionPane.ERROR_MESSAGE);
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
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data awal: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showChartInNewWindow() {
        if (chartFrame == null || !chartFrame.isVisible()) {
            chartFrame = new JFrame("Grafik Perbandingan Waktu Eksekusi");
            chartFrame.setSize(800, 600);
            chartFrame.setLocationRelativeTo(this);

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
        for (int i = 0; i < Math.max(iterativeHistory.size(), recursiveHistory.size()); i++) {
            String category = "Pencarian " + (i + 1);
            if (i < iterativeHistory.size()) dataset.addValue(iterativeHistory.get(i), "Iteratif", category);
            if (i < recursiveHistory.size()) dataset.addValue(recursiveHistory.get(i), "Rekursif", category);
        }
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        return button;
    }

    private JLabel createStyledTimeLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        label.setOpaque(true);
        label.setBackground(Color.WHITE);
        return label;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
>>>>>>> 6dae274 (sql)
