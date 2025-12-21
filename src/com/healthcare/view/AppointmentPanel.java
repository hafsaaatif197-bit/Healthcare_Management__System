package com.healthcare.view;

import com.healthcare.controller.HealthcareController;
import com.healthcare.model.Appointment;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class AppointmentPanel extends JPanel {
    private HealthcareController controller;
    private JTable table;
    private DefaultTableModel tableModel;
    
    // Fields matching your Model's 13 attributes
    private JTextField appointmentIDField, patientIDField, clinicianIDField, facilityIDField;
    private JTextField dateField, timeField, durationField, typeField, statusField;
    private JTextField reasonField, notesField, createdDateField, modifiedField;

    private final Color NAVY_PRIMARY = new Color(40, 53, 147);
    private final Color SLATE_BG = new Color(245, 247, 250);

    public AppointmentPanel(HealthcareController controller) {
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
            "ID", "Patient", "Clinician", "Facility", "Date", "Time", 
            "Duration", "Type", "Status", "Reason", "Notes"
        };
        
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        table = new JTable(tableModel);
        table.setRowHeight(28);
        table.setSelectionBackground(new Color(232, 240, 254));
        
        JTableHeader header = table.getTableHeader();
        header.setBackground(NAVY_PRIMARY);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) loadSelectedAppointment();
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Appointment Schedule"));
        add(scrollPane, BorderLayout.CENTER);

        add(createFormPanel(), BorderLayout.SOUTH);
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Row 1
        addFormField(panel, gbc, 0, 0, "Appt ID:", appointmentIDField = new JTextField());
        addFormField(panel, gbc, 0, 2, "Patient ID:", patientIDField = new JTextField());
        addFormField(panel, gbc, 0, 4, "Clinician ID:", clinicianIDField = new JTextField());

        // Row 2
        addFormField(panel, gbc, 1, 0, "Facility ID:", facilityIDField = new JTextField());
        addFormField(panel, gbc, 1, 2, "Date:", dateField = new JTextField());
        addFormField(panel, gbc, 1, 4, "Time:", timeField = new JTextField());

        // Row 3
        addFormField(panel, gbc, 2, 0, "Duration:", durationField = new JTextField());
        addFormField(panel, gbc, 2, 2, "Type:", typeField = new JTextField());
        addFormField(panel, gbc, 2, 4, "Status:", statusField = new JTextField());

        // Row 4
        addFormField(panel, gbc, 3, 0, "Reason:", reasonField = new JTextField());
        addFormField(panel, gbc, 3, 2, "Notes:", notesField = new JTextField());
        addFormField(panel, gbc, 3, 4, "Created:", createdDateField = new JTextField());

        return panel;
    }

    private void addFormField(JPanel p, GridBagConstraints gbc, int row, int col, String label, JTextField tf) {
        gbc.gridy = row; gbc.gridx = col; p.add(new JLabel(label), gbc);
        gbc.gridx = col + 1; p.add(tf, gbc);
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setOpaque(false);
        
        JButton addBtn = styleButton(new JButton("Book Appointment"), new Color(33, 150, 243));
        addBtn.addActionListener(e -> addAppointment());

        JButton updateBtn = styleButton(new JButton("Update"), new Color(76, 175, 80));
        updateBtn.addActionListener(e -> updateAppointment());

        JButton deleteBtn = styleButton(new JButton("Cancel"), new Color(211, 47, 47));
        deleteBtn.addActionListener(e -> deleteAppointment());

        panel.add(addBtn); panel.add(updateBtn); panel.add(deleteBtn);
        return panel;
    }

    private JButton styleButton(JButton b, Color bg) {
        b.setBackground(bg); b.setForeground(Color.WHITE);
        b.setFocusPainted(false); b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

    private void loadSelectedAppointment() {
        int r = table.getSelectedRow();
        if (r >= 0) {
            appointmentIDField.setText((String) tableModel.getValueAt(r, 0));
            patientIDField.setText((String) tableModel.getValueAt(r, 1));
            clinicianIDField.setText((String) tableModel.getValueAt(r, 2));
            facilityIDField.setText((String) tableModel.getValueAt(r, 3));
            dateField.setText((String) tableModel.getValueAt(r, 4));
            timeField.setText((String) tableModel.getValueAt(r, 5));
            durationField.setText((String) tableModel.getValueAt(r, 6));
            typeField.setText((String) tableModel.getValueAt(r, 7));
            statusField.setText((String) tableModel.getValueAt(r, 8));
            reasonField.setText((String) tableModel.getValueAt(r, 9));
            notesField.setText((String) tableModel.getValueAt(r, 10));
        }
    }

    private void addAppointment() {
        Appointment a = createFromForm();
        if (a != null) {
            controller.addAppointment(a);
            refreshData();
        }
    }

    private void updateAppointment() {
        int r = table.getSelectedRow();
        if (r >= 0) {
            controller.deleteAppointment((String) tableModel.getValueAt(r, 0));
            controller.addAppointment(createFromForm());
            refreshData();
        }
    }

    private void deleteAppointment() {
        int r = table.getSelectedRow();
        if (r >= 0) {
            controller.deleteAppointment((String) tableModel.getValueAt(r, 0));
            refreshData();
        }
    }

    private Appointment createFromForm() {
        
        return new Appointment(
            appointmentIDField.getText(), patientIDField.getText(), clinicianIDField.getText(),
            facilityIDField.getText(), dateField.getText(), timeField.getText(),
            durationField.getText(), typeField.getText(), statusField.getText(),
            reasonField.getText(), notesField.getText(), createdDateField.getText(), ""
        );
    }

    public void refreshData() {
        tableModel.setRowCount(0);
        for (Appointment a : controller.getAllAppointments()) {
            
            tableModel.addRow(new Object[]{
                a.getAppointmentID(), a.getPatientID(), a.getClinicianID(), a.getFacilityID(),
                a.getDate(), a.getTime(), a.getDurationMinutes(), a.getAppointmentType(),
                a.getStatus(), a.getReason(), a.getNotes()
            });
        }
    }
}