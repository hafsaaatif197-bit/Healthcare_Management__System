package com.healthcare.view;

import com.healthcare.controller.HealthcareController;
import com.healthcare.model.Patient;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class PatientPanel extends JPanel {
    private HealthcareController controller;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField patientIDField, firstNameField, lastNameField, dobField, genderField;
    private JTextField nhsNumberField, emailField, phoneField, addressField, postcodeField;
    private JTextField emergencyContactNameField, emergencyContactPhoneField, registrationDateField, gpSurgeryField;

    // Standardized Navy Colors
    private final Color NAVY_PRIMARY = new Color(40, 53, 147);
    private final Color SLATE_BG = new Color(245, 247, 250);

    public PatientPanel(HealthcareController controller) {
        this.controller = controller;
        initializePanel();
        refreshData();
    }

    private void initializePanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(SLATE_BG);

        // 1. FULL COLUMNS RESTORED
        String[] columns = {"Patient ID", "First Name", "Last Name", "DOB", "Gender",
                          "NHS Number", "Email", "Phone", "Address", "Postcode",
                          "Emergency Contact", "Emergency Phone", "Registration Date", "GP Surgery"};
        
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setRowHeight(26);
        table.setShowGrid(true);
        table.setGridColor(new Color(230, 230, 230));
        table.setSelectionBackground(new Color(227, 242, 253));
        
        // 2. NAVY TABLE HEADER
        JTableHeader header = table.getTableHeader();
        header.setReorderingAllowed(false);
        header.setBackground(NAVY_PRIMARY);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) loadSelectedPatient();
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Patient Registry"));
        add(scrollPane, BorderLayout.CENTER);

        // 3. FULL FORM RESTORED (Layout exactly as original)
        add(createFormPanel(), BorderLayout.SOUTH);

        // 4. TOP ACTION BAR
        add(createButtonPanel(), BorderLayout.NORTH);
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Patient Details"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Row 0
        gbc.gridy = 0; gbc.gridx = 0; panel.add(new JLabel("Patient ID:"), gbc);
        gbc.gridx = 1; panel.add(patientIDField = new JTextField(15), gbc);
        gbc.gridx = 2; panel.add(new JLabel("First Name:"), gbc);
        gbc.gridx = 3; panel.add(firstNameField = new JTextField(15), gbc);
        gbc.gridx = 4; panel.add(new JLabel("Last Name:"), gbc);
        gbc.gridx = 5; panel.add(lastNameField = new JTextField(15), gbc);

        // Row 1
        gbc.gridy = 1; gbc.gridx = 0; panel.add(new JLabel("DOB:"), gbc);
        gbc.gridx = 1; panel.add(dobField = new JTextField(15), gbc);
        gbc.gridx = 2; panel.add(new JLabel("Gender:"), gbc);
        gbc.gridx = 3; panel.add(genderField = new JTextField(15), gbc);
        gbc.gridx = 4; panel.add(new JLabel("NHS Number:"), gbc);
        gbc.gridx = 5; panel.add(nhsNumberField = new JTextField(15), gbc);

        // Row 2
        gbc.gridy = 2; gbc.gridx = 0; panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; panel.add(emailField = new JTextField(15), gbc);
        gbc.gridx = 2; panel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 3; panel.add(phoneField = new JTextField(15), gbc);
        gbc.gridx = 4; panel.add(new JLabel("Address:"), gbc);
        gbc.gridx = 5; panel.add(addressField = new JTextField(15), gbc);

        // Row 3
        gbc.gridy = 3; gbc.gridx = 0; panel.add(new JLabel("Postcode:"), gbc);
        gbc.gridx = 1; panel.add(postcodeField = new JTextField(15), gbc);
        gbc.gridx = 2; panel.add(new JLabel("Emergency Contact:"), gbc);
        gbc.gridx = 3; panel.add(emergencyContactNameField = new JTextField(15), gbc);
        gbc.gridx = 4; panel.add(new JLabel("Emergency Phone:"), gbc);
        gbc.gridx = 5; panel.add(emergencyContactPhoneField = new JTextField(15), gbc);

        // Row 4
        gbc.gridy = 4; gbc.gridx = 0; panel.add(new JLabel("Registration Date:"), gbc);
        gbc.gridx = 1; panel.add(registrationDateField = new JTextField(15), gbc);
        gbc.gridx = 2; panel.add(new JLabel("GP Surgery:"), gbc);
        gbc.gridx = 3; panel.add(gpSurgeryField = new JTextField(15), gbc);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panel.setOpaque(false);

        JButton addBtn = styleButton(new JButton("Add"), new Color(33, 150, 243));
        JButton updateBtn = styleButton(new JButton("Update"), new Color(76, 175, 80));
        JButton deleteBtn = styleButton(new JButton("Delete"), new Color(211, 47, 47));
        JButton clearBtn = styleButton(new JButton("Clear"), new Color(158, 158, 158));
        JButton refreshBtn = styleButton(new JButton("Refresh"), NAVY_PRIMARY);

        addBtn.addActionListener(e -> addPatient());
        updateBtn.addActionListener(e -> updatePatient());
        deleteBtn.addActionListener(e -> deletePatient());
        clearBtn.addActionListener(e -> clearForm());
        refreshBtn.addActionListener(e -> refreshData());

        panel.add(addBtn); panel.add(updateBtn); panel.add(deleteBtn); panel.add(clearBtn); panel.add(refreshBtn);
        return panel;
    }

    private JButton styleButton(JButton btn, Color bg) {
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // --- RESTORED LOGIC ---

    private void loadSelectedPatient() {
        int r = table.getSelectedRow();
        if (r >= 0) {
            patientIDField.setText((String) tableModel.getValueAt(r, 0));
            firstNameField.setText((String) tableModel.getValueAt(r, 1));
            lastNameField.setText((String) tableModel.getValueAt(r, 2));
            dobField.setText((String) tableModel.getValueAt(r, 3));
            genderField.setText((String) tableModel.getValueAt(r, 4));
            nhsNumberField.setText((String) tableModel.getValueAt(r, 5));
            emailField.setText((String) tableModel.getValueAt(r, 6));
            phoneField.setText((String) tableModel.getValueAt(r, 7));
            addressField.setText((String) tableModel.getValueAt(r, 8));
            postcodeField.setText((String) tableModel.getValueAt(r, 9));
            emergencyContactNameField.setText((String) tableModel.getValueAt(r, 10));
            emergencyContactPhoneField.setText((String) tableModel.getValueAt(r, 11));
            registrationDateField.setText((String) tableModel.getValueAt(r, 12));
            gpSurgeryField.setText((String) tableModel.getValueAt(r, 13));
        }
    }

    private void addPatient() {
        Patient p = createPatientFromForm();
        if (p != null) {
            controller.addPatient(p);
            refreshData();
            clearForm();
        }
    }

    private void updatePatient() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            String id = (String) tableModel.getValueAt(row, 0);
            controller.deletePatient(id);
            controller.addPatient(createPatientFromForm());
            refreshData();
        }
    }

    private void deletePatient() {
        int row = table.getSelectedRow();
        if (row >= 0 && JOptionPane.showConfirmDialog(this, "Delete?") == 0) {
            controller.deletePatient((String) tableModel.getValueAt(row, 0));
            refreshData();
            clearForm();
        }
    }

    private Patient createPatientFromForm() {
        if (patientIDField.getText().isEmpty()) return null;
        return new Patient(patientIDField.getText(), firstNameField.getText(), lastNameField.getText(),
                dobField.getText(), genderField.getText(), nhsNumberField.getText(), emailField.getText(),
                phoneField.getText(), addressField.getText(), postcodeField.getText(),
                emergencyContactNameField.getText(), emergencyContactPhoneField.getText(),
                registrationDateField.getText(), gpSurgeryField.getText());
    }

    private void clearForm() {
        JTextField[] fields = {patientIDField, firstNameField, lastNameField, dobField, genderField,
                nhsNumberField, emailField, phoneField, addressField, postcodeField,
                emergencyContactNameField, emergencyContactPhoneField, registrationDateField, gpSurgeryField};
        for (JTextField f : fields) f.setText("");
    }

    public void refreshData() {
        tableModel.setRowCount(0);
        for (Patient p : controller.getAllPatients()) {
            tableModel.addRow(new Object[]{p.getPatientID(), p.getFirstName(), p.getLastName(),
                    p.getDateOfBirth(), p.getGender(), p.getNhsNumber(), p.getEmail(), p.getPhone(),
                    p.getAddress(), p.getPostcode(), p.getEmergencyContactName(),
                    p.getEmergencyContactPhone(), p.getRegistrationDate(), p.getGpSurgery()});
        }
    }
}