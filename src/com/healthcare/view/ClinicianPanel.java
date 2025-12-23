package com.healthcare.view;

import com.healthcare.controller.HealthcareController;
import com.healthcare.model.Clinician;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;


public class ClinicianPanel extends JPanel {
    private HealthcareController controller;
    private JTable table;
    private DefaultTableModel tableModel;
    
    // Original fields preserved
    private JTextField clinicianIDField, firstNameField, lastNameField, qualificationField;
    private JTextField specialtyField, gmcNumberField, workplaceField, workplaceTypeField;
    private JTextField employmentStatusField, startDateField, emailField, phoneField;

    // Standard Navy Palette
    private final Color NAVY_PRIMARY = new Color(40, 53, 147);
    private final Color SLATE_BG = new Color(245, 247, 250);

    public ClinicianPanel(HealthcareController controller) {
        this.controller = controller;
        initializePanel();
        refreshData();
    }

    private void initializePanel() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(SLATE_BG);

        
        add(createButtonPanel(), BorderLayout.NORTH);

        
        String[] columns = {
            "Clinician ID", "First Name", "Last Name", "Qualification", 
            "Specialty", "GMC Number", "Workplace", "Type", 
            "Status", "Start Date", "Email", "Phone"
        };
        
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        table = new JTable(tableModel);
        table.setRowHeight(28);
        table.setSelectionBackground(new Color(232, 240, 254));
        table.setShowGrid(true);
        table.setGridColor(new Color(230, 230, 230));

       
        JTableHeader header = table.getTableHeader();
        header.setBackground(NAVY_PRIMARY);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) loadSelectedClinician();
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Medical Staff Registry (Clinicians)"));
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);

        
        add(createFormPanel(), BorderLayout.SOUTH);
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Clinician Profile Details"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 10, 6, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Column 1
        gbc.gridy = 0; gbc.gridx = 0; panel.add(new JLabel("Clinician ID:"), gbc);
        gbc.gridx = 1; panel.add(clinicianIDField = new JTextField(12), gbc);
        gbc.gridy = 1; gbc.gridx = 0; panel.add(new JLabel("First Name:"), gbc);
        gbc.gridx = 1; panel.add(firstNameField = new JTextField(12), gbc);
        gbc.gridy = 2; gbc.gridx = 0; panel.add(new JLabel("Last Name:"), gbc);
        gbc.gridx = 1; panel.add(lastNameField = new JTextField(12), gbc);
        gbc.gridy = 3; gbc.gridx = 0; panel.add(new JLabel("Qualification:"), gbc);
        gbc.gridx = 1; panel.add(qualificationField = new JTextField(12), gbc);

        // Column 2
        gbc.gridy = 0; gbc.gridx = 2; panel.add(new JLabel("Specialty:"), gbc);
        gbc.gridx = 3; panel.add(specialtyField = new JTextField(12), gbc);
        gbc.gridy = 1; gbc.gridx = 2; panel.add(new JLabel("GMC Number:"), gbc);
        gbc.gridx = 3; panel.add(gmcNumberField = new JTextField(12), gbc);
        gbc.gridy = 2; gbc.gridx = 2; panel.add(new JLabel("Workplace:"), gbc);
        gbc.gridx = 3; panel.add(workplaceField = new JTextField(12), gbc);
        gbc.gridy = 3; gbc.gridx = 2; panel.add(new JLabel("Workplace Type:"), gbc);
        gbc.gridx = 3; panel.add(workplaceTypeField = new JTextField(12), gbc);

        // Column 3
        gbc.gridy = 0; gbc.gridx = 4; panel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 5; panel.add(employmentStatusField = new JTextField(12), gbc);
        gbc.gridy = 1; gbc.gridx = 4; panel.add(new JLabel("Start Date:"), gbc);
        gbc.gridx = 5; panel.add(startDateField = new JTextField(12), gbc);
        gbc.gridy = 2; gbc.gridx = 4; panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 5; panel.add(emailField = new JTextField(12), gbc);
        gbc.gridy = 3; gbc.gridx = 4; panel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 5; panel.add(phoneField = new JTextField(12), gbc);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        panel.setOpaque(false);

        JButton addBtn = styleButton(new JButton("Register Clinician"), new Color(33, 150, 243));
        addBtn.addActionListener(e -> addClinician());

        JButton updateBtn = styleButton(new JButton("Update Profile"), new Color(76, 175, 80));
        updateBtn.addActionListener(e -> updateClinician());

        JButton deleteBtn = styleButton(new JButton("Delete Record"), new Color(211, 47, 47));
        deleteBtn.addActionListener(e -> deleteClinician());

        JButton clearBtn = styleButton(new JButton("Clear Form"), new Color(158, 158, 158));
        clearBtn.addActionListener(e -> clearForm());

        JButton refreshBtn = styleButton(new JButton("Refresh List"), NAVY_PRIMARY);
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
        btn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        return btn;
    }

    

    private void loadSelectedClinician() {
        int r = table.getSelectedRow();
        if (r >= 0) {
            clinicianIDField.setText((String) tableModel.getValueAt(r, 0));
            firstNameField.setText((String) tableModel.getValueAt(r, 1));
            lastNameField.setText((String) tableModel.getValueAt(r, 2));
            qualificationField.setText((String) tableModel.getValueAt(r, 3));
            specialtyField.setText((String) tableModel.getValueAt(r, 4));
            gmcNumberField.setText((String) tableModel.getValueAt(r, 5));
            workplaceField.setText((String) tableModel.getValueAt(r, 6));
            workplaceTypeField.setText((String) tableModel.getValueAt(r, 7));
            employmentStatusField.setText((String) tableModel.getValueAt(r, 8));
            startDateField.setText((String) tableModel.getValueAt(r, 9));
            emailField.setText((String) tableModel.getValueAt(r, 10));
            phoneField.setText((String) tableModel.getValueAt(r, 11));
        }
    }

    private void addClinician() {
        Clinician c = createClinicianFromForm();
        if (c != null) {
            controller.addClinician(c);
            refreshData();
            clearForm();
            JOptionPane.showMessageDialog(this, "Clinician Added.");
        }
    }

    private void updateClinician() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            String id = (String) tableModel.getValueAt(row, 0);
            controller.deleteClinician(id);
            controller.addClinician(createClinicianFromForm());
            refreshData();
            JOptionPane.showMessageDialog(this, "Clinician Updated.");
        }
    }

    private void deleteClinician() {
        int row = table.getSelectedRow();
        if (row >= 0 && JOptionPane.showConfirmDialog(this, "Delete Clinician?") == 0) {
            controller.deleteClinician((String) tableModel.getValueAt(row, 0));
            refreshData();
            clearForm();
        }
    }

    private Clinician createClinicianFromForm() {
        if (clinicianIDField.getText().isEmpty()) return null;
        return new Clinician(
            clinicianIDField.getText(), firstNameField.getText(), lastNameField.getText(),
            qualificationField.getText(), specialtyField.getText(), gmcNumberField.getText(),
            workplaceField.getText(), workplaceTypeField.getText(), employmentStatusField.getText(),
            startDateField.getText(), emailField.getText(), phoneField.getText()
        );
    }

    private void clearForm() {
        JTextField[] fields = {
            clinicianIDField, firstNameField, lastNameField, qualificationField,
            specialtyField, gmcNumberField, workplaceField, workplaceTypeField,
            employmentStatusField, startDateField, emailField, phoneField
        };
        for (JTextField f : fields) f.setText("");
    }

    public void refreshData() {
        tableModel.setRowCount(0);
        for (Clinician c : controller.getAllClinicians()) {
            tableModel.addRow(new Object[]{
                c.getClinicianID(), c.getFirstName(), c.getLastName(), c.getQualification(),
                c.getSpecialty(), c.getGmcNumber(), c.getWorkplace(), c.getWorkplaceType(),
                c.getEmploymentStatus(), c.getStartDate(), c.getEmail(), c.getPhone()
            });
        }
    }
}