/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.mycompany.tubesakajava;

import javax.swing.*;

/**
 *
 * @author fikri
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Error setting Look and Feel: " + e.getMessage());
        }

        // Launch the application in the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            try {
                MainFrame frame = new MainFrame(); // Create an instance of the main frame
                frame.setVisible(true);           // Make the frame visible
            } catch (Exception e) {
                System.err.println("Error initializing application: " + e.getMessage());
                e.printStackTrace();
            }
        });
        // TODO code application logic here
    }

}
