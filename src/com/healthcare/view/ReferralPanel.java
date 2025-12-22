package com.healthcare.view;

import com.healthcare.controller.HealthcareController;
import com.healthcare.model.Referral;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.io.File;

/**
 * Referral Management Panel - 16 Fields Restored
 * Professional Navy Theme with Optimized Grid Layout
 */
public class ReferralPanel extends JPanel {
    private HealthcareController controller;
    private JTable table;
    private DefaultTableModel tableModel;
    
    // All 16 original fields preserved
    private JTextField referralIDField, patientIDField, referringClinicianIDField, receivingClinicianIDField;
    private JTextField referringFacilityField, receivingFacilityField, dateField, urgencyField;
    private JTextField referralReasonField, clinicalSummaryField, investigationsField, appointmentIDField;
    private JTextField notesField, statusField, createdDateField, lastUpdatedField;

    // Standard Navy Palette
    private final Color NAVY_PRIMARY = new Color(40, 53, 147);
    private final Color SLATE_BG = new Color(245, 247, 250);

    public ReferralPanel(HealthcareController controller) {
        this.controller = controller;
        initializePanel();
        refreshData();
    }

    private void initializePanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(SLATE_BG);

        // 1. TOP ACTION BAR
        add(createButtonPanel(), BorderLayout.NORTH);

        // 2. CENTER TABLE - ALL 16 COLUMNS RESTORED
        String[] columns = {
            "Referral ID", "Patient ID", "Ref Clinician", "Rec Clinician",
            "Ref Facility", "Rec Facility", "Date", "Urgency", "Reason",
            "Summary", "Investigations", "Appt ID", "Notes", "Status", "Created", "Updated"
        };
        
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        table = new JTable(tableModel);
        table.setRowHeight(26);
        table.setSelectionBackground(new Color(232, 240, 254));
        table.setShowGrid(true);
        table.setGridColor(new Color(230, 230, 230));

