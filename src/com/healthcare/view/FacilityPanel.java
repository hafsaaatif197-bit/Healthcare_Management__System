package com.healthcare.view;

import com.healthcare.controller.HealthcareController;
import com.healthcare.model.Facility;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

/**
 * Facility Management Panel
 * Navy Enterprise Theme - All 11 Original Fields Preserved
 */
public class FacilityPanel extends JPanel {
    private HealthcareController controller;
    private JTable table;
    private DefaultTableModel tableModel;
    
    // Original fields preserved
    private JTextField facilityIDField, nameField, typeField, addressField, postcodeField;
    private JTextField phoneField, emailField, openingHoursField, managerField, servicesField, capacityField;

    // Standard Navy Palette
    private final Color NAVY_PRIMARY = new Color(40, 53, 147);
    private final Color SLATE_BG = new Color(245, 247, 250);

    public FacilityPanel(HealthcareController controller) {
        this.controller = controller;
        initializePanel();
        refreshData();
    }

    private void initializePanel() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(SLATE_BG);

        // 1. TOP ACTION BAR
        add(createButtonPanel(), BorderLayout.NORTH);

        // 2. CENTER TABLE - ALL COLUMNS RESTORED
        String[] columns = {
            "Facility ID", "Name", "Type", "Address", "Postcode", 
            "Phone", "Email", "Opening Hours", "Manager", "Services", "Capacity"
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

        // Styling the Navy Header
        JTableHeader header = table.getTableHeader();
        header.setBackground(NAVY_PRIMARY);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) loadSelectedFacility();
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Healthcare Facilities"));
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);

        // 3. BOTTOM FORM - ORGANIZED GRID
        add(createFormPanel(), BorderLayout.SOUTH);
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Facility Management Details"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 10, 6, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Column 1
        gbc.gridy = 0; gbc.gridx = 0; panel.add(new JLabel("Facility ID:"), gbc);
        gbc.gridx = 1; panel.add(facilityIDField = new JTextField(12), gbc);
        gbc.gridy = 1; gbc.gridx = 0; panel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1; panel.add(nameField = new JTextField(12), gbc);
        gbc.gridy = 2; gbc.gridx = 0; panel.add(new JLabel("Type:"), gbc);
        gbc.gridx = 1; panel.add(typeField = new JTextField(12), gbc);
        gbc.gridy = 3; gbc.gridx = 0; panel.add(new JLabel("Manager:"), gbc);
        gbc.gridx = 1; panel.add(managerField = new JTextField(12), gbc);

        // Column 2
        gbc.gridy = 0; gbc.gridx = 2; panel.add(new JLabel("Address:"), gbc);
        gbc.gridx = 3; panel.add(addressField = new JTextField(12), gbc);
        gbc.gridy = 1; gbc.gridx = 2; panel.add(new JLabel("Postcode:"), gbc);
        gbc.gridx = 3; panel.add(postcodeField = new JTextField(12), gbc);
        gbc.gridy = 2; gbc.gridx = 2; panel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 3; panel.add(phoneField = new JTextField(12), gbc);
        gbc.gridy = 3; gbc.gridx = 2; panel.add(new JLabel("Capacity:"), gbc);
        gbc.gridx = 3; panel.add(capacityField = new JTextField(12), gbc);

        // Column 3
        gbc.gridy = 0; gbc.gridx = 4; panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 5; panel.add(emailField = new JTextField(12), gbc);
        gbc.gridy = 1; gbc.gridx = 4; panel.add(new JLabel("Opening Hours:"), gbc);
        gbc.gridx = 5; panel.add(openingHoursField = new JTextField(12), gbc);
        gbc.gridy = 2; gbc.gridx = 4; panel.add(new JLabel("Services:"), gbc);
        gbc.gridx = 5; panel.add(servicesField = new JTextField(12), gbc);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        panel.setOpaque(false);

        JButton addBtn = styleButton(new JButton("Add Facility"), new Color(33, 150, 243));
        addBtn.addActionListener(e -> addFacility());

        JButton updateBtn = styleButton(new JButton("Update Record"), new Color(76, 175, 80));
        updateBtn.addActionListener(e -> updateFacility());

        JButton deleteBtn = styleButton(new JButton("Remove Facility"), new Color(211, 47, 47));
        deleteBtn.addActionListener(e -> deleteFacility());

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

    // --- LOGIC ---

    private void loadSelectedFacility() {
        int r = table.getSelectedRow();
        if (r >= 0) {
            facilityIDField.setText((String) tableModel.getValueAt(r, 0));
            nameField.setText((String) tableModel.getValueAt(r, 1));
            typeField.setText((String) tableModel.getValueAt(r, 2));
            addressField.setText((String) tableModel.getValueAt(r, 3));
            postcodeField.setText((String) tableModel.getValueAt(r, 4));
            phoneField.setText((String) tableModel.getValueAt(r, 5));
            emailField.setText((String) tableModel.getValueAt(r, 6));
            openingHoursField.setText((String) tableModel.getValueAt(r, 7));
            managerField.setText((String) tableModel.getValueAt(r, 8));
            servicesField.setText((String) tableModel.getValueAt(r, 9));
            capacityField.setText((String) tableModel.getValueAt(r, 10));
        }
    }

    private void addFacility() {
        Facility f = createFacilityFromForm();
        if (f != null) {
            controller.addFacility(f);
            refreshData();
            clearForm();
        }
    }

    private void updateFacility() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            String id = (String) tableModel.getValueAt(row, 0);
            controller.deleteFacility(id);
            controller.addFacility(createFacilityFromForm());
            refreshData();
            JOptionPane.showMessageDialog(this, "Facility Record Updated.");
        }
    }

    private void deleteFacility() {
        int row = table.getSelectedRow();
        if (row >= 0 && JOptionPane.showConfirmDialog(this, "Delete Facility?") == 0) {
            controller.deleteFacility((String) tableModel.getValueAt(row, 0));
            refreshData();
            clearForm();
        }
    }

    private Facility createFacilityFromForm() {
        if (facilityIDField.getText().isEmpty()) return null;
        return new Facility(
            facilityIDField.getText(), nameField.getText(), typeField.getText(),
            addressField.getText(), postcodeField.getText(), phoneField.getText(),
            emailField.getText(), openingHoursField.getText(), managerField.getText(),
            servicesField.getText(), capacityField.getText()
        );
    }

    private void clearForm() {
        JTextField[] fields = {
            facilityIDField, nameField, typeField, addressField, postcodeField,
            phoneField, emailField, openingHoursField, managerField, servicesField, capacityField
        };
        for (JTextField f : fields) f.setText("");
    }

    public void refreshData() {
        tableModel.setRowCount(0);
        for (Facility f : controller.getAllFacilities()) {
            tableModel.addRow(new Object[]{
                f.getFacilityID(), f.getName(), f.getType(), f.getAddress(),
                f.getPostcode(), f.getPhone(), f.getEmail(), f.getOpeningHours(),
                f.getManagerName(), f.getServices(), f.getCapacity()
            });
        }
    }
}