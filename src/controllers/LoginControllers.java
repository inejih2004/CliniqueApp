package controllers;
import views.LoginView;
import views.dashbordSecréteriaView;
import views.MedecinDashboard;
import modeles.ModelLogin;
import utils.UserSession;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class LoginControllers {
    private LoginView view;
    private ModelLogin model;

    public LoginControllers(LoginView view, ModelLogin model) {
        this.view = view;
        this.model = model;
        this.view.addLoginListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String Numero = view.getNumero();
                String password = view.getPassword();
                if (Numero.isEmpty() || password.isEmpty()) {
                    view.champsObligotoire();
                } else {
                    String role = model.checkLogin(Numero, password);
                    if (role == null) {
                        view.showMessage("la Numéro ou la mot de passe incorrécte");
                    } else {
                        // Set UserSession data
                        UserSession.medecinId = model.id;
                        UserSession.medecinNom = model.name;
                        UserSession.medecinPrenom = model.prenom;
                        UserSession.medecinSpecialite = model.specialite;
                        UserSession.setCurrentUserId(model.id);

                        view.dispose(); // Close login window
                        if (role.equalsIgnoreCase("medecin")) {
                            MedecinDashboard dashboard = new MedecinDashboard();
                            dashboard.setVisible(true);
                        } else if (role.equalsIgnoreCase("secretaire")) {
                            dashbordSecréteriaView dashbordview = new dashbordSecréteriaView();
                            dshbordSecréteriaControllers dashbordContr = new dshbordSecréteriaControllers(dashbordview);
                            dashbordview.ditBonjour(model.name, model.prenom);
                            dashbordview.setVisible(true);
                        }
                    }
                }
            }
        });
    }
}