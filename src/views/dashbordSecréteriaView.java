package views;
import controllers.LoginControllers;
import modeles.ModelLogin;
import controllers.dshbordSecréteriaControllers;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class dashbordSecréteriaView extends JFrame {
    // declération
    private JPanel buttonPanel;
    private JButton btnRécherche;
    private JButton btnpatient;
    private JButton btnRendvou;
    private JButton btnLogout;
    private JLabel ditBonjour;
    private JPanel ditBonjourPanel;
    private JPanel CardsContainer;

    // function pour deconnexion
    public void addLogautListener(ActionListener listener) {
        btnLogout.addActionListener(listener);
    }

    // function pour les rendvou
    public void addRendvouListener(ActionListener listener) {
        btnRendvou.addActionListener(listener);
    }

    // function pour Récherche
    public void RechercheListener(ActionListener listener) {
        btnRécherche.addActionListener(listener);
    }

    // function pour gestion de patient
    public void addPatientListener(ActionListener listener) {
        btnpatient.addActionListener(listener);
    }
  
    // function pour afficher le Message Bonjour
    public void ditBonjour(String name, String prenom) {
        ditBonjourPanel.setLayout(new BoxLayout(ditBonjourPanel, BoxLayout.Y_AXIS));
        ditBonjourPanel.setBackground(Color.decode("#F0F4F8"));
        ditBonjour = new JLabel(" Bienvenue" + " " + name + " " + prenom);
        ditBonjour.setFont(new Font("Arial", Font.BOLD, 30));
        ditBonjour.setBorder(new EmptyBorder(0, 266, 10, 0));
        ditBonjour.setForeground(Color.decode("#023859"));
        ditBonjourPanel.add(Box.createVerticalStrut(20));
        ditBonjourPanel.add(ditBonjour);
    }

    // function pour deconnexion
    public void Deconnexion() {
        LoginView login = new LoginView();
        ModelLogin model = new ModelLogin();
        LoginControllers control = new LoginControllers(login, model);
        login.setVisible(true);
    }

    // function pour rédirect
    public static void Redirect() {
        dashbordSecréteriaView dashbordview = new dashbordSecréteriaView();
        dshbordSecréteriaControllers dashbordContr = new dshbordSecréteriaControllers(dashbordview);
        dashbordview.setVisible(true);
    }

    public dashbordSecréteriaView() {
        this.setTitle("Dashbore Sécréteria");
        this.setSize(800, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);

        // creation cardContainer
        CardsContainer = new JPanel();
        CardsContainer.setLayout(new GridLayout(1, 3, 20, 20));

        // création
        buttonPanel = new JPanel();
        btnRécherche = new JButton("Récherche");
        btnpatient = new JButton("gestion de patient");
        btnRendvou = new JButton("les Rendvou");
        btnLogout = new JButton("de Connexion");
        ditBonjourPanel = new JPanel();

        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(Color.decode("#E3F6F5"));
        buttonPanel.setBorder(new EmptyBorder(0, 10, 0, 10));

        // design pour les button
        btnRécherche.setBackground(Color.decode("#04BFAD"));
        btnRécherche.setForeground(Color.white);
        btnRécherche.setFont(new Font("Arial", Font.BOLD, 14));

        btnpatient.setBackground(new Color(4, 191, 173));
        btnpatient.setForeground(Color.white);
        btnpatient.setFont(new Font("Arial", Font.BOLD, 14));

        btnRendvou.setBackground(new Color(4, 191, 173));
        btnRendvou.setForeground(Color.white);
        btnRendvou.setFont(new Font("Arial", Font.BOLD, 14));

        btnLogout.setBackground(new Color(4, 191, 173));
        btnLogout.setForeground(Color.white);
        btnLogout.setFont(new Font("Arial", Font.BOLD, 14));

        // width and height of btn
        Dimension btnSize = new Dimension(300, 40);
        btnRécherche.setMaximumSize(btnSize);
        btnRendvou.setMaximumSize(btnSize);
        btnpatient.setMaximumSize(btnSize);
        btnLogout.setMaximumSize(btnSize);

        // ajouter les button sur la panel
        buttonPanel.add(Box.createVerticalStrut(50));
        buttonPanel.add(btnRécherche);
        buttonPanel.add(Box.createVerticalStrut(40));
        buttonPanel.add(btnpatient);
        buttonPanel.add(Box.createVerticalStrut(40));
        buttonPanel.add(btnRendvou);
        buttonPanel.add(Box.createVerticalStrut(200));
        buttonPanel.add(btnLogout);
        buttonPanel.add(Box.createVerticalStrut(40));

        this.getContentPane().add(buttonPanel, BorderLayout.WEST);
        this.getContentPane().add(ditBonjourPanel, BorderLayout.NORTH);
        this.getContentPane().add(CardsContainer, BorderLayout.CENTER);
    }
}