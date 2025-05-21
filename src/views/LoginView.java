package views;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;


public class LoginView extends JFrame {
  // declearation
    private JPanel panelLogin;
    private JTextField Numéro;
    private JPasswordField password ;
    private JLabel labelNuméro;
    private JLabel labelPassword;
    private JButton btnConnexion;


    // getrs pour le numero
    public String getNumero(){
        return Numéro.getText();
    }
    public String getPassword(){
        return new String(password.getPassword());
    }

    // function pour message invalid
       public void showMessage(String Message){
        JOptionPane.showMessageDialog(null,Message);
       }
       public void showDocto(String MessageDoctor){
        JOptionPane.showMessageDialog(null,MessageDoctor);
    }

    // function pour afficher le message invaid champs obligtoire
    public void champsObligotoire(){
        JOptionPane.showMessageDialog(null,"les champs ne peut pas être vide");
    }
    // fonction pour btn
    public void addLoginListener(ActionListener listener){
        btnConnexion.addActionListener(listener);
    }

    public  LoginView (){
        this.setTitle("connexion");
        this.setSize(400,500);
        this.setBackground(Color.gray);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);


        // creation
        panelLogin = new JPanel();
        Numéro =  new JTextField();
        password = new JPasswordField();
        labelNuméro = new JLabel("Numéro du téléphone");
        labelPassword = new JLabel("Mot de passe");
        btnConnexion = new JButton("Connexion");


        // desgin pouur panlLogin
        panelLogin.setLayout(new BoxLayout(panelLogin,BoxLayout.Y_AXIS));
        panelLogin.setBorder(new EmptyBorder(133,50,0,50));
        panelLogin.setBackground(new Color(240,245,250));
        // postiotn
       /* labelNuméro.setBounds(20,120,260,20);
        Numéro.setBounds(20,150,260,30);
        labelPassword.setBounds(20,190,260,20);
        password.setBounds(20,220,260,30);
        btnConnexion.setBounds(90,280,120,30);*/

        // widht and height
        Numéro.setMaximumSize(new Dimension(500,30));
        password.setMaximumSize(new Dimension(500,30));

        // color et font pour label
          labelNuméro.setFont(new Font("Segoe UI", Font.BOLD,14));
          labelPassword.setFont(new Font("Segoe UI", Font.BOLD,14));

        // color et Font pour btn
        btnConnexion.setBackground(new Color(4,191,173));
        btnConnexion.setFont(new Font("Segoe UI",Font.BOLD,14));
        btnConnexion.setForeground(Color.white);
        btnConnexion.setFocusPainted(false);
        btnConnexion.setBorderPainted(false);
        btnConnexion.setMaximumSize(new Dimension(150,30));


       // Ajouter les element sur le panel
        panelLogin.add(labelNuméro);
        panelLogin.add(Box.createVerticalStrut(10));
        panelLogin.add(Numéro);
        panelLogin.add(Box.createVerticalStrut(10));
        panelLogin.add(labelPassword);
        panelLogin.add(Box.createVerticalStrut(10));
        panelLogin.add(password);
        panelLogin.add(Box.createVerticalStrut(10));
        panelLogin.add(btnConnexion);


        // Ajouter le element sur la frame
           /*add(labelNuméro);
           add(Numéro);
           add(labelPassword);
           add(password);
           add(btnConnexion);*/
           this.getContentPane().add(panelLogin,BorderLayout.CENTER);


    }

}