        // Styling the Navy Header
        JTableHeader header = table.getTableHeader();
        header.setBackground(NAVY_PRIMARY);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) loadSelectedReferral();
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Referral Registry"));
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);

        // 3. BOTTOM FORM - 4-COLUMN OPTIMIZED GRID
        add(createFormPanel(), BorderLayout.SOUTH);
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Referral Record Details"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 8, 4, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Column 1
        gbc.gridy = 0; gbc.gridx = 0; panel.add(new JLabel("Referral ID:"), gbc);
        gbc.gridx = 1; panel.add(referralIDField = new JTextField(10), gbc);
        gbc.gridy = 1; gbc.gridx = 0; panel.add(new JLabel("Patient ID:"), gbc);
        gbc.gridx = 1; panel.add(patientIDField = new JTextField(10), gbc);
        gbc.gridy = 2; gbc.gridx = 0; panel.add(new JLabel("Ref Clinician:"), gbc);
        gbc.gridx = 1; panel.add(referringClinicianIDField = new JTextField(10), gbc);
        gbc.gridy = 3; gbc.gridx = 0; panel.add(new JLabel("Rec Clinician:"), gbc);
        gbc.gridx = 1; panel.add(receivingClinicianIDField = new JTextField(10), gbc);

        // Column 2
        gbc.gridy = 0; gbc.gridx = 2; panel.add(new JLabel("Ref Facility:"), gbc);
        gbc.gridx = 3; panel.add(referringFacilityField = new JTextField(10), gbc);
        gbc.gridy = 1; gbc.gridx = 2; panel.add(new JLabel("Rec Facility:"), gbc);
        gbc.gridx = 3; panel.add(receivingFacilityField = new JTextField(10), gbc);
        gbc.gridy = 2; gbc.gridx = 2; panel.add(new JLabel("Date:"), gbc);
        gbc.gridx = 3; panel.add(dateField = new JTextField(10), gbc);
        gbc.gridy = 3; gbc.gridx = 2; panel.add(new JLabel("Urgency:"), gbc);
        gbc.gridx = 3; panel.add(urgencyField = new JTextField(10), gbc);

        // Column 3
        gbc.gridy = 0; gbc.gridx = 4; panel.add(new JLabel("Appt ID:"), gbc);
        gbc.gridx = 5; panel.add(appointmentIDField = new JTextField(10), gbc);
        gbc.gridy = 1; gbc.gridx = 4; panel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 5; panel.add(statusField = new JTextField(10), gbc);
        gbc.gridy = 2; gbc.gridx = 4; panel.add(new JLabel("Created:"), gbc);
        gbc.gridx = 5; panel.add(createdDateField = new JTextField(10), gbc);
        gbc.gridy = 3; gbc.gridx = 4; panel.add(new JLabel("Updated:"), gbc);
        gbc.gridx = 5; panel.add(lastUpdatedField = new JTextField(10), gbc);

        // Row 4: Wide Fields (Reason & Summary)
        gbc.gridy = 4; gbc.gridx = 0; panel.add(new JLabel("Reason:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2; panel.add(referralReasonField = new JTextField(), gbc);
        gbc.gridx = 3; gbc.gridwidth = 1; panel.add(new JLabel("Summary:"), gbc);
        gbc.gridx = 4; gbc.gridwidth = 2; panel.add(clinicalSummaryField = new JTextField(), gbc);

        // Row 5: Wide Fields (Investigations & Notes)
        gbc.gridy = 5; gbc.gridx = 0; gbc.gridwidth = 1; panel.add(new JLabel("Investigations:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2; panel.add(investigationsField = new JTextField(), gbc);
        gbc.gridx = 3; gbc.gridwidth = 1; panel.add(new JLabel("Notes:"), gbc);
        gbc.gridx = 4; gbc.gridwidth = 2; panel.add(notesField = new JTextField(), gbc);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 5));
        panel.setOpaque(false);

        JButton addBtn = styleButton(new JButton("Add Referral"), new Color(33, 150, 243));
        JButton updateBtn = styleButton(new JButton("Update"), new Color(76, 175, 80));
        JButton deleteBtn = styleButton(new JButton("Delete"), new Color(211, 47, 47));
        JButton fileBtn = styleButton(new JButton("Generate File"), new Color(255, 143, 0));
        JButton clearBtn = styleButton(new JButton("Clear"), new Color(158, 158, 158));
        JButton refreshBtn = styleButton(new JButton("Refresh"), NAVY_PRIMARY);

        addBtn.addActionListener(e -> addReferral());
        updateBtn.addActionListener(e -> updateReferral());
        deleteBtn.addActionListener(e -> deleteReferral());
        fileBtn.addActionListener(e -> generateReferralFile());
        clearBtn.addActionListener(e -> clearForm());
        refreshBtn.addActionListener(e -> refreshData());

        panel.add(addBtn); panel.add(updateBtn); panel.add(deleteBtn); 
        panel.add(fileBtn); panel.add(clearBtn); panel.add(refreshBtn);
        return panel;
    }

    private JButton styleButton(JButton btn, Color bg) {
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(7, 12, 7, 12));
        return btn;
    }

    // --- LOGIC ---

    private void loadSelectedReferral() {
        int r = table.getSelectedRow();
        if (r >= 0) {
            referralIDField.setText((String) tableModel.getValueAt(r, 0));
            patientIDField.setText((String) tableModel.getValueAt(r, 1));
            referringClinicianIDField.setText((String) tableModel.getValueAt(r, 2));
            receivingClinicianIDField.setText((String) tableModel.getValueAt(r, 3));
            referringFacilityField.setText((String) tableModel.getValueAt(r, 4));
            receivingFacilityField.setText((String) tableModel.getValueAt(r, 5));
            dateField.setText((String) tableModel.getValueAt(r, 6));
            urgencyField.setText((String) tableModel.getValueAt(r, 7));
            referralReasonField.setText((String) tableModel.getValueAt(r, 8));
            clinicalSummaryField.setText((String) tableModel.getValueAt(r, 9));
            investigationsField.setText((String) tableModel.getValueAt(r, 10));
            appointmentIDField.setText((String) tableModel.getValueAt(r, 11));
            notesField.setText((String) tableModel.getValueAt(r, 12));
            statusField.setText((String) tableModel.getValueAt(r, 13));
            createdDateField.setText((String) tableModel.getValueAt(r, 14));
            lastUpdatedField.setText((String) tableModel.getValueAt(r, 15));
        }
    }

    private void addReferral() {
        Referral ref = createReferralFromForm();
        if (ref != null) {
            controller.addReferral(ref);
            refreshData();
            clearForm();
        }
    }

    private void updateReferral() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            controller.deleteReferral((String) tableModel.getValueAt(row, 0));
            controller.addReferral(createReferralFromForm());
            refreshData();
            JOptionPane.showMessageDialog(this, "Referral Updated.");
        }
    }

    private void deleteReferral() {
        int row = table.getSelectedRow();
        if (row >= 0 && JOptionPane.showConfirmDialog(this, "Delete Record?") == 0) {
            controller.deleteReferral((String) tableModel.getValueAt(row, 0));
            refreshData();
            clearForm();
        }
    }

    private void generateReferralFile() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        String id = (String) tableModel.getValueAt(row, 0);
        Referral ref = controller.getAllReferrals().stream().filter(x -> x.getReferralID().equals(id)).findFirst().orElse(null);
        if (ref != null) {
            JFileChooser fc = new JFileChooser();
            fc.setSelectedFile(new File("referral_" + id + ".txt"));
            if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                controller.generateReferralFile(ref, fc.getSelectedFile().getAbsolutePath());
                JOptionPane.showMessageDialog(this, "File Saved.");
            }
        }
    }

    private Referral createReferralFromForm() {
        if (referralIDField.getText().isEmpty()) return null;
        return new Referral(
            referralIDField.getText(), patientIDField.getText(), referringClinicianIDField.getText(),
            receivingClinicianIDField.getText(), referringFacilityField.getText(), receivingFacilityField.getText(),
            dateField.getText(), urgencyField.getText(), referralReasonField.getText(), clinicalSummaryField.getText(),
            investigationsField.getText(), appointmentIDField.getText(), notesField.getText(),
            statusField.getText(), createdDateField.getText(), lastUpdatedField.getText()
        );
    }

    private void clearForm() {
        JTextField[] fields = {
            referralIDField, patientIDField, referringClinicianIDField, receivingClinicianIDField,
            referringFacilityField, receivingFacilityField, dateField, urgencyField,
            referralReasonField, clinicalSummaryField, investigationsField, appointmentIDField,
            notesField, statusField, createdDateField, lastUpdatedField
        };
        for (JTextField f : fields) f.setText("");
    }

    public void refreshData() {
        tableModel.setRowCount(0);
        for (Referral r : controller.getAllReferrals()) {
            tableModel.addRow(new Object[]{
                r.getReferralID(), r.getPatientID(), r.getReferringClinicianID(), r.getReceivingClinicianID(),
                r.getReferringFacility(), r.getReceivingFacility(), r.getDate(), r.getUrgency(),
                r.getReferralReason(), r.getClinicalSummary(), r.getRequestedInvestigations(),
                r.getAppointmentID(), r.getNotes(), r.getStatus(), r.getCreatedDate(), r.getLastUpdated()
            });
        }
    }
}