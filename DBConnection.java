package banking.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Manages a single shared JDBC connection to MySQL.
 * Call DBConnection.getConnection() anywhere you need DB access.
 */
public class DBConnection {

    // ── Change these three values to match your MySQL setup ──────────────
    private static final String URL      = "jdbc:mysql://localhost:3306/banking_system"
                                         + "?useSSL=false&serverTimezone=UTC";
    private static final String USER     = "root";
    private static final String PASSWORD = "root";          // ← your MySQL password
    // ─────────────────────────────────────────────────────────────────────

    private static Connection connection = null;

    private DBConnection() { /* utility class – no instances */ }

    /**
     * Returns the shared connection, creating it on first call.
     */
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("[DB] Connection established successfully.");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("[DB] MySQL Driver not found. Add mysql-connector-j to your classpath.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("[DB] Failed to connect to the database.");
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * Closes the shared connection (call on application exit).
     */
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("[DB] Connection closed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
