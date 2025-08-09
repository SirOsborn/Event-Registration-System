package com.eventms.view;

import com.eventms.model.User;
import com.eventms.service.EventService;
import com.eventms.service.RegistrationService;
import com.eventms.service.UserService;
import java.awt.*;
import javax.swing.*;

/**
 * Main application window for the Event Management System
 * Provides the main interface after successful login
 */
public class MainApplicationFrame extends JFrame {
    private User currentUser;
    private UserService userService;
    private EventService eventService;
    private RegistrationService registrationService;
    
    // Main components
    private JMenuBar menuBar;
    private JPanel mainPanel;
    private JLabel welcomeLabel;
    private JTextArea infoArea;

    public MainApplicationFrame(User user, UserService userService, EventService eventService, 
                               RegistrationService registrationService) {
        this.currentUser = user;
        this.userService = userService;
        this.eventService = eventService;
        this.registrationService = registrationService;
        
        initializeComponents();
        setupLayout();
        setupMenuBar();
        setupWindow();
    }

    private void initializeComponents() {
        // Create main panel
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        
        // Create welcome label
        welcomeLabel = new JLabel("Welcome, " + currentUser.getFullName() + "!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        welcomeLabel.setForeground(new Color(60, 179, 113));
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Create info area
        infoArea = new JTextArea();
        infoArea.setEditable(false);
        infoArea.setFont(new Font("Arial", Font.PLAIN, 14));
        infoArea.setBackground(new Color(248, 249, 250));
        infoArea.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Set initial content
        updateInfoArea();
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Create responsive top panel for welcome message
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.add(welcomeLabel, BorderLayout.CENTER);
        
        // Make welcome label responsive to window size
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        add(topPanel, BorderLayout.NORTH);
        
        // Create center panel with flexible layout
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);
        
        // Add info area to center with responsive scrolling
        JScrollPane scrollPane = new JScrollPane(infoArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Dashboard"));
        scrollPane.setPreferredSize(new Dimension(0, 0)); // Let it grow
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Smooth scrolling
        
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);
        
        // Create bottom panel with responsive action buttons
        JPanel bottomPanel = createBottomPanel();
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createBottomPanel() {
        // Create responsive bottom panel that adjusts to window size
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        // Create action buttons with responsive sizing
        JButton refreshButton = new JButton("Refresh Dashboard");
        JButton profileButton = new JButton("View Profile");
        JButton logoutButton = new JButton("Logout");
        
        // Style buttons with flexible sizing
        styleButton(refreshButton, new Color(60, 179, 113));
        styleButton(profileButton, new Color(23, 162, 184));
        styleButton(logoutButton, new Color(220, 53, 69));
        
        // Make buttons responsive
        refreshButton.setMaximumSize(new Dimension(200, 35));
        profileButton.setMaximumSize(new Dimension(200, 35));
        logoutButton.setMaximumSize(new Dimension(200, 35));
        
        // Add action listeners
        refreshButton.addActionListener(e -> refreshDashboard());
        profileButton.addActionListener(e -> showProfile());
        logoutButton.addActionListener(e -> logout());
        
        // Add buttons with flexible spacing
        bottomPanel.add(Box.createHorizontalGlue());
        bottomPanel.add(refreshButton);
        bottomPanel.add(Box.createHorizontalStrut(15));
        bottomPanel.add(profileButton);
        bottomPanel.add(Box.createHorizontalStrut(15));
        bottomPanel.add(logoutButton);
        bottomPanel.add(Box.createHorizontalGlue());
        
        return bottomPanel;
    }

    private void styleButton(JButton button, Color backgroundColor) {
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(120, 30));
    }

