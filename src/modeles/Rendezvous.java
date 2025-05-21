package modeles;

import java.util.Date;

public class Rendezvous {
    private int id;
    private int patientId;
    private int medecinId;
    private Date dateRdv;
    private String heureRdv;
    private String statut;
    private String patientNom;

    // Constructor
    public Rendezvous() {}

    public Rendezvous(int id, int patientId, int medecinId, Date dateRdv, String heureRdv, String statut,String patientNom) {
        this.id = id;
        this.patientId = patientId;
        this.medecinId = medecinId;
        this.dateRdv = dateRdv;
        this.heureRdv = heureRdv;
        this.statut = statut;
        this.patientNom = patientNom;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getPatientId() { return patientId; }
    public void setPatientId(int patientId) { this.patientId = patientId; }
    public int getMedecinId() { return medecinId; }
    public void setMedecinId(int medecinId) { this.medecinId = medecinId; }
    public Date getDateRdv() { return dateRdv; }
    public void setDateRdv(Date dateRdv) { this.dateRdv = dateRdv; }
    public String getHeureRdv() { return heureRdv; }
    public void setHeureRdv(String heureRdv) { this.heureRdv = heureRdv; }
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
    public String getPatientNom() { return patientNom; }
    public void setPatientNom(String patientNom) { this.patientNom = patientNom; }
}