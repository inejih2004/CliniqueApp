package views;

import dao.RendezvousDAO;
import modeles.Rendezvous;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RendezvousFrame extends JFrame {
    private RendezvousDAO rendezvousDAO;
    private DefaultTableModel tableModel;
    private JTable rendezvousTable;
    private JTextField patientIdField, medecinIdField, heureRdvField;
    private JDateChooser dateRdvChooser;
    private JComboBox<String> statutComboBox;
    private int selectedRendezvousId = -1;

    public RendezvousFrame() {
        rendezvousDAO = new RendezvousDAO();
        initUI();
        refreshTable();
    }

    private void initUI() {
        setTitle("Rendezvous Management");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        formPanel.add(new JLabel("Patient ID:"));
        patientIdField = new JTextField();
        formPanel.add(patientIdField);

        formPanel.add(new JLabel("Médecin ID:"));
        medecinIdField = new JTextField();
        formPanel.add(medecinIdField);

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
        JButton addButton = new JButton("Ajouter");
        JButton editButton = new JButton("Modifier");
        JButton deleteButton = new JButton("Supprimer");
        JButton refreshButton = new JButton("Rafraîchir");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        // Table
        String[] columns = {"ID", "Patient ID", "Médecin ID", "Date RDV", "Heure RDV", "Statut"};
        tableModel = new DefaultTableModel(columns, 0);
        rendezvousTable = new JTable(tableModel);
        rendezvousTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        rendezvousTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = rendezvousTable.getSelectedRow();
            if (selectedRow >= 0) {
                selectedRendezvousId = (int) tableModel.getValueAt(selectedRow, 0);
                patientIdField.setText(String.valueOf(tableModel.getValueAt(selectedRow, 1)));
                medecinIdField.setText(String.valueOf(tableModel.getValueAt(selectedRow, 2)));
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

        // Add components to frame
        add(formPanel, BorderLayout.NORTH);
        add(new JScrollPane(rendezvousTable), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Button Actions
        addButton.addActionListener(e -> addRendezvous());
        editButton.addActionListener(e -> editRendezvous());
        deleteButton.addActionListener(e -> deleteRendezvous());
        refreshButton.addActionListener(e -> refreshTable());
    }

    private void addRendezvous() {
        if (!validateInputs()) return;

        Rendezvous rdv = new Rendezvous();
        rdv.setPatientId(Integer.parseInt(patientIdField.getText()));
        rdv.setMedecinId(Integer.parseInt(medecinIdField.getText()));
        rdv.setDateRdv(dateRdvChooser.getDate());
        rdv.setHeureRdv(heureRdvField.getText());
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
        rdv.setPatientId(Integer.parseInt(patientIdField.getText()));
        rdv.setMedecinId(Integer.parseInt(medecinIdField.getText()));
        rdv.setDateRdv(dateRdvChooser.getDate());
        rdv.setHeureRdv(heureRdvField.getText());
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
        List<Rendezvous> rendezVousList = rendezvousDAO.getAllRendezvous();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (Rendezvous rdv : rendezVousList) {
            tableModel.addRow(new Object[]{
                rdv.getId(),
                rdv.getPatientId(),
                rdv.getMedecinId(),
                sdf.format(rdv.getDateRdv()),
                rdv.getHeureRdv(),
                rdv.getStatut()
            });
        }
        selectedRendezvousId = -1;
    }

    private boolean validateInputs() {
        try {
            Integer.parseInt(patientIdField.getText());
            Integer.parseInt(medecinIdField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Les champs Patient ID et Médecin ID doivent être des nombres entiers.", 
                                         "Erreur", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (patientIdField.getText().trim().isEmpty() ||
            medecinIdField.getText().trim().isEmpty() ||
            dateRdvChooser.getDate() == null ||
            heureRdvField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.", 
                                         "Erreur", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validate heure_rdv format (HH:mm:ss)
        if (!heureRdvField.getText().matches("^([0-1]?[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]$")) {
            JOptionPane.showMessageDialog(this, "L'heure du RDV doit être au format HH:mm:ss (ex: 14:30:00).", 
                                         "Erreur", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private void clearFields() {
        patientIdField.setText("");
        medecinIdField.setText("");
        dateRdvChooser.setDate(null);
        heureRdvField.setText("");
        statutComboBox.setSelectedIndex(0);
        selectedRendezvousId = -1;
    }
}