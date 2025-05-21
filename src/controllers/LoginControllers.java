package controllers;
import views.LoginView;
import views.dashbordSecréteriaView;
import modeles.ModelLogin;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class LoginControllers{
    private LoginView view;
    private ModelLogin model;
    /*private String name = model.name;
    private String prenom = model.prenom;*/


    public LoginControllers(LoginView view, ModelLogin model){
        this.view = view;
        this.model = model;
        this.view.addLoginListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String  Numero = view.getNumero();
                String password = view.getPassword();
                if(Numero.isEmpty() || password.isEmpty()){
                    view.champsObligotoire();
                }
                else{
                String role = model.checkLogin(Numero,password);
                if(role == null){
                    view.showMessage("la Numéro ou la mot de passe incorrécte");
                }else{
                    if(role.equalsIgnoreCase("medecin")) {
                         view.showDocto("Bonjour");

                    }else{
                        if(role.equalsIgnoreCase("secretaire")) {
                            view.dispose();
                            dashbordSecréteriaView dashbordview = new dashbordSecréteriaView();
                            dshbordSecréteriaControllers dashbordContr = new dshbordSecréteriaControllers(dashbordview);

                            dashbordview.ditBonjour(model.name, model.prenom);
                            dashbordview.setVisible(true);


                        }
                        }
                    }
                }
            }
        });
    }


}