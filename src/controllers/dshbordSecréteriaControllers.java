package controllers;
import modeles.ModelLogin;
import modeles.ModelRecherche;
import views.LoginView;
import views.dashbordSecréteriaView;
import views.tableView;
import views.PatientFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import views.RechercheView;

public class dshbordSecréteriaControllers {
    private dashbordSecréteriaView secreteriaView;

    public dshbordSecréteriaControllers(dashbordSecréteriaView secreteriaView) {
        this.secreteriaView = secreteriaView;

        // Logout button listener
        secreteriaView.addLogautListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                secreteriaView.dispose();
                secreteriaView.Deconnexion();
            }
        });

        // Rendezvous button listener
        secreteriaView.addRendvouListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tableView tv = new tableView();
                tv.afficherTable();
                tableController tableCont = new tableController(tv);
                tv.setVisible(true);
            }
        });

        // Search button listener
        secreteriaView.RechercheListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RechercheView RechV = new RechercheView();
                ModelRecherche RechM = new ModelRecherche();
                RechercheController RechC = new RechercheController(RechV, RechM);
                RechV.setVisible(true);
            }
        });

        // Patient management button listener
        secreteriaView.addPatientListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    PatientFrame patientFrame = new PatientFrame(secreteriaView); // Pass dashboard reference
                    patientFrame.setVisible(true);
                    // Removed: secreteriaView.dispose(); // Keep dashboard open
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(secreteriaView, "Erreur: " + ex.getMessage());
                }
            }
        });
    }
}