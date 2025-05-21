package controllers;
import views.RechercheView;
import modeles.ModelRecherche;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

public class RechercheController {
   private  RechercheView Recherche;
   private ModelRecherche RechercheM;
    public RechercheController(RechercheView Recherche ,ModelRecherche RechercheM){
        this.Recherche = Recherche;
        this.RechercheM = RechercheM;

        Recherche.addRechercheListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String result = Recherche.RecuperValeurduLinput();
                if (result.isEmpty()) {
                    Recherche.messageErreur();
                } else {
                    DefaultTableModel resulta;
                    resulta = RechercheM.Rechercher(result);
                    Recherche.updateTab(resulta);
                }
            }
        });
    }
}
