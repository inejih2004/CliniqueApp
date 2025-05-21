package dao;
import modeles.Patient;
import modeles.Rendezvous;
import utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RendezvousDAO {
	   public List<Patient> getPatientsByMedecinSpecialty(int medecinId) {
	        List<Patient> patients = new ArrayList<>();
	        String sql = "SELECT DISTINCT p.* " +
	                     "FROM patients p " +
	                     "JOIN rdv r ON p.id = r.patient_id " +
	                     "JOIN users u ON r.medecin_id = u.id " +
	                     "JOIN users u2 ON u2.specialite = u.specialite " +
	                     "WHERE u2.id = ?";

	        try (Connection conn = DBConnection.getConnection();
	             PreparedStatement pstmt = conn.prepareStatement(sql)) {
	            
	            pstmt.setInt(1, medecinId);
	            ResultSet rs = pstmt.executeQuery();
	            
	            while (rs.next()) {
	                Patient p = new Patient();
	                p.setId(rs.getInt("id"));
	                p.setNom(rs.getString("nom"));
	                p.setPrenom(rs.getString("prenom"));
	                p.setDateNaissance(rs.getDate("date_naissance"));
	                p.setTelephone(rs.getString("telephone"));
	                p.setAdresse(rs.getString("adresse"));
	                patients.add(p);
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return patients;
	    }

    public List<Rendezvous> getAllRendezvous() {
        List<Rendezvous> rendezVousList = new ArrayList<>();
        String query = "SELECT * FROM rdv";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Rendezvous rdv = new Rendezvous();
                rdv.setId(rs.getInt("id"));
                rdv.setPatientId(rs.getInt("patient_id"));
                rdv.setMedecinId(rs.getInt("medecin_id"));
                rdv.setDateRdv(rs.getDate("date_rdv"));
                rdv.setHeureRdv(rs.getString("heure_rdv"));
                rdv.setStatut(rs.getString("statut"));
                rendezVousList.add(rdv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rendezVousList;
    }

    public void insertRendezvous(Rendezvous rdv) {
        String query = "INSERT INTO rdv (patient_id, medecin_id, date_rdv, heure_rdv, statut) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, rdv.getPatientId());
            pstmt.setInt(2, rdv.getMedecinId());
            pstmt.setDate(3, new java.sql.Date(rdv.getDateRdv().getTime()));
            pstmt.setString(4, rdv.getHeureRdv());
            pstmt.setString(5, rdv.getStatut());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateRendezvous(Rendezvous rdv) {
        String query = "UPDATE rdv SET patient_id = ?, medecin_id = ?, date_rdv = ?, heure_rdv = ?, statut = ? WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, rdv.getPatientId());
            pstmt.setInt(2, rdv.getMedecinId());
            pstmt.setDate(3, new java.sql.Date(rdv.getDateRdv().getTime()));
            pstmt.setString(4, rdv.getHeureRdv());
            pstmt.setString(5, rdv.getStatut());
            pstmt.setInt(6, rdv.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteRendezvous(int id) {
        String query = "DELETE FROM rdv WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    public List<Rendezvous> getRendezVousByMedecin(int medecinId) {
        List<Rendezvous> rdvs = new ArrayList<>();
        String sql = "SELECT r.id, p.nom AS patient_nom, r.date_rdv, r.heure_rdv, r.statut " +
                     "FROM rdv r JOIN patients p ON r.patient_id = p.id " +
                     "WHERE r.medecin_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, medecinId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Rendezvous rdv = new Rendezvous();
                rdv.setId(rs.getInt("id"));
                rdv.setPatientNom(rs.getString("patient_nom")); // Assuming a setter for patient name
                rdv.setDateRdv(rs.getDate("date_rdv"));
                rdv.setHeureRdv(rs.getString("heure_rdv"));
                rdv.setStatut(rs.getString("statut"));
                rdvs.add(rdv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rdvs;
    
    }
    
}