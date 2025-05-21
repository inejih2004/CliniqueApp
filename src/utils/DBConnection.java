package utils;

import java.sql.*;

public class DBConnection {
    private static Connection connection = null;
    private static final String URL = "jdbc:mysql://localhost:3306/clinique?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private static boolean loggedSuccess = false;

    public static Connection getConnection() {
        try {
            // Check if connection is valid
            if (connection != null && !connection.isClosed() && connection.isValid(1)) {
                return connection;
            }

            // Close stale connection if exists
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.err.println("Warning: Error closing stale connection: " + e.getMessage());
                }
            }

            // Create new connection
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            connection.setAutoCommit(true);
            if (!loggedSuccess) {
                System.out.println("✅ Connexion réussie à la base de données.");
                loggedSuccess = true;
            }
            return connection;
        } catch (SQLException e) {
            System.err.println("Erreur de connexion: " + e.getMessage());
            throw new RuntimeException("Failed to connect to database: " + e.getMessage(), e);
        }
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        } finally {
            connection = null;
            loggedSuccess = false;
        }
    }
}