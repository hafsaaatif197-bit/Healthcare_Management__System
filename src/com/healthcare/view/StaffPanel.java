package com.healthcare.view;

import com.healthcare.controller.HealthcareController;
import com.healthcare.model.Staff;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;


public class StaffPanel extends JPanel {
    private HealthcareController controller;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField staffIDField, firstNameField, lastNameField, roleField, departmentField;
    private JTextField facilityIDField, emailField, phoneField, employmentStatusField;
    private JTextField startDateField, lineManagerField, accessLevelField;

    // Standard Dashboard Colors
    private final Color NAVY_PRIMARY = new Color(40, 53, 147);
    private final Color BACKGROUND_SLATE = new Color(245, 247, 250);

    public StaffPanel(HealthcareController controller) {
        this.controller = controller;
        initializePanel();
        refreshData(); // Load initial data
    }

    private void initializePanel() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(BACKGROUND_SLATE);

        
        JPanel buttonPanel = createButtonPanel();
        buttonPanel.setOpaque(false);
        add(buttonPanel, BorderLayout.NORTH);

        
        String[] columns = {"Staff ID", "First Name", "Last Name", "Role", "Department", "Facility ID", 
                          "Email", "Phone", "Status", "Start Date", "Manager", "Access"};
        
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        table = new JTable(tableModel);
        table.setRowHeight(28);
        table.setSelectionBackground(new Color(232, 240, 254));
        table.setSelectionForeground(Color.BLACK);
        table.setShowVerticalLines(false);

        
        JTableHeader header = table.getTableHeader();
        header.setBackground(NAVY_PRIMARY);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) loadSelectedStaff();
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Staff Directory"));
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);

        
        JPanel formPanel = createFormPanel();
        formPanel.setBorder(BorderFactory.createTitledBorder("Staff Detailed Records"));
        add(formPanel, BorderLayout.SOUTH);
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // COLUMN 1
        gbc.gridy = 0; gbc.gridx = 0; panel.add(new JLabel("Staff ID:"), gbc);
        gbc.gridx = 1; panel.add(staffIDField = new JTextField(12), gbc);
        gbc.gridy = 1; gbc.gridx = 0; panel.add(new JLabel("First Name:"), gbc);
        gbc.gridx = 1; panel.add(firstNameField = new JTextField(12), gbc);
        gbc.gridy = 2; gbc.gridx = 0; panel.add(new JLabel("Last Name:"), gbc);
        gbc.gridx = 1; panel.add(lastNameField = new JTextField(12), gbc);
        gbc.gridy = 3; gbc.gridx = 0; panel.add(new JLabel("Role:"), gbc);
        gbc.gridx = 1; panel.add(roleField = new JTextField(12), gbc);

        // COLUMN 2
        gbc.gridy = 0; gbc.gridx = 2; panel.add(new JLabel("Department:"), gbc);
        gbc.gridx = 3; panel.add(departmentField = new JTextField(12), gbc);
        gbc.gridy = 1; gbc.gridx = 2; panel.add(new JLabel("Facility ID:"), gbc);
        gbc.gridx = 3; panel.add(facilityIDField = new JTextField(12), gbc);
        gbc.gridy = 2; gbc.gridx = 2; panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 3; panel.add(emailField = new JTextField(12), gbc);
        gbc.gridy = 3; gbc.gridx = 2; panel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 3; panel.add(phoneField = new JTextField(12), gbc);

        // COLUMN 3
        gbc.gridy = 0; gbc.gridx = 4; panel.add(new JLabel("Employment Status:"), gbc);
        gbc.gridx = 5; panel.add(employmentStatusField = new JTextField(12), gbc);
        gbc.gridy = 1; gbc.gridx = 4; panel.add(new JLabel("Start Date:"), gbc);
        gbc.gridx = 5; panel.add(startDateField = new JTextField(12), gbc);
        gbc.gridy = 2; gbc.gridx = 4; panel.add(new JLabel("Line Manager:"), gbc);
        gbc.gridx = 5; panel.add(lineManagerField = new JTextField(12), gbc);
        gbc.gridy = 3; gbc.gridx = 4; panel.add(new JLabel("Access Level:"), gbc);
        gbc.gridx = 5; panel.add(accessLevelField = new JTextField(12), gbc);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        
        JButton addBtn = styleButton(new JButton("Register Staff"), new Color(33, 150, 243));
        addBtn.addActionListener(e -> addStaff());
        
        JButton updateBtn = styleButton(new JButton("Update Record"), new Color(76, 175, 80));
        updateBtn.addActionListener(e -> updateStaff());
        
        JButton deleteBtn = styleButton(new JButton("Remove Staff"), new Color(211, 47, 47));
        deleteBtn.addActionListener(e -> deleteStaff());
        
        JButton clearBtn = styleButton(new JButton("Clear Form"), new Color(158, 158, 158));
        clearBtn.addActionListener(e -> clearForm());

        panel.add(addBtn);
        panel.add(updateBtn);
        panel.add(deleteBtn);
        panel.add(clearBtn);
        
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

    // Logic Implementation
    private void addStaff() {
        Staff staff = createStaffFromForm();
        if (staff != null) {
            controller.addStaff(staff);
            refreshData();
            clearForm();
            JOptionPane.showMessageDialog(this, "Staff added successfully!");
        }
    }

    private void updateStaff() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            String id = (String) tableModel.getValueAt(row, 0);
            controller.deleteStaff(id); // Simple update via delete/re-add
            controller.addStaff(createStaffFromForm());
            refreshData();
            JOptionPane.showMessageDialog(this, "Staff record updated.");
        }
    }

    private void deleteStaff() {
        int row = table.getSelectedRow();
        if (row >= 0 && JOptionPane.showConfirmDialog(this, "Confirm deletion?") == JOptionPane.YES_OPTION) {
            controller.deleteStaff((String) tableModel.getValueAt(row, 0));
            refreshData();
            clearForm();
        }
    }

    private void loadSelectedStaff() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            staffIDField.setText((String) tableModel.getValueAt(row, 0));
            firstNameField.setText((String) tableModel.getValueAt(row, 1));
            lastNameField.setText((String) tableModel.getValueAt(row, 2));
            roleField.setText((String) tableModel.getValueAt(row, 3));
            departmentField.setText((String) tableModel.getValueAt(row, 4));
            facilityIDField.setText((String) tableModel.getValueAt(row, 5));
            emailField.setText((String) tableModel.getValueAt(row, 6));
            phoneField.setText((String) tableModel.getValueAt(row, 7));
            employmentStatusField.setText((String) tableModel.getValueAt(row, 8));
            startDateField.setText((String) tableModel.getValueAt(row, 9));
            lineManagerField.setText((String) tableModel.getValueAt(row, 10));
            accessLevelField.setText((String) tableModel.getValueAt(row, 11));
        }
    }

    private Staff createStaffFromForm() {
        if (staffIDField.getText().trim().isEmpty()) return null;
        return new Staff(staffIDField.getText(), firstNameField.getText(), lastNameField.getText(),
            roleField.getText(), departmentField.getText(), facilityIDField.getText(),
            emailField.getText(), phoneField.getText(), employmentStatusField.getText(),
            startDateField.getText(), lineManagerField.getText(), accessLevelField.getText());
    }

    private void clearForm() {
        JTextField[] fields = {staffIDField, firstNameField, lastNameField, roleField, departmentField,
            facilityIDField, emailField, phoneField, employmentStatusField, startDateField,
            lineManagerField, accessLevelField};
        for (JTextField f : fields) f.setText("");
    }

    public void refreshData() {
        tableModel.setRowCount(0);
        for (Staff s : controller.getAllStaff()) {
            tableModel.addRow(new Object[]{s.getStaffID(), s.getFirstName(), s.getLastName(),
                s.getRole(), s.getDepartment(), s.getFacilityID(), s.getEmail(), s.getPhone(),
                s.getEmploymentStatus(), s.getStartDate(), s.getLineManager(), s.getAccessLevel()});
        }
    }
}