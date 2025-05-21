package modeles;
import utils.DBConnection;
import java.sql.*;

public class ModelLogin{
private  Connection connection;


public  String name;
public  String prenom;

public ModelLogin(){
    connection = DBConnection.getConnection();
}
public String checkLogin(String Numero , String password) {
    String query = "SELECT role,nom,prenom FROM users WHERE Numero = ? AND password = ?";
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
        stmt.setString(1, Numero);
        stmt.setString(2, password);
        ResultSet rs = stmt.executeQuery();
        if(rs.next()){
              name = rs.getString("nom");
              prenom = rs.getString("prenom");
            return rs.getString("role");

        }else{
            return null;
        }
    }catch (SQLException e){
        e.printStackTrace();
          return null;
    }


}

}




