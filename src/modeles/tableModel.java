package modeles;
import utils.DBConnection;

import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class tableModel {
    private Connection connection;

    public tableModel() {
        connection = DBConnection.getConnection();
    }

    // fucntion pour récupérer les Rendvou
    public DefaultTableModel getTableData() {
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"Nom", "prenom", "specialite", "telephone",
                "Date", "heure", "Statut"});
        String query = "SELECT nom,prenom,specialite ," +
                "telephone,date_rdv,heure_rdv,statut " +
                "FROM patients JOIN rdv ON rdv.patient_id = patients.id GROUP BY specialite";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                model.addRow(new String[]{
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("specialite"),
                        rs.getString("telephone"),
                        rs.getString("date_rdv"),
                        rs.getString("heure_rdv"),
                        rs.getString("statut")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

        return model;
    }
}

