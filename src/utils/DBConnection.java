package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@SuppressWarnings("unused")
public class DBConnection {
    private static Connection con;

    public static Connection getConnection() {
        if (con == null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                con = DriverManager.getConnection("jdbc:mysql://localhost:3306/clinique", "root", "");
                System.out.println("✅ Connexion réussie à la base de données.");
            } catch (Exception e) {
                System.out.println("❌ Erreur de connexion : " + e.getMessage());
            }
        }
        return con;
    }
}
