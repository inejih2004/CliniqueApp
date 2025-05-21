package modeles;
import utils.DBConnection;

import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ModelRecherche {

    private  Connection connection;
    public ModelRecherche(){
        connection = DBConnection.getConnection();
    }
    public DefaultTableModel Rechercher(String name) {

        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{ "Nom", "Prénom", "date Naiss","telephone","Adresse","Spéciaité"});

        String query = "SELECT nom ,prenom,date_naissance,telephone,adresse,specialite FROM patients WHERE nom LIKE ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, "%" + name + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                model.addRow(new String[]{
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("date_naissance"),
                        rs.getString("telephone"),
                        rs.getString("adresse"),
                        rs.getString("specialite")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }
}