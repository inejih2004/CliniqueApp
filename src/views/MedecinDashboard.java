package views;

import dao.RendezvousDAO;
import modeles.ModelLogin;
import modeles.Patient;
import modeles.Rendezvous;
import utils.DBConnection;
import utils.UserSession;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import controllers.LoginControllers;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import java.text.SimpleDateFormat;
import java.util.List;



public class MedecinDashboard extends JFrame {
    private RendezvousDAO rendezvousDAO;
    private DefaultTableModel tableModel;
    private JTable rendezVousTable, patientsTable;
    private Timer animationTimer;
    private float opacity = 0.0f;
    private DefaultTableModel tableModelRdv, tableModelPatients;

    public MedecinDashboard() {
        rendezvousDAO = new RendezvousDAO();
        initUI();
        loadRendezVousWithAnimation();
    }
  

    private void initUI() {
        setTitle("Tableau de Bord du Médecin");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(50, 60, 150), 0, getHeight(), new Color(100, 50, 150));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.setColor(new Color(255, 255, 255, 20));
                for (int i = 0; i < getWidth(); i += 50) {
                    g2d.drawLine(i, 0, i - 20, 50);
                    g2d.drawLine(i, getHeight(), i - 20, getHeight() - 50);
                }
            }
        };
        backgroundPanel.setLayout(new BorderLayout(10, 10));
        add(backgroundPanel);

        JPanel topPanel = new JPanel(new BorderLayout(10, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                RoundRectangle2D roundedRect = new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20);
                g2d.setColor(new Color(255, 255, 255, 200));
                g2d.fill(roundedRect);
                g2d.setColor(new Color(0, 0, 0, 50));
                g2d.draw(roundedRect);
            }
        };
        topPanel.setOpaque(false);

        JPanel leftInfoPanel = new JPanel(new GridLayout(3, 1));
        leftInfoPanel.setOpaque(false);
    
        JLabel nomLabel = new JLabel("👨‍⚕️ Dr. " + UserSession.medecinNom);
        JLabel prenomLabel = new JLabel("🧑 Prénom: " + UserSession.medecinPrenom);
        JLabel specLabel = new JLabel("🏥 Spécialité: " + UserSession.medecinSpecialite);

        for (JLabel lbl : new JLabel[]{nomLabel, prenomLabel, specLabel}) {
            lbl.setFont(new Font("Poppins", Font.BOLD, 18));
            lbl.setForeground(new Color(60, 60, 60));
            leftInfoPanel.add(lbl);
        }

        topPanel.add(leftInfoPanel, BorderLayout.WEST);
        backgroundPanel.add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                RoundRectangle2D roundedRect = new RoundRectangle2D.Double(10, 10, getWidth() - 20, getHeight() - 20, 20, 20);
                g2d.setColor(new Color(255, 255, 255, 230));
                g2d.fill(roundedRect);
                g2d.setColor(new Color(0, 0, 0, 100));
                g2d.draw(roundedRect);
            }
        };
        centerPanel.setOpaque(false);
        JButton logoutButton = new JButton("Se Déconnecter");
        logoutButton.setFont(new Font("Poppins", Font.BOLD, 16));
        logoutButton.setBackground(new Color(70, 130, 180)); // Steel Blue
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false);
        logoutButton.setPreferredSize(new Dimension(180, 45));

        // RDV Table
        String[] columns = {"ID", "Patient", "Date", "Heure", "Statut"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        rendezVousTable = new JTable(tableModel);
        rendezVousTable.setShowGrid(false);
        rendezVousTable.setIntercellSpacing(new Dimension(0, 0));
        rendezVousTable.setRowHeight(35);
        rendezVousTable.setFont(new Font("Roboto", Font.PLAIN, 16));
        rendezVousTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(row % 2 == 0 ? new Color(240, 248, 255) : new Color(245, 245, 245));
                if (isSelected) {
                    c.setBackground(new Color(138, 43, 226));
                    c.setForeground(Color.WHITE);
                } else {
                    c.setForeground(Color.BLACK);
                }
                return c;
            }
        });

        // Patients Table
        String[] columnsPatients = {"ID", "Nom", "Prénom", "Téléphone", "Adresse"};
        tableModelPatients = new DefaultTableModel(columnsPatients, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        patientsTable = new JTable(tableModelPatients);
        patientsTable.setShowGrid(false);
        patientsTable.setIntercellSpacing(new Dimension(0, 0));
        patientsTable.setRowHeight(35);
        patientsTable.setFont(new Font("Roboto", Font.PLAIN, 16));
        patientsTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(row % 2 == 0 ? new Color(240, 248, 255) : new Color(245, 245, 245));
                if (isSelected) {
                    c.setBackground(new Color(138, 43, 226));
                    c.setForeground(Color.WHITE);
                }
                return c;
            }
        });

        // ✅ TABBED PANE FIX
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("🗓️ Rendez-vous", new JScrollPane(rendezVousTable));
        tabbedPane.addTab("🧑‍🤝‍🧑 Patients", new JScrollPane(patientsTable));
        centerPanel.add(tabbedPane, BorderLayout.CENTER);

        backgroundPanel.add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton manageButton = new JButton("Gérer les Rendez-vous") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                RoundRectangle2D roundedRect = new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 15, 15);
                g2d.setColor(getBackground());
                g2d.fill(roundedRect);
                g2d.setColor(getForeground());
                g2d.draw(roundedRect);
                super.paintComponent(g);
            }
        };
        manageButton.setFont(new Font("Poppins", Font.BOLD, 18));
        manageButton.setBackground(new Color(255, 99, 71));
        manageButton.setForeground(Color.WHITE);
        manageButton.setFocusPainted(false);
        manageButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        manageButton.addActionListener(e -> {
        	new views.RendezvousFrame(this).setVisible(true); // ✅ Fix: pass the dashboard reference
            dispose();
        });
        logoutButton.addActionListener(event -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                "Êtes-vous sûr de vouloir vous déconnecter ?", "Confirmation",
                JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                // Close the database connection
                DBConnection.closeConnection();

                // Clear UserSession data
                UserSession.medecinId = 0;
                UserSession.medecinNom = null;
                UserSession.medecinPrenom = null;
                UserSession.medecinSpecialite = null;
                UserSession.setCurrentUserId(0);

                // Show login window with fresh ModelLogin and LoginControllers
                LoginView loginView = new LoginView();
                ModelLogin modelLogin = new ModelLogin();
                LoginControllers loginControllers = new LoginControllers(loginView, modelLogin);
                loginView.setVisible(true);

                // Close this dashboard
                dispose();
            }
        });
        manageButton.setPreferredSize(new Dimension(280, 50));
        bottomPanel.setOpaque(false);
        bottomPanel.add(manageButton);
        backgroundPanel.add(bottomPanel, BorderLayout.SOUTH);
        bottomPanel.add(logoutButton);

        manageButton.setToolTipText("<html><b>Gérer les Rendez-vous</b><br>Ouvrir l'interface de gestion.</html>");
        rendezVousTable.setToolTipText("<html><b>Vos Rendez-vous</b><br>Cliquez pour plus d'options.</html>");
    }
    
   

    private void loadPatientsBySpecialty() {
        tableModelPatients.setRowCount(0);
        List<Patient> patients = rendezvousDAO.getPatientsByMedecinSpecialty(UserSession.medecinId);
        for (Patient p : patients) {
            tableModelPatients.addRow(new Object[]{
                p.getId(),
                p.getNom(),
                p.getPrenom(),
                p.getTelephone(),
                p.getAdresse()
            });
        }
    }

    private void loadRendezVousWithAnimation() {
        tableModel.setRowCount(0);
        List<Rendezvous> rdvs = rendezvousDAO.getRendezVousByMedecin(UserSession.medecinId);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        opacity = 0.0f;

        animationTimer = new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (opacity < 1.0f) {
                    opacity += 0.1f;
                    rendezVousTable.setOpaque(false);
                    rendezVousTable.getParent().repaint();
                } else {
                    ((Timer) e.getSource()).stop();
                    for (Rendezvous rdv : rdvs) {
                        tableModel.addRow(new Object[]{
                                rdv.getId(),
                                rdv.getPatientNom(),
                                sdf.format(rdv.getDateRdv()),
                                rdv.getHeureRdv(),
                                rdv.getStatut()
                        });
                    }
                }
            }
        });
        animationTimer.start();
        loadPatientsBySpecialty();
    }
}
