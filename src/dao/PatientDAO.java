package dao;

import modeles.Patient;
import utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

public class PatientDAO {
	
    public List<Patient> getAllPatients() {
        List<Patient> patients = new ArrayList<>();
        String query = "SELECT * FROM patients";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Patient p = new Patient();
                p.setId(rs.getInt("id"));
                p.setNom(rs.getString("nom"));
                p.setPrenom(rs.getString("prenom"));
                p.setDateNaissance(rs.getDate("date_naissance"));
                p.setTelephone(rs.getString("telephone"));
                p.setAdresse(rs.getString("adresse"));
                p.setSpecialite(rs.getString("specialite"));
                patients.add(p);
            }
        } catch (SQLException e) {
        	  e.printStackTrace(); 
        	 
        }
        return patients;
    }

    public void insertPatient(Patient p) {
    	String query = "INSERT INTO patients (nom, prenom, date_naissance, telephone, adresse, specialite) VALUES (?, ?, ?, ?, ?, ?)";

        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, p.getNom());
            pstmt.setString(2, p.getPrenom());
            pstmt.setDate(3, new java.sql.Date(p.getDateNaissance().getTime()));
            pstmt.setString(4, p.getTelephone());
            pstmt.setString(5, p.getAdresse());
            pstmt.setString(6, p.getSpecialite()); 
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "\"❌ Une erreur est survenue lors de l'ajout du patient. Veuillez réessayer.\"\r\n"
            		+ "", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void updatePatient(Patient p) {
    	String query = "UPDATE patients SET nom = ?, prenom = ?, date_naissance = ?, telephone = ?, adresse = ?, specialite = ? WHERE id = ?";

        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, p.getNom());
            pstmt.setString(2, p.getPrenom());
            pstmt.setDate(3, new java.sql.Date(p.getDateNaissance().getTime()));
            pstmt.setString(4, p.getTelephone());
            pstmt.setString(5, p.getAdresse());
            pstmt.setString(6, p.getSpecialite());
            pstmt.setInt(7, p.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "\"\"⚠️ Impossible de modifier le patient. Vérifiez les champs et réessayez.\"\r\n"
            		+ "", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void deletePatient(int id) {
        String query = "DELETE FROM patients WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "\"⚠️ Erreur lors de la suppression du patient. Vérifiez que le patient est bien sélectionné.\"\r\n"
            		+ "", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}