package controllers;
import modeles.ModelLogin;
import modeles.ModelRecherche;
import views.LoginView;
import views.dashbordSecréteriaView;
import views.tableView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import views.RechercheView;


public class dshbordSecréteriaControllers {
    private dashbordSecréteriaView secreteriaView;
    public dshbordSecréteriaControllers(dashbordSecréteriaView secreteriaView){
        this.secreteriaView = secreteriaView;
            secreteriaView.addLogautListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                secreteriaView.dispose();
                secreteriaView.Deconnexion();

            }
        });
            secreteriaView.addRendvouListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    tableView tv = new tableView();

                    tv.afficherTable();
                    tableController tableCont = new tableController(tv);
                    tv.setVisible(true);
                }
            });

            secreteriaView.RechercheListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    RechercheView RechV = new RechercheView();
                    ModelRecherche RechM = new ModelRecherche();
                    RechercheController RechC = new RechercheController(RechV,RechM);
                    RechV.setVisible(true);
                }
            });
    }

}
