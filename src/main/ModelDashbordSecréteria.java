package models;
import utils.DBConnection;
import java.sql.Connection;

public class ModelDashbordSecréteria {
    private  Connection connection;
    public ModelDashbordSecréteria(){
        connection = DBConnection.getConnection();
    }

}
