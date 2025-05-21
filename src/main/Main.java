
package main;
import utils.DBConnection;
import views.LoginView;
import modeles.ModelLogin;
import controllers.LoginControllers;
import controllers.dshbordSecréteriaControllers;
import views.dashbordSecréteriaView;
import views.tableView;
import controllers.tableController;
import modeles.tableModel;
public class Main {


        public static void main(String[] args) {
            DBConnection.getConnection();

            LoginView login = new LoginView();
            ModelLogin model = new ModelLogin();
            LoginControllers control = new LoginControllers(login,model);
            login.setVisible(true);

        }


    }


