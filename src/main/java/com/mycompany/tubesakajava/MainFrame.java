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