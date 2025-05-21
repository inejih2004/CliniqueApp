package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import utils.DBConnection;

public class SpecialiteDAO {
    public List<String> getAllSpecialites() {
        List<String> specialites = new ArrayList<>();
        String query = "SELECT DISTINCT specialite FROM users WHERE role = 'medecin' AND specialite IS NOT NULL";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
             
            while (rs.next()) {
                specialites.add(rs.getString("specialite"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "\"⚠️ Erreur lors de lajoutation de la spesialite . Vérifiez que le spesialite est exsist.\"\r\n"
            		+ "", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
        return specialites;
    }
}

