package views;

import dao.RendezvousDAO;
import dao.PatientDAO;
import modeles.Rendezvous;
import utils.UserSession;
import modeles.Patient;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class RendezvousFrame extends JFrame {
    private RendezvousDAO rendezvousDAO;
    private PatientDAO patientDAO;
    private DefaultTableModel tableModel;
    private JTable rendezvousTable;
    private JComboBox<String> patientCombo;
    private JTextField heureRdvField;
    private JDateChooser dateRdvChooser;
    private JComboBox<String> statutComboBox;
    private MedecinDashboard MedicinFrame;
    private int selectedRendezvousId = -1;
    private Map<String, Integer> patientNameToIdMap;

    public RendezvousFrame(MedecinDashboard MedecinDashboard) {
        this.MedicinFrame = MedecinDashboard;
        rendezvousDAO = new RendezvousDAO();
        patientDAO = new PatientDAO();
        patientNameToIdMap = new HashMap<>();
        initUI();
        refreshTable();
    }

    private void initUI() {
        setTitle("Rendezvous Management");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        formPanel.add(new JLabel("Patient:"));
        patientCombo = new JComboBox<>();
        loadPatients();
        formPanel.add(patientCombo);

        formPanel.add(new JLabel("Date du RDV:"));
        dateRdvChooser = new JDateChooser();
        formPanel.add(dateRdvChooser);

        formPanel.add(new JLabel("Heure du RDV (HH:mm:ss):"));
        heureRdvField = new JTextField();
        formPanel.add(heureRdvField);

        formPanel.add(new JLabel("Statut:"));
        String[] statuts = {"programmé", "annulé", "terminé"};
        statutComboBox = new JComboBox<>(statuts);
        formPanel.add(statutComboBox);

        // Buttons Panel
        JPanel buttonPanel = new JPanel();
        JButton addButton = createShinyButton("Ajouter", new Color(0, 150, 0), new Color(0, 255, 0));
        JButton editButton = createShinyButton("Modifier", new Color(0, 0, 150), new Color(0, 0, 255));
        JButton deleteButton = createShinyButton("Supprimer", new Color(150, 0, 0), new Color(255, 0, 0));
        JButton refreshButton = createShinyButton("Rafraîchir", new Color(255, 140, 0), new Color(255, 215, 0));
        JButton previousButton = createShinyButton("Précédent", new Color(128, 0, 128), new Color(186, 85, 211));
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(previousButton);

        // Table
        String[] columns = {"ID", "Patient", "Médecin ID", "Date RDV", "Heure RDV", "Statut"};
        tableModel = new DefaultTableModel(columns, 0);
        rendezvousTable = new JTable(tableModel);
        rendezvousTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        rendezvousTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = rendezvousTable.getSelectedRow();
            if (selectedRow >= 0) {
                selectedRendezvousId = (int) tableModel.getValueAt(selectedRow, 0);
                patientCombo.setSelectedItem((String) tableModel.getValueAt(selectedRow, 1));
                try {
                    String dateStr = (String) tableModel.getValueAt(selectedRow, 3);
                    Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
                    dateRdvChooser.setDate(date);
                } catch (Exception ex) {
                    dateRdvChooser.setDate(null);
                }
                heureRdvField.setText((String) tableModel.getValueAt(selectedRow, 4));
                statutComboBox.setSelectedItem((String) tableModel.getValueAt(selectedRow, 5));
            }
        });

        add(formPanel, BorderLayout.NORTH);
        add(new JScrollPane(rendezvousTable), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> addRendezvous());
        editButton.addActionListener(e -> editRendezvous());
        deleteButton.addActionListener(e -> deleteRendezvous());
        refreshButton.addActionListener(e -> refreshTable());
        previousButton.addActionListener(e -> {
            setVisible(false);
            if (MedicinFrame != null) {
                MedicinFrame.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Erreur : Impossible de retourner au tableau de bord.",
                                             "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private JButton createShinyButton(String text, Color startColor, Color endColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Gradient background
                GradientPaint gp = new GradientPaint(0, 0, startColor, 0, getHeight(), endColor);
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30); // More rounded corners

                g2.dispose();
                super.paintComponent(g);
            }
        };

        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16)); // Modern font
        button.setPreferredSize(new Dimension(140, 40)); // Bigger button
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setContentAreaFilled(false);
        button.setOpaque(false);

        // Soft drop shadow effect
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.WHITE, 2, true),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));

        // Hover effect: glow
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3, true));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2, true));
            }
        });

        return button;
    }


    private void loadPatients() {
        try {
            List<Patient> patients = patientDAO.getAllPatients();
            patientCombo.removeAllItems();
            patientNameToIdMap.clear();
            for (Patient p : patients) {
                String fullName = p.getNom() + " " + p.getPrenom();
                patientCombo.addItem(fullName);
                patientNameToIdMap.put(fullName, p.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des patients.",
                                         "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addRendezvous() {
        if (!validateInputs()) return;

        Rendezvous rdv = new Rendezvous();
        String selectedPatient = (String) patientCombo.getSelectedItem();
        rdv.setPatientId(patientNameToIdMap.get(selectedPatient));
        rdv.setMedecinId(UserSession.getCurrentUserId());
        rdv.setDateRdv(dateRdvChooser.getDate());
        rdv.setHeureRdv(heureRdvField.getText().trim());
        rdv.setStatut((String) statutComboBox.getSelectedItem());

        rendezvousDAO.insertRendezvous(rdv);
        JOptionPane.showMessageDialog(this, "Rendez-vous ajouté avec succès!");
        refreshTable();
        clearFields();
    }

    private void editRendezvous() {
        if (selectedRendezvousId == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un rendez-vous à modifier.");
            return;
        }
        if (!validateInputs()) return;

        Rendezvous rdv = new Rendezvous();
        rdv.setId(selectedRendezvousId);
        String selectedPatient = (String) patientCombo.getSelectedItem();
        rdv.setPatientId(patientNameToIdMap.get(selectedPatient));
        rdv.setMedecinId(UserSession.getCurrentUserId());
        rdv.setDateRdv(dateRdvChooser.getDate());
        rdv.setHeureRdv(heureRdvField.getText().trim());
        rdv.setStatut((String) statutComboBox.getSelectedItem());

        rendezvousDAO.updateRendezvous(rdv);
        JOptionPane.showMessageDialog(this, "Rendez-vous modifié avec succès!");
        refreshTable();
        clearFields();
    }

    private void deleteRendezvous() {
        if (selectedRendezvousId == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un rendez-vous à supprimer.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Êtes-vous sûr de vouloir supprimer ce rendez-vous?",
                                                   "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            rendezvousDAO.deleteRendezvous(selectedRendezvousId);
            JOptionPane.showMessageDialog(this, "Rendez-vous supprimé avec succès!");
            refreshTable();
            clearFields();
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        try {
            List<Rendezvous> rendezVousList = rendezvousDAO.getAllRendezvous();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            for (Rendezvous rdv : rendezVousList) {
                String patientName = getPatientName(rdv.getPatientId());
                tableModel.addRow(new Object[]{
                    rdv.getId(),
                    patientName,
                    rdv.getMedecinId(),
                    sdf.format(rdv.getDateRdv()),
                    rdv.getHeureRdv(),
                    rdv.getStatut()
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de la mise à jour de la table: " + e.getMessage(),
                                         "Erreur", JOptionPane.ERROR_MESSAGE);
        }
        selectedRendezvousId = -1;
    }

    private String getPatientName(int patientId) {
        try {
            List<Patient> patients = patientDAO.getAllPatients();
            for (Patient p : patients) {
                if (p.getId() == patientId) {
                    return p.getNom() + " " + p.getPrenom();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Inconnu";
    }

    private boolean validateInputs() {
        // Patient: Non-empty
        if (patientCombo.getSelectedItem() == null || ((String) patientCombo.getSelectedItem()).trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un patient.",
                                         "Erreur", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Date du RDV: Not null, not in past
        Date dateRdv = dateRdvChooser.getDate();
        if (dateRdv == null) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une date de rendez-vous.",
                                         "Erreur", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        Date today = new Date();
        if (dateRdv.before(today) && !isSameDay(dateRdv, today)) {
            JOptionPane.showMessageDialog(this, "La date du rendez-vous ne peut pas être dans le passé.",
                                         "Erreur", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Heure du RDV: Valid HH:mm:ss
        String heureRdv = heureRdvField.getText().trim();
        if (!heureRdv.matches("^([0-1]?[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]$")) {
            JOptionPane.showMessageDialog(this, "L'heure du RDV doit être au format HH:mm:ss (ex: 14:30:00).",
                                         "Erreur", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Statut: Non-empty
        if (statutComboBox.getSelectedItem() == null || ((String) statutComboBox.getSelectedItem()).trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un statut.",
                                         "Erreur", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private boolean isSameDay(Date date1, Date date2) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.format(date1).equals(sdf.format(date2));
    }

    private void clearFields() {
        patientCombo.setSelectedIndex(-1);
        dateRdvChooser.setDate(null);
        heureRdvField.setText("");
        statutComboBox.setSelectedIndex(0);
        selectedRendezvousId = -1;
    }
}