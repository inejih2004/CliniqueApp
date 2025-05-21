package views;

import dao.PatientDAO;
import dao.SpecialiteDAO;
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
import java.util.regex.Pattern;

public class PatientFrame extends JFrame {
    private PatientDAO patientDAO;
    private dashbordSecréteriaView secreteriaView;
    private DefaultTableModel tableModel;
    private JTable patientTable;
    private JTextField nomField, prenomField, telephoneField, adresseField;
    private JDateChooser dateChooser;
    private JComboBox<String> specialiteCombo;
    private int selectedPatientId = -1;

    public PatientFrame(dashbordSecréteriaView secreteriaView) {
        this.secreteriaView = secreteriaView;
        patientDAO = new PatientDAO();
        initUI();
        refreshTable();
    }

    private void initUI() {
        setTitle("Patient Management");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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

        add(formPanel, BorderLayout.NORTH);
        add(new JScrollPane(patientTable), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> addPatient());
        editButton.addActionListener(e -> editPatient());
        deleteButton.addActionListener(e -> deletePatient());
        refreshButton.addActionListener(e -> refreshTable());
        previousButton.addActionListener(e -> {
            setVisible(false);
            secreteriaView.setVisible(true);
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
            specialiteCombo.addItem("Cardiologie");
            specialiteCombo.addItem("Dermatologie");
            specialiteCombo.addItem("Généraliste");
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des spécialités. Utilisation des valeurs par défaut.",
                                         "Avertissement", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void addPatient() {
        if (!validateInputs()) return;

        Patient p = new Patient();
        p.setNom(nomField.getText().trim());
        p.setPrenom(prenomField.getText().trim());
        p.setDateNaissance(dateChooser.getDate());
        p.setTelephone(telephoneField.getText().trim());
        p.setAdresse(adresseField.getText().trim());
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
        p.setNom(nomField.getText().trim());
        p.setPrenom(prenomField.getText().trim());
        p.setDateNaissance(dateChooser.getDate());
        p.setTelephone(telephoneField.getText().trim());
        p.setAdresse(adresseField.getText().trim());
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
        try {
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
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de la mise à jour de la table: " + e.getMessage(),
                                         "Erreur", JOptionPane.ERROR_MESSAGE);
        }
        selectedPatientId = -1;
    }

    private boolean validateInputs() {
        // Nom: Letters, spaces, hyphens, apostrophes
        String nom = nomField.getText().trim();
        if (nom.isEmpty() || !Pattern.matches("^[a-zA-Z\\s\\-']+$", nom)) {
            JOptionPane.showMessageDialog(this, "Le nom doit contenir uniquement des lettres, espaces, tirets ou apostrophes.",
                                         "Erreur", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Prénom: Same as Nom
        String prenom = prenomField.getText().trim();
        if (prenom.isEmpty() || !Pattern.matches("^[a-zA-Z\\s\\-']+$", prenom)) {
            JOptionPane.showMessageDialog(this, "Le prénom doit contenir uniquement des lettres, espaces, tirets ou apostrophes.",
                                         "Erreur", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Date de Naissance: Not null, not in future
        Date dateNaissance = dateChooser.getDate();
        if (dateNaissance == null) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une date de naissance.",
                                         "Erreur", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (dateNaissance.after(new Date())) {
            JOptionPane.showMessageDialog(this, "La date de naissance ne peut pas être dans le futur.",
                                         "Erreur", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Téléphone: Mauritanian format (+222 followed by 8 digits)
        String telephone = telephoneField.getText().trim();
        if (!Pattern.matches("^\\+222[0-9]{8}$", telephone)) {
            JOptionPane.showMessageDialog(this, "Le numéro de téléphone doit être au format +222 suivi de 8 chiffres (ex: +22212345678).",
                                         "Erreur", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Adresse: Non-empty, alphanumeric with spaces and punctuation
        String adresse = adresseField.getText().trim();
        if (adresse.isEmpty() || !Pattern.matches("^[a-zA-Z0-9\\s,\\-\\.']+$", adresse)) {
            JOptionPane.showMessageDialog(this, "L'adresse doit contenir des lettres, chiffres, espaces, virgules, points ou tirets.",
                                         "Erreur", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Spécialité: Non-empty
        if (specialiteCombo.getSelectedItem() == null || ((String) specialiteCombo.getSelectedItem()).trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une spécialité.",
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