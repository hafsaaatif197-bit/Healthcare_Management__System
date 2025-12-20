package com.healthcare.view;

import com.healthcare.controller.HealthcareController;
import com.healthcare.model.Clinician;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

/**
 * Professional Clinician Management Panel
 * Standardized to match the Patient Dashboard theme.
 */
public class ClinicianPanel extends JPanel {
    private HealthcareController controller;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField clinicianIDField, firstNameField, lastNameField, qualificationField;
    private JTextField specialtyField, gmcNumberField, workplaceField, workplaceTypeField;
    private JTextField employmentStatusField, startDateField, emailField, phoneField;

    public ClinicianPanel(HealthcareController controller) {
        this.controller = controller;
        initializePanel();
        refreshData();
    }

    private void initializePanel() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(new Color(245, 247, 250)); // Professional Slate Gray

        // 1. TOP BUTTON PANEL (Standardized Actions)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        buttonPanel.setOpaque(false);

        JButton addBtn = styleButton(new JButton("Register Clinician"), new Color(33, 150, 243));
        addBtn.addActionListener(e -> addClinician());

        JButton updateBtn = styleButton(new JButton("Update Record"), new Color(76, 175, 80));
        updateBtn.addActionListener(e -> updateClinician());

        JButton deleteBtn = styleButton(new JButton("Delete"), new Color(211, 47, 47));
        deleteBtn.addActionListener(e -> deleteClinician());

        JButton clearBtn = styleButton(new JButton("Clear Form"), new Color(158, 158, 158));
        clearBtn.addActionListener(e -> clearForm());

        buttonPanel.add(addBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(clearBtn);
        add(buttonPanel, BorderLayout.NORTH);

        // 2. CENTER TABLE (Navy Header Style)
        String[] columns = {"ID", "First Name", "Last Name", "Specialty", "GMC No", "Workplace", "Email", "Phone"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        
        table = new JTable(tableModel);
        table.setRowHeight(28);
        table.setSelectionBackground(new Color(232, 240, 254));

        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(40, 53, 147)); // Professional Navy
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) loadSelectedClinician();
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Registered Clinicians"));
        add(scrollPane, BorderLayout.CENTER);

        // 3. BOTTOM FORM PANEL (Organized 4-column Grid)
        add(createFormPanel(), BorderLayout.SOUTH);
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Row 1
        gbc.gridy = 0; gbc.gridx = 0; panel.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1; panel.add(clinicianIDField = new JTextField(10), gbc);
        gbc.gridx = 2; panel.add(new JLabel("First Name:"), gbc);
        gbc.gridx = 3; panel.add(firstNameField = new JTextField(10), gbc);
        gbc.gridx = 4; panel.add(new JLabel("Last Name:"), gbc);
        gbc.gridx = 5; panel.add(lastNameField = new JTextField(10), gbc);

        // Row 2
        gbc.gridy = 1; gbc.gridx = 0; panel.add(new JLabel("Qualification:"), gbc);
        gbc.gridx = 1; panel.add(qualificationField = new JTextField(10), gbc);
        gbc.gridx = 2; panel.add(new JLabel("Specialty:"), gbc);
        gbc.gridx = 3; panel.add(specialtyField = new JTextField(10), gbc);
        gbc.gridx = 4; panel.add(new JLabel("GMC Number:"), gbc);
        gbc.gridx = 5; panel.add(gmcNumberField = new JTextField(10), gbc);

        // Row 3
        gbc.gridy = 2; gbc.gridx = 0; panel.add(new JLabel("Workplace:"), gbc);
        gbc.gridx = 1; panel.add(workplaceField = new JTextField(10), gbc);
        gbc.gridx = 2; panel.add(new JLabel("Workplace Type:"), gbc);
        gbc.gridx = 3; panel.add(workplaceTypeField = new JTextField(10), gbc);
        gbc.gridx = 4; panel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 5; panel.add(employmentStatusField = new JTextField(10), gbc);

        // Row 4
        gbc.gridy = 3; gbc.gridx = 0; panel.add(new JLabel("Start Date:"), gbc);
        gbc.gridx = 1; panel.add(startDateField = new JTextField(10), gbc);
        gbc.gridx = 2; panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 3; panel.add(emailField = new JTextField(10), gbc);
        gbc.gridx = 4; panel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 5; panel.add(phoneField = new JTextField(10), gbc);

        return panel;
    }

    private JButton styleButton(JButton btn, Color bg) {
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return btn;
    }

    // --- LOGIC METHODS ---

    private void addClinician() {
        Clinician c = createClinicianFromForm();
        if (c != null) {
            controller.addClinician(c);
            refreshData();
            clearForm();
            JOptionPane.showMessageDialog(this, "Clinician registered.");
        }
    }

    private void updateClinician() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        String id = (String) tableModel.getValueAt(row, 0);
        controller.deleteClinician(id);
        controller.addClinician(createClinicianFromForm());
        refreshData();
        JOptionPane.showMessageDialog(this, "Record updated.");
    }

    private void deleteClinician() {
        int row = table.getSelectedRow();
        if (row >= 0 && JOptionPane.showConfirmDialog(this, "Delete this record?") == 0) {
            controller.deleteClinician((String) tableModel.getValueAt(row, 0));
            refreshData();
            clearForm();
        }
    }

    private void loadSelectedClinician() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            clinicianIDField.setText((String) tableModel.getValueAt(row, 0));
            firstNameField.setText((String) tableModel.getValueAt(row, 1));
            lastNameField.setText((String) tableModel.getValueAt(row, 2));
            specialtyField.setText((String) tableModel.getValueAt(row, 3));
            gmcNumberField.setText((String) tableModel.getValueAt(row, 4));
            workplaceField.setText((String) tableModel.getValueAt(row, 5));
            emailField.setText((String) tableModel.getValueAt(row, 6));
            phoneField.setText((String) tableModel.getValueAt(row, 7));
        }
    }

    private Clinician createClinicianFromForm() {
        if (clinicianIDField.getText().isEmpty()) return null;
        return new Clinician(clinicianIDField.getText(), firstNameField.getText(), 
            lastNameField.getText(), qualificationField.getText(), specialtyField.getText(), 
            gmcNumberField.getText(), workplaceField.getText(), workplaceTypeField.getText(), 
            employmentStatusField.getText(), startDateField.getText(), emailField.getText(), phoneField.getText());
    }

    private void clearForm() {
        JTextField[] fields = {clinicianIDField, firstNameField, lastNameField, qualificationField, 
            specialtyField, gmcNumberField, workplaceField, workplaceTypeField, 
            employmentStatusField, startDateField, emailField, phoneField};
        for (JTextField f : fields) f.setText("");
    }

    public void refreshData() {
        tableModel.setRowCount(0);
        for (Clinician c : controller.getAllClinicians()) {
            tableModel.addRow(new Object[]{c.getClinicianID(), c.getFirstName(), c.getLastName(), 
                c.getSpecialty(), c.getGmcNumber(), c.getWorkplace(), c.getEmail(), c.getPhone()});
        }
    }
}