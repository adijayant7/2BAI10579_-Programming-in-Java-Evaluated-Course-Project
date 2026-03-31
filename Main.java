package startOfProject;

import banking.ui.MainFrame;
import banking.ui.Theme;

import javax.swing.*;

/**
 * ╔══════════════════════════════════════════════════════════╗
 * ║              BankingSystem – Entry Point                 ║
 * ║                                                          ║
 * ║  Run this class to start the application.                ║
 * ║  Make sure your MySQL server is running and the          ║
 * ║  database is set up (see database/banking_schema.sql).   ║
 * ║                                                          ║
 * ║  DB config: banking/db/DBConnection.java                 ║
 * ╚══════════════════════════════════════════════════════════╝
 */
public class Main {

    public static void main(String[] args) {
        // Apply system L&F before any Swing component is created
        Theme.applyLookAndFeel();

        // Always create Swing components on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                new MainFrame();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null,
                        "Failed to start BankingSystem:\n" + e.getMessage(),
                        "Startup Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
                System.exit(1);
            }
        });
    }
}
