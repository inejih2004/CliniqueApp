package views;

import dao.PatientDAO;
import dao.SpecialiteDAO;
import modeles.Patient;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.JFrame;
import com.toedter.calendar.JDateChooser;

public class PatientFrame extends JFrame {
    private PatientDAO patientDAO;
    private DefaultTableModel tableModel;
    private JTable patientTable;
    private JTextField nomField, prenomField, telephoneField, adresseField;
    private JDateChooser dateChooser;
    private JComboBox<String> specialiteCombo;
    private int selectedPatientId = -1;

    public PatientFrame() {
        patientDAO = new PatientDAO();
        initUI();
        refreshTable();
    }

    private void initUI() {
        setTitle("Patient Management");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        formPanel.add(new JLabel("Nom:"));
        nomField = new JTextField();
        formPanel.add(nomField);

        formPanel.add(new JLabel("Prénom:"));
        prenomField = new JTextField();
        formPanel.add(prenomField);

        formPanel.add(new JLabel("Date de Naissance:"));
        dateChooser = new JDateChooser();
        formPanel.add(dateChooser);

        formPanel.add(new JLabel("Téléphone:"));
        telephoneField = new JTextField();
        formPanel.add(telephoneField);

        formPanel.add(new JLabel("Adresse:"));
        adresseField = new JTextField();
        formPanel.add(adresseField);

        formPanel.add(new JLabel("Spécialité demandée:"));
        specialiteCombo = new JComboBox<>();
        formPanel.add(specialiteCombo);

        loadSpecialites();

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
        String[] columns = {"ID", "Nom", "Prénom", "Date de Naissance", "Téléphone", "Adresse", "Spécialité"};
        tableModel = new DefaultTableModel(columns, 0);
        patientTable = new JTable(tableModel);
        patientTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        patientTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = patientTable.getSelectedRow();
            if (selectedRow >= 0) {
                selectedPatientId = (int) tableModel.getValueAt(selectedRow, 0);
                nomField.setText((String) tableModel.getValueAt(selectedRow, 1));
                prenomField.setText((String) tableModel.getValueAt(selectedRow, 2));
                try {
                    String dateStr = (String) tableModel.getValueAt(selectedRow, 3);
                    Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
                    dateChooser.setDate(date);
                } catch (Exception ex) {
                    dateChooser.setDate(null);
                }
                telephoneField.setText((String) tableModel.getValueAt(selectedRow, 4));
                adresseField.setText((String) tableModel.getValueAt(selectedRow, 5));
                String spec = (String) tableModel.getValueAt(selectedRow, 6);
                specialiteCombo.setSelectedItem(spec);
            }
        });

        // Add components to frame
        add(formPanel, BorderLayout.NORTH);
        add(new JScrollPane(patientTable), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Button Actions
        addButton.addActionListener(e -> addPatient());
        editButton.addActionListener(e -> editPatient());
        deleteButton.addActionListener(e -> deletePatient());
        refreshButton.addActionListener(e -> refreshTable());
    }

    private void loadSpecialites() {
        try {
        	SpecialiteDAO specialiteDAO = new SpecialiteDAO();
            List<String> specialites = specialiteDAO.getAllSpecialites();
            specialiteCombo.removeAllItems();
            for (String s : specialites) {
                specialiteCombo.addItem(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // fallback specialties
            specialiteCombo.addItem("Cardiologie");
            specialiteCombo.addItem("Dermatologie");
            specialiteCombo.addItem("Généraliste");
        }
    }

    private void addPatient() {
        if (!validateInputs()) return;

        Patient p = new Patient();
        p.setNom(nomField.getText());
        p.setPrenom(prenomField.getText());
        p.setDateNaissance(dateChooser.getDate());
        p.setTelephone(telephoneField.getText());
        p.setAdresse(adresseField.getText());
        p.setSpecialite((String) specialiteCombo.getSelectedItem());

        patientDAO.insertPatient(p);
        JOptionPane.showMessageDialog(this, "Patient ajouté avec succès!");
        refreshTable();
        clearFields();
    }

    private void editPatient() {
        if (selectedPatientId == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un patient à modifier.");
            return;
        }
        if (!validateInputs()) return;

        Patient p = new Patient();
        p.setId(selectedPatientId);
        p.setNom(nomField.getText());
        p.setPrenom(prenomField.getText());
        p.setDateNaissance(dateChooser.getDate());
        p.setTelephone(telephoneField.getText());
        p.setAdresse(adresseField.getText());
        p.setSpecialite((String) specialiteCombo.getSelectedItem());

        patientDAO.updatePatient(p);
        JOptionPane.showMessageDialog(this, "Patient modifié avec succès!");
        refreshTable();
        clearFields();
    }

    private void deletePatient() {
        if (selectedPatientId == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un patient à supprimer.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Êtes-vous sûr de vouloir supprimer ce patient?",
                "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            patientDAO.deletePatient(selectedPatientId);
            JOptionPane.showMessageDialog(this, "Patient supprimé avec succès!");
            refreshTable();
            clearFields();
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        List<Patient> patients = patientDAO.getAllPatients();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (Patient p : patients) {
            tableModel.addRow(new Object[]{
                    p.getId(),
                    p.getNom(),
                    p.getPrenom(),
                    sdf.format(p.getDateNaissance()),
                    p.getTelephone(),
                    p.getAdresse(),
                    p.getSpecialite()
            });
        }
        selectedPatientId = -1;
    }

    private boolean validateInputs() {
        if (nomField.getText().trim().isEmpty() ||
                prenomField.getText().trim().isEmpty() ||
                dateChooser.getDate() == null ||
                telephoneField.getText().trim().isEmpty() ||
                adresseField.getText().trim().isEmpty() ||
                specialiteCombo.getSelectedItem() == null ||
                ((String) specialiteCombo.getSelectedItem()).trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void clearFields() {
        nomField.setText("");
        prenomField.setText("");
        dateChooser.setDate(null);
        telephoneField.setText("");
        adresseField.setText("");
        specialiteCombo.setSelectedIndex(0);
        selectedPatientId = -1;
    }
}
