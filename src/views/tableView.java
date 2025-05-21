package views;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionListener;
import modeles.tableModel;
import controllers.tableController;
public class tableView extends JFrame{

    private JTable tabel;
    private JPanel panelBtnClose;
    private JButton btnClose;
    private JTableHeader header;

    // function pour afficher le table de Rendvou
    public void afficherTable(){
        tableModel model = new tableModel();
        DefaultTableModel data = model.getTableData();
        tabel.setModel(data);

    }

    // function btn close
     public void  addCloseFrameListener(ActionListener listener){
        btnClose.addActionListener(listener);
     }
 // function pour férmer la frame;
    public void closeFrame(){
        this.dispose();
    }

    public tableView(){
        this.setTitle("les Rendvou");
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setSize(700,400);
        this.setLocationRelativeTo(null);
        this.setResizable(false);


        // creation
        tabel = new JTable();
        panelBtnClose = new JPanel();
        btnClose = new JButton("Fermer");
        header = tabel.getTableHeader();
        add(new JScrollPane(tabel));

        // design pour le table
        tabel.setRowHeight(28);
        tabel.setFont(new Font("segoe UI",Font.PLAIN,14));
        tabel.setForeground(Color.DARK_GRAY);
        tabel.setDefaultEditor(Object.class,null);


        // design pour le tete de tabel
        header.setFont(new Font("Segoe UI", Font.BOLD,16));
        header.setBackground(Color.decode("#045B8D"));
        header.setForeground(Color.white);

        // design panel
        panelBtnClose.setLayout(new BoxLayout(panelBtnClose,BoxLayout.Y_AXIS));
        panelBtnClose.setBorder(new EmptyBorder(10,300,10,0));
        panelBtnClose.setBackground(Color.white);
        // add button on panel
        panelBtnClose.add(btnClose);

        // design btnClose
        btnClose.setForeground(Color.white);
        btnClose.setBackground(new Color(4,191,173));
        btnClose.setFont(new Font("Segoe UI", Font.BOLD,14));

        // add panel on frame
        this.getContentPane().add(panelBtnClose,BorderLayout.SOUTH);
    }


}
