package com.healthcare.view;

import com.healthcare.controller.HealthcareController;
import com.healthcare.model.Patient;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

/**
 * Panel for Patient management (CRUD operations)
 * Standardized to match the professional Dashboard theme.
 */
public class PatientPanel extends JPanel {
    private HealthcareController controller;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField patientIDField, firstNameField, lastNameField, dobField, genderField, nhsNumberField, gpSurgeryField;

    public PatientPanel(HealthcareController controller) {
        this.controller = controller;
        initializePanel();
        refreshData();
    }

    private void initializePanel() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(new Color(245, 247, 250)); // Slate Background

        // Table Setup
        String[] columns = {"ID", "First Name", "Last Name", "DOB", "Gender", "NHS No", "GP Surgery"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        table = new JTable(tableModel);
        table.setRowHeight(28);
        table.setSelectionBackground(new Color(232, 240, 254));

        // Professional Navy Header
        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(40, 53, 147));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));

        // FIX: Defined the selection listener
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) loadSelectedPatient();
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Patient Records"));
        add(scrollPane, BorderLayout.CENTER);

        // Form and Buttons
        add(createFormPanel(), BorderLayout.SOUTH);
        add(createButtonPanel(), BorderLayout.NORTH);
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.setPreferredSize(new Dimension(0, 180));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("Patient ID:"), gbc);
        gbc.gridx = 1; panel.add(patientIDField = new JTextField(12), gbc);
        gbc.gridx = 2; panel.add(new JLabel("First Name:"), gbc);
        gbc.gridx = 3; panel.add(firstNameField = new JTextField(12), gbc);

        gbc.gridy = 1; gbc.gridx = 0; panel.add(new JLabel("Last Name:"), gbc);
        gbc.gridx = 1; panel.add(lastNameField = new JTextField(12), gbc);
        gbc.gridx = 2; panel.add(new JLabel("NHS Number:"), gbc);
        gbc.gridx = 3; panel.add(nhsNumberField = new JTextField(12), gbc);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        panel.setOpaque(false);

        JButton addBtn = styleButton(new JButton("Add Patient"), new Color(33, 150, 243));
        addBtn.addActionListener(e -> addPatient()); // FIX: Defined action

        JButton deleteBtn = styleButton(new JButton("Delete"), new Color(211, 47, 47));
        deleteBtn.addActionListener(e -> deletePatient()); // FIX: Defined action

        panel.add(addBtn);
        panel.add(deleteBtn);
        return panel;
    }

    private JButton styleButton(JButton btn, Color bg) {
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return btn;
    }

    // --- FIX: ADDED MISSING METHODS TO RESOLVE ECLIPSE ERRORS ---

    private void loadSelectedPatient() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            patientIDField.setText((String) tableModel.getValueAt(row, 0));
            firstNameField.setText((String) tableModel.getValueAt(row, 1));
            lastNameField.setText((String) tableModel.getValueAt(row, 2));
            nhsNumberField.setText((String) tableModel.getValueAt(row, 5));
        }
    }

    private void addPatient() {
        Patient p = new Patient();
        p.setPatientID(patientIDField.getText());
        p.setFirstName(firstNameField.getText());
        p.setLastName(lastNameField.getText());
        p.setNhsNumber(nhsNumberField.getText());
        controller.addPatient(p);
        refreshData();
    }

    private void deletePatient() {
        String id = patientIDField.getText();
        if (!id.isEmpty()) {
            controller.deletePatient(id);
            refreshData();
        }
    }

    public void refreshData() {
        tableModel.setRowCount(0);
        List<Patient> patients = controller.getAllPatients();
        for (Patient p : patients) {
            tableModel.addRow(new Object[]{
                p.getPatientID(), p.getFirstName(), p.getLastName(), 
                p.getDateOfBirth(), p.getGender(), p.getNhsNumber(), p.getGpSurgery()
            });
        }
    }
}