package com.healthcare.view;

import com.healthcare.controller.HealthcareController;
import javax.swing.*;
import java.awt.*;

public class MainView extends JFrame {
    private HealthcareController controller;
    private JTabbedPane tabbedPane;
    private PatientPanel patientPanel;
    private ClinicianPanel clinicianPanel;
    private FacilityPanel facilityPanel;
    private AppointmentPanel appointmentPanel;
    private PrescriptionPanel prescriptionPanel;
    private ReferralPanel referralPanel;
    private StaffPanel staffPanel;

    public MainView(HealthcareController controller) {
        this.controller = controller;
        initializeGUI();
        refreshAllPanels();
    }

    private void initializeGUI() {
        setTitle("Healthcare Management System - Admin Console");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1300, 850);
        setLocationRelativeTo(null);

        
        Color primaryNavy = new Color(40, 53, 147);
        Color backgroundSlate = new Color(240, 242, 245);

        getContentPane().setBackground(backgroundSlate);

        // Header Section
        JPanel header = new JPanel(new BorderLayout());
        header.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
        header.setBackground(primaryNavy);

        JLabel titleLabel = new JLabel("HEALTHCARE MANAGEMENT SYSTEM");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        header.add(titleLabel, BorderLayout.WEST);

        add(header, BorderLayout.NORTH);

        createMenuBar();

       
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabbedPane.setBackground(Color.WHITE);
        
        // Panels
        patientPanel = new PatientPanel(controller);
        clinicianPanel = new ClinicianPanel(controller);
        facilityPanel = new FacilityPanel(controller);
        appointmentPanel = new AppointmentPanel(controller);
        prescriptionPanel = new PrescriptionPanel(controller);
        referralPanel = new ReferralPanel(controller);
        staffPanel = new StaffPanel(controller);

        tabbedPane.addTab("Patients", patientPanel);
        tabbedPane.addTab("Appointments", appointmentPanel);
        tabbedPane.addTab("Prescriptions", prescriptionPanel);
        tabbedPane.addTab("Clinicians", clinicianPanel);
        tabbedPane.addTab("Facilities", facilityPanel);
        tabbedPane.addTab("Referrals", referralPanel);
        tabbedPane.addTab("Staff", staffPanel);

        add(tabbedPane, BorderLayout.CENTER);

        
        JPanel statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusBar.setBackground(Color.WHITE);
        statusBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 220, 220)));
        statusBar.add(new JLabel(" System Status: Operational | Connection: Secure"));
        add(statusBar, BorderLayout.SOUTH);
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem loadMenuItem = new JMenuItem("Import CSV Data");
        loadMenuItem.addActionListener(e -> loadData());
        fileMenu.add(loadMenuItem);
        fileMenu.addSeparator();
        JMenuItem exitMenuItem = new JMenuItem("Exit System");
        exitMenuItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitMenuItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
    }

    private void loadData() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            controller.loadData(fileChooser.getSelectedFile().getAbsolutePath());
            refreshAllPanels();
            JOptionPane.showMessageDialog(this, "System data synchronized.");
        }
    }

    public void refreshAllPanels() {
        patientPanel.refreshData();
        clinicianPanel.refreshData();
        facilityPanel.refreshData();
        appointmentPanel.refreshData();
        prescriptionPanel.refreshData();
        referralPanel.refreshData();
        staffPanel.refreshData();
    }
}