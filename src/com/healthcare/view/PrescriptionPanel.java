package com.healthcare.view;

import com.healthcare.controller.HealthcareController;
import com.healthcare.model.Prescription;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;


public class PrescriptionPanel extends JPanel {
    private HealthcareController controller;
    private JTable table;
    private DefaultTableModel tableModel;
    
    
    private JTextField prescriptionIDField, patientIDField, clinicianIDField, appointmentIDField, medicationField;
    private JTextField dosageField, frequencyField, durationDaysField, quantityField, pharmacyField;
    private JTextField datePrescribedField, issueDateField, collectionDateField, collectionStatusField, notesField;

    
    private final Color NAVY_PRIMARY = new Color(40, 53, 147);
    private final Color SLATE_BG = new Color(245, 247, 250);

    public PrescriptionPanel(HealthcareController controller) {
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
            "Presc ID", "Patient ID", "Clinician ID", "Appt ID", "Medication", 
            "Dosage", "Freq", "Days", "Qty", "Pharmacy", 
            "Date", "Issue Date", "Coll. Date", "Status", "Notes"
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
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) loadSelectedPrescription();
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Pharmacy Order History"));
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);

        
        add(createFormPanel(), BorderLayout.SOUTH);
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Prescription Entry Form"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 10, 6, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Column 1: Identification
        gbc.gridy = 0; gbc.gridx = 0; panel.add(new JLabel("Prescription ID:"), gbc);
        gbc.gridx = 1; panel.add(prescriptionIDField = new JTextField(12), gbc);
        gbc.gridy = 1; gbc.gridx = 0; panel.add(new JLabel("Patient ID:"), gbc);
        gbc.gridx = 1; panel.add(patientIDField = new JTextField(12), gbc);
        gbc.gridy = 2; gbc.gridx = 0; panel.add(new JLabel("Clinician ID:"), gbc);
        gbc.gridx = 1; panel.add(clinicianIDField = new JTextField(12), gbc);
        gbc.gridy = 3; gbc.gridx = 0; panel.add(new JLabel("Appointment ID:"), gbc);
        gbc.gridx = 1; panel.add(appointmentIDField = new JTextField(12), gbc);
        gbc.gridy = 4; gbc.gridx = 0; panel.add(new JLabel("Pharmacy:"), gbc);
        gbc.gridx = 1; panel.add(pharmacyField = new JTextField(12), gbc);

        // Column 2: Medication Details
        gbc.gridy = 0; gbc.gridx = 2; panel.add(new JLabel("Medication Name:"), gbc);
        gbc.gridx = 3; panel.add(medicationField = new JTextField(12), gbc);
        gbc.gridy = 1; gbc.gridx = 2; panel.add(new JLabel("Dosage:"), gbc);
        gbc.gridx = 3; panel.add(dosageField = new JTextField(12), gbc);
        gbc.gridy = 2; gbc.gridx = 2; panel.add(new JLabel("Frequency:"), gbc);
        gbc.gridx = 3; panel.add(frequencyField = new JTextField(12), gbc);
        gbc.gridy = 3; gbc.gridx = 2; panel.add(new JLabel("Duration (Days):"), gbc);
        gbc.gridx = 3; panel.add(durationDaysField = new JTextField(12), gbc);
        gbc.gridy = 4; gbc.gridx = 2; panel.add(new JLabel("Quantity:"), gbc);
        gbc.gridx = 3; panel.add(quantityField = new JTextField(12), gbc);

        // Column 3: Tracking & Notes
        gbc.gridy = 0; gbc.gridx = 4; panel.add(new JLabel("Date Prescribed:"), gbc);
        gbc.gridx = 5; panel.add(datePrescribedField = new JTextField(12), gbc);
        gbc.gridy = 1; gbc.gridx = 4; panel.add(new JLabel("Issue Date:"), gbc);
        gbc.gridx = 5; panel.add(issueDateField = new JTextField(12), gbc);
        gbc.gridy = 2; gbc.gridx = 4; panel.add(new JLabel("Collection Date:"), gbc);
        gbc.gridx = 5; panel.add(collectionDateField = new JTextField(12), gbc);
        gbc.gridy = 3; gbc.gridx = 4; panel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 5; panel.add(collectionStatusField = new JTextField(12), gbc);
        gbc.gridy = 4; gbc.gridx = 4; panel.add(new JLabel("Notes:"), gbc);
        gbc.gridx = 5; panel.add(notesField = new JTextField(12), gbc);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        panel.setOpaque(false);

        JButton addBtn = styleButton(new JButton("Add Prescription"), new Color(33, 150, 243));
        addBtn.addActionListener(e -> addPrescription());

        JButton updateBtn = styleButton(new JButton("Update Script"), new Color(76, 175, 80));
        updateBtn.addActionListener(e -> updatePrescription());

        JButton deleteBtn = styleButton(new JButton("Delete Script"), new Color(211, 47, 47));
        deleteBtn.addActionListener(e -> deletePrescription());

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

    

    private void loadSelectedPrescription() {
        int r = table.getSelectedRow();
        if (r >= 0) {
            prescriptionIDField.setText((String) tableModel.getValueAt(r, 0));
            patientIDField.setText((String) tableModel.getValueAt(r, 1));
            clinicianIDField.setText((String) tableModel.getValueAt(r, 2));
            appointmentIDField.setText((String) tableModel.getValueAt(r, 3));
            medicationField.setText((String) tableModel.getValueAt(r, 4));
            dosageField.setText((String) tableModel.getValueAt(r, 5));
            frequencyField.setText((String) tableModel.getValueAt(r, 6));
            durationDaysField.setText((String) tableModel.getValueAt(r, 7));
            quantityField.setText((String) tableModel.getValueAt(r, 8));
            pharmacyField.setText((String) tableModel.getValueAt(r, 9));
            datePrescribedField.setText((String) tableModel.getValueAt(r, 10));
            issueDateField.setText((String) tableModel.getValueAt(r, 11));
            collectionDateField.setText((String) tableModel.getValueAt(r, 12));
            collectionStatusField.setText((String) tableModel.getValueAt(r, 13));
            notesField.setText((String) tableModel.getValueAt(r, 14));
        }
    }

    private void addPrescription() {
        Prescription p = createPrescriptionFromForm();
        if (p != null) {
            controller.addPrescription(p);
            refreshData();
            clearForm();
        }
    }

    private void updatePrescription() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            controller.deletePrescription((String) tableModel.getValueAt(row, 0));
            controller.addPrescription(createPrescriptionFromForm());
            refreshData();
            JOptionPane.showMessageDialog(this, "Script Updated.");
        }
    }

    private void deletePrescription() {
        int row = table.getSelectedRow();
        if (row >= 0 && JOptionPane.showConfirmDialog(this, "Delete Script?") == 0) {
            controller.deletePrescription((String) tableModel.getValueAt(row, 0));
            refreshData();
            clearForm();
        }
    }

    private Prescription createPrescriptionFromForm() {
        if (prescriptionIDField.getText().isEmpty()) return null;
        return new Prescription(
            prescriptionIDField.getText(), patientIDField.getText(), clinicianIDField.getText(),
            appointmentIDField.getText(), medicationField.getText(), dosageField.getText(),
            frequencyField.getText(), durationDaysField.getText(), quantityField.getText(),
            pharmacyField.getText(), datePrescribedField.getText(), issueDateField.getText(),
            collectionDateField.getText(), collectionStatusField.getText(), notesField.getText()
        );
    }

    private void clearForm() {
        JTextField[] fields = {
            prescriptionIDField, patientIDField, clinicianIDField, appointmentIDField, 
            medicationField, dosageField, frequencyField, durationDaysField, 
            quantityField, pharmacyField, datePrescribedField, issueDateField, 
            collectionDateField, collectionStatusField, notesField
        };
        for (JTextField f : fields) f.setText("");
    }

    public void refreshData() {
        tableModel.setRowCount(0);
        for (Prescription p : controller.getAllPrescriptions()) {
            tableModel.addRow(new Object[]{
                p.getPrescriptionID(), p.getPatientID(), p.getClinicianID(), p.getAppointmentID(),
                p.getMedication(), p.getDosage(), p.getFrequency(), p.getDurationDays(),
                p.getQuantity(), p.getPharmacy(), p.getDatePrescribed(), p.getIssueDate(),
                p.getCollectionDate(), p.getCollectionStatus(), p.getNotes()
            });
        }
    }
}