    private void setupMenuBar() {
        menuBar = new JMenuBar();
        
        // File menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem refreshMenuItem = new JMenuItem("Refresh");
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        
        refreshMenuItem.addActionListener(e -> refreshDashboard());
        exitMenuItem.addActionListener(e -> logout());
        
        fileMenu.add(refreshMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(exitMenuItem);
        
        // User menu
        JMenu userMenu = new JMenu("User");
        JMenuItem profileMenuItem = new JMenuItem("Profile");
        JMenuItem logoutMenuItem = new JMenuItem("Logout");
        
        profileMenuItem.addActionListener(e -> showProfile());
        logoutMenuItem.addActionListener(e -> logout());
        
        userMenu.add(profileMenuItem);
        userMenu.addSeparator();
        userMenu.add(logoutMenuItem);
        
        // Events menu
        JMenu eventsMenu = new JMenu("Events");
        JMenuItem viewEventsMenuItem = new JMenuItem("View All Events");
        JMenuItem myRegistrationsMenuItem = new JMenuItem("My Registrations");
        
        viewEventsMenuItem.addActionListener(e -> showAllEvents());
        myRegistrationsMenuItem.addActionListener(e -> showMyRegistrations());
        
        eventsMenu.add(viewEventsMenuItem);
        eventsMenu.add(myRegistrationsMenuItem);
        
        // Help menu
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutMenuItem = new JMenuItem("About");
        
        aboutMenuItem.addActionListener(e -> showAbout());
        
        helpMenu.add(aboutMenuItem);
        
        // Add menus to menu bar
        menuBar.add(fileMenu);
        menuBar.add(userMenu);
        menuBar.add(eventsMenu);
        menuBar.add(helpMenu);
        
        setJMenuBar(menuBar);
    }

    private void setupWindow() {
        setTitle("Event Management System - Dashboard");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setResizable(true);
        
        // Set responsive window dimensions
        setSize(900, 700);
        setMinimumSize(new Dimension(600, 400));
        setMaximumSize(new Dimension(1400, 1000));
        setLocationRelativeTo(null);
        
        // Add component listener for responsive behavior
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                // Adjust layout components when window is resized
                revalidate();
                repaint();
            }
        });
        
        // Handle window closing
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                logout();
            }
        });
        
        // Maximize on startup for better user experience
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private void updateInfoArea() {
        StringBuilder info = new StringBuilder();
        info.append("=== USER INFORMATION ===\n");
        info.append("Name: ").append(currentUser.getFullName()).append("\n");
        info.append("Email: ").append(currentUser.getEmail()).append("\n");
        info.append("Contact: ").append(currentUser.getContactNumber()).append("\n");
        info.append("Role: ").append(currentUser.getRole()).append("\n");
        info.append("User ID: ").append(currentUser.getUserId()).append("\n");
        info.append("\n");
        
        info.append("=== QUICK ACTIONS ===\n");
        info.append("• Use the Events menu to view available events\n");
        info.append("• Check 'My Registrations' to see events you've registered for\n");
        info.append("• Visit your profile to update personal information\n");
        info.append("• Use Refresh to update the dashboard with latest data\n");
        info.append("\n");
        
        info.append("=== SYSTEM STATUS ===\n");
        info.append("✓ Successfully logged in\n");
        info.append("✓ All services are operational\n");
        info.append("✓ Ready to manage events\n");
        
        infoArea.setText(info.toString());
    }

    private void refreshDashboard() {
        // Update the info area with latest data
        updateInfoArea();
        
        // Show confirmation
        JOptionPane.showMessageDialog(
            this,
            "Dashboard refreshed successfully!",
            "Refresh Complete",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void showProfile() {
        // Create profile dialog
        JDialog profileDialog = new JDialog(this, "User Profile", true);
        profileDialog.setLayout(new BorderLayout());
        
        // Create profile content
        JPanel profilePanel = new JPanel(new GridBagLayout());
        profilePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        
        int row = 0;
        addProfileField(profilePanel, gbc, "Full Name:", currentUser.getFullName(), row++);
        addProfileField(profilePanel, gbc, "Email:", currentUser.getEmail(), row++);
        addProfileField(profilePanel, gbc, "Contact Number:", currentUser.getContactNumber(), row++);
        addProfileField(profilePanel, gbc, "Role:", currentUser.getRole().toString(), row++);
        addProfileField(profilePanel, gbc, "User ID:", String.valueOf(currentUser.getUserId()), row++);
        addProfileField(profilePanel, gbc, "Occupation:", currentUser.getOccupation(), row++);
        addProfileField(profilePanel, gbc, "Date of Birth:", currentUser.getDob(), row++);
        addProfileField(profilePanel, gbc, "Gender:", String.valueOf(currentUser.getGender()), row++);
        
        // Add close button
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> profileDialog.dispose());
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(closeButton);
        
        profileDialog.add(profilePanel, BorderLayout.CENTER);
        profileDialog.add(buttonPanel, BorderLayout.SOUTH);
        
        profileDialog.pack();
        profileDialog.setLocationRelativeTo(this);
        profileDialog.setVisible(true);
    }

    private void addProfileField(JPanel panel, GridBagConstraints gbc, String label, String value, int row) {
        gbc.gridx = 0; gbc.gridy = row;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel(label), gbc);
        
        gbc.gridx = 1; gbc.gridy = row;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(valueLabel, gbc);
    }

    private void showAllEvents() {
        JOptionPane.showMessageDialog(
            this,
            "Events management feature will be implemented here.\n" +
            "This will show all available events for registration.",
            "Events",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void showMyRegistrations() {
        JOptionPane.showMessageDialog(
            this,
            "Registration management feature will be implemented here.\n" +
            "This will show all events you have registered for.",
            "My Registrations",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void showAbout() {
        JOptionPane.showMessageDialog(
            this,
            "Event Management System\n" +
            "Version 1.0\n\n" +
            "A comprehensive system for managing events and registrations.\n" +
            "Built with Java Swing for desktop GUI experience.",
            "About",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void logout() {
        int result = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to logout?",
            "Confirm Logout",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (result == JOptionPane.YES_OPTION) {
            // Create new login frame
            SwingUtilities.invokeLater(() -> {
                LoginFrame loginFrame = new LoginFrame(userService);
                loginFrame.setVisible(true);
                this.dispose();
            });
        }
    }
}
