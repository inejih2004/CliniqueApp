package views;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import views.dashbordSecréteriaView;
import controllers.RechercheController;
public class RechercheView  extends JFrame {

    // declération
    private JPanel RecherchePanel;
    private JPanel TablePanel;
    private JLabel Recherchelabel;
    private JTextField RechercheInput;
    private JButton btnRecherche;
    private JTable tableRecherche;
     private JScrollPane jscroll;
    // functions pour afficher la frame Recherche
   /* public void AfficherFrameR() {
        RechercheView RechV = new RechercheView();
        RechercheController RechC = new RechercheController(RechV);
        this.setVisible(true);
    }*/



    // function pour btn Rechercher
    public void addRechercheListener(ActionListener listener) {
        btnRecherche.addActionListener(listener);
    }

    // fucntion pour recuper la saisi
    public String RecuperValeurduLinput() {
       String resultat = RechercheInput.getText();
       return resultat;
    }
    // function pour afficher le message Erreur
     public void messageErreur(){
        JOptionPane.showMessageDialog(null,"ce champs ne peut être  vide");
     }

   // function pour affiche le table aprés la Recherche
    public void  updateTab(DefaultTableModel model) {
        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(null, "Aucun résultat trouvé");
        } else {
            tableRecherche.setModel(model);
        }
    }
    public RechercheView(){
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(950,400);
        this.setLocationRelativeTo(null);
        this.setTitle("Recherche");
        this.setResizable(false);

        // creation
        tableRecherche = new JTable();
        RecherchePanel = new JPanel();
        TablePanel = new JPanel();
        RecherchePanel.setLayout(new BoxLayout(RecherchePanel,BoxLayout.Y_AXIS));
        RecherchePanel.setBackground(Color.decode("#E3F6F5"));
        RecherchePanel.setBorder(new EmptyBorder(0,20,0,0));


        Recherchelabel = new JLabel("Rechercher par nom ou numéro");
        RechercheInput = new JTextField();
        btnRecherche = new JButton("Recherche");
        jscroll = new JScrollPane(tableRecherche);


        // design pour TabelPanel
        TablePanel.setLayout(new BoxLayout(TablePanel,BoxLayout.X_AXIS));
        TablePanel.setBackground(Color.decode("#E3F6F5"));

        // design pour le button
        btnRecherche.setBackground(new Color(4,191,173));
        btnRecherche.setForeground(Color.white);
        btnRecherche.setFont(new Font("Arial",Font.BOLD,14));


        // design pour le table
        tableRecherche.setRowHeight(30);
        tableRecherche.setFont(new Font("segoe UI",Font.PLAIN,12));
        tableRecherche.setForeground(Color.DARK_GRAY);
        tableRecherche.setDefaultEditor(Object.class,null);

        // design table recherche
        tableRecherche.setMaximumSize(new Dimension(500,400));


        // design pour RechercheInput
        RechercheInput.setMaximumSize(new Dimension(450,30));

        // design pour le label
           Recherchelabel.setFont(new Font("Arial", Font.BOLD, 18));

        // add btn and label and field on panel
        RecherchePanel.add(Box.createVerticalStrut(40));
        RecherchePanel.add(Recherchelabel);
        RecherchePanel.add(Box.createVerticalStrut(10));
        RecherchePanel.add(RechercheInput);
        RecherchePanel.add(Box.createVerticalStrut(10));
        RecherchePanel.add(btnRecherche);
        TablePanel.add(jscroll);

        // add panel on frame
        this.getContentPane().add(RecherchePanel, BorderLayout.CENTER);
        this.getContentPane().add(TablePanel,BorderLayout.EAST);

    }



}
