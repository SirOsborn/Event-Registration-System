package com.eventms.view;

import com.eventms.database.Database;
import com.eventms.database.EventDao;
import com.eventms.database.EventTokenDao;
import com.eventms.database.RegistrationDao;
import com.eventms.database.UserDao;
import com.eventms.model.User;
import com.eventms.service.EventService;
import com.eventms.service.RegistrationService;
import com.eventms.service.UserService;
import com.eventms.view.ui.Theme;
import java.awt.*;
import javax.swing.*;

/**
 * Main application window for the Event Management System
 * Provides the main interface after successful login
 */
public class MainApplicationFrame extends JFrame {
    @SuppressWarnings("FieldMayBeFinal")
    private User currentUser;
    @SuppressWarnings("FieldMayBeFinal")
    private UserService userService;
    @SuppressWarnings({"FieldMayBeFinal", "unused"})
    private EventService eventService;
    @SuppressWarnings({"FieldMayBeFinal", "unused"})
    private RegistrationService registrationService;
    
    // Main components
    private JMenuBar menuBar;
    private JPanel mainPanel;
    private JLabel welcomeLabel;
    private JTextArea infoArea;
    private JTabbedPane tabbedPane;
    private JPanel feedPanel;

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
    Theme.install();
    mainPanel = new JPanel(new BorderLayout());
    mainPanel.setBackground(Theme.BG_APP);
        
        // Create welcome label
    welcomeLabel = Theme.h1("Welcome, " + currentUser.getFullName() + "!");
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Create info area
        infoArea = new JTextArea();
        infoArea.setEditable(false);
    infoArea.setFont(Theme.fontPlain(14));
    infoArea.setBackground(Theme.BG_SURFACE);
        infoArea.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Set initial content
        updateInfoArea();
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Create responsive top panel for welcome message
    JPanel topPanel = new JPanel(new BorderLayout());
    topPanel.setBackground(Theme.BG_APP);
        topPanel.add(welcomeLabel, BorderLayout.CENTER);
        
        // Make welcome label responsive to window size
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        add(topPanel, BorderLayout.NORTH);
        
    // Center content: Tabs for Feed (open events) and Dashboard
    tabbedPane = new JTabbedPane();
    tabbedPane.setBorder(null);

    // Feed tab
    JPanel feedWrapper = new JPanel(new BorderLayout());
    feedWrapper.setBackground(Theme.BG_APP);
    feedPanel = new JPanel();
    feedPanel.setLayout(new BoxLayout(feedPanel, BoxLayout.Y_AXIS));
    feedPanel.setBackground(Theme.BG_APP);
    JScrollPane feedScroll = new JScrollPane(feedPanel);
    // Minimal look: remove titled border/lines
    feedScroll.setBorder(null);
    feedScroll.getVerticalScrollBar().setUnitIncrement(16);
    feedWrapper.add(feedScroll, BorderLayout.CENTER);
    tabbedPane.addTab("Feed", feedWrapper);

    // Dashboard tab (existing info panel)
    JPanel dashboardWrapper = new JPanel(new BorderLayout());
    dashboardWrapper.setBackground(Theme.BG_APP);
    JScrollPane dashboardScroll = new JScrollPane(infoArea);
    // Minimal look: no titled border
    dashboardScroll.setBorder(null);
    dashboardScroll.setPreferredSize(new Dimension(0, 0));
    dashboardScroll.getVerticalScrollBar().setUnitIncrement(16);
    dashboardWrapper.add(dashboardScroll, BorderLayout.CENTER);
    tabbedPane.addTab("Dashboard", dashboardWrapper);

    add(tabbedPane, BorderLayout.CENTER);
    // Initial feed load
    refreshFeed();
        
        // Create bottom panel with responsive action buttons
    JPanel bottomPanel = createBottomPanel();
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createBottomPanel() {
        // Create responsive bottom panel that adjusts to window size
    JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
    bottomPanel.setBackground(Theme.BG_SURFACE);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        // Create action buttons with responsive sizing
    JButton refreshButton = new JButton("Refresh Dashboard");
    JButton profileButton = new JButton("View Profile");
    JButton logoutButton = new JButton("Logout");
        
        // Style buttons with flexible sizing
    Theme.styleSuccessButton(refreshButton);
    Theme.styleInfoButton(profileButton);
    Theme.styleDangerButton(logoutButton);
        
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

    // Styling handled globally by Theme

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
    JMenuItem myEventsMenuItem = new JMenuItem("My Events (Manage)");
    JMenuItem createEventMenuItem = new JMenuItem("Create Event");
        
    viewEventsMenuItem.addActionListener(e -> showAllEvents());
    myRegistrationsMenuItem.addActionListener(e -> showMyRegistrations());
    myEventsMenuItem.addActionListener(e -> showMyEvents());
    createEventMenuItem.addActionListener(e -> showCreateEvent());
        
    eventsMenu.add(viewEventsMenuItem);
    eventsMenu.add(myRegistrationsMenuItem);
    eventsMenu.add(myEventsMenuItem);
    eventsMenu.addSeparator();
    eventsMenu.add(createEventMenuItem);
        
        // Help menu
        JMenu helpMenu = new JMenu("Help");
    JMenuItem aboutMenuItem = new JMenuItem("About");
        
    aboutMenuItem.addActionListener(e -> showAbout());
        
    helpMenu.add(aboutMenuItem);
        
        // Admin menu (role-based)
        if (currentUser.getRole().toString().equalsIgnoreCase("ADMIN")) {
            JMenu adminMenu = new JMenu("Admin");
            JMenuItem tokensItem = new JMenuItem("Manage Tokens");
            JMenuItem cancelEventItem = new JMenuItem("Cancel Event");
            JMenuItem manageUsersItem = new JMenuItem("Manage Users");
            JMenuItem manageEventsItem = new JMenuItem("Manage Events");
            tokensItem.addActionListener(e -> showTokens());
            cancelEventItem.addActionListener(e -> showCancelEvent());
            manageUsersItem.addActionListener(e -> showManageUsers());
            manageEventsItem.addActionListener(e -> showManageEvents());
            adminMenu.add(tokensItem);
            adminMenu.add(cancelEventItem);
            adminMenu.add(manageUsersItem);
            adminMenu.add(manageEventsItem);
            menuBar.add(adminMenu);
        }

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

    // Set app icon
    try { setIconImage(com.eventms.view.ui.Theme.getAppIcon()); } catch (Exception ignore) {}
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
    info.append("- Use the Events menu to view available events\n");
    info.append("- Check 'My Registrations' to see events you've registered for\n");
    info.append("- Visit your profile to update personal information\n");
    info.append("- Use Refresh to update the dashboard with latest data\n");
        info.append("\n");
        
    info.append("=== SYSTEM STATUS ===\n");
    info.append("OK: Successfully logged in\n");
    info.append("OK: All services are operational\n");
    info.append("OK: Ready to manage events\n");
        
        infoArea.setText(info.toString());
    }

    private void refreshDashboard() {
        // Update the info area and refresh the feed
        updateInfoArea();
        refreshFeed();
    }

    private void refreshFeed() {
        feedPanel.removeAll();
        if (!Database.isConfigured()) {
            JLabel msg = new JLabel("Database not available. Drop MySQL driver jar into lib/ and set DB env vars.");
            msg.setForeground(Theme.TEXT_SECONDARY);
            msg.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
            feedPanel.add(msg);
            feedPanel.revalidate();
            feedPanel.repaint();
            return;
        }
        EventDao dao = new EventDao();
        java.util.List<EventDao.EventRow> rows = dao.listAll();
        int added = 0;
        for (EventDao.EventRow r : rows) {
            if ("open".equalsIgnoreCase(r.status)) {
                feedPanel.add(buildFeedCard(r));
                feedPanel.add(Box.createVerticalStrut(10));
                added++;
            }
        }
        if (added == 0) {
            JLabel none = new JLabel("No open events at the moment.");
            none.setForeground(Theme.TEXT_SECONDARY);
            none.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
            feedPanel.add(none);
        }
        feedPanel.revalidate();
        feedPanel.repaint();
    }

    private JPanel buildFeedCard(EventDao.EventRow r) {
    JPanel card = new JPanel(new BorderLayout());
    card.setBackground(Theme.BG_SURFACE);
    card.setBorder(Theme.cardBorder());

        JLabel title = new JLabel(r.title);
    title.setFont(Theme.fontBold(16));
    title.setForeground(Theme.TEXT_PRIMARY);
        card.add(title, BorderLayout.NORTH);

        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
    body.setBackground(Theme.BG_SURFACE);
        java.text.SimpleDateFormat fmt = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");
        String when = fmt.format(r.start) + " → " + fmt.format(r.end);
        body.add(labelLine("When:", when));
        body.add(labelLine("Location:", r.location));
        body.add(labelLine("Capacity:", String.valueOf(r.capacity)));
        if (r.description != null && !r.description.isBlank()) {
            body.add(Box.createVerticalStrut(8));
            JTextArea ta = new JTextArea(r.description);
            ta.setWrapStyleWord(true);
            ta.setLineWrap(true);
            ta.setEditable(false);
            ta.setBackground(new Color(250, 250, 250));
            ta.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
            card.add(ta, BorderLayout.CENTER);
        } else {
            card.add(body, BorderLayout.CENTER);
        }

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    actions.setBackground(Theme.BG_SURFACE);
        JButton registerBtn = new JButton("Register");
    Theme.styleSuccessButton(registerBtn);
        registerBtn.addActionListener(e -> {
            RegistrationDao rdao = new RegistrationDao();
            boolean ok = rdao.register(currentUser.getUserId(), r.id);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Registered for '" + r.title + "'", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Event is full or registration failed.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        actions.add(registerBtn);
        card.add(actions, BorderLayout.SOUTH);

        return card;
    }

    private JPanel labelLine(String key, String value) {
        JPanel line = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 2));
    line.setBackground(Theme.BG_SURFACE);
        JLabel k = new JLabel(key);
    k.setForeground(Theme.TEXT_SECONDARY);
        JLabel v = new JLabel(value);
    v.setForeground(Theme.TEXT_PRIMARY);
        line.add(k); line.add(v);
        return line;
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
        if (!Database.isConfigured()) {
            JOptionPane.showMessageDialog(this,
                "Database driver not found. Drop mysql-connector-j.jar into lib/ and set DB env vars to enable live events.",
                "Database Unavailable",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        EventDao dao = new EventDao();
        java.util.List<EventDao.EventRow> rows = dao.listAll();
        String[] cols = {"ID", "Title", "Location", "Start", "End", "Capacity", "Status"};
        Object[][] data = new Object[rows.size()][cols.length];
        for (int i = 0; i < rows.size(); i++) {
            var r = rows.get(i);
            data[i][0] = r.id;
            data[i][1] = r.title;
            data[i][2] = r.location;
            data[i][3] = r.start;
            data[i][4] = r.end;
            data[i][5] = r.capacity;
            data[i][6] = r.status;
        }

        JTable table = new JTable(data, cols);
        table.setFillsViewportHeight(true);
        table.setAutoCreateRowSorter(true);
        table.setRowHeight(24);
        JScrollPane sp = new JScrollPane(table);
        sp.setPreferredSize(new Dimension(900, 500));

        JDialog dlg = new JDialog(this, "All Events", true);
        dlg.setLayout(new BorderLayout());
        dlg.add(sp, BorderLayout.CENTER);
        JPanel bp = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton register = new JButton("Register");
        JButton close = new JButton("Close");
        close.addActionListener(e -> dlg.dispose());
        register.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(dlg, "Select an event first.", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            int modelRow = table.convertRowIndexToModel(row);
            int eventId = (Integer) data[modelRow][0];
            RegistrationDao rdao = new RegistrationDao();
            boolean ok = rdao.register(currentUser.getUserId(), eventId);
            if (ok) {
                JOptionPane.showMessageDialog(dlg, "Registered successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(dlg, "Event is full or registration failed.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        bp.add(register);
        bp.add(close);
        dlg.add(bp, BorderLayout.SOUTH);
        dlg.pack();
        dlg.setLocationRelativeTo(this);
        dlg.setVisible(true);
    }

    private void showMyRegistrations() {
        if (!Database.isConfigured()) {
            JOptionPane.showMessageDialog(this,
                "Database driver not found. Drop mysql-connector-j.jar into lib/ and set DB env vars to enable live registrations.",
                "Database Unavailable",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        RegistrationDao dao = new RegistrationDao();
        var rows = dao.listByUser(currentUser.getUserId());
        String[] cols = {"Reg ID", "Event ID", "Title", "Date", "Status"};
        Object[][] data = new Object[rows.size()][cols.length];
        for (int i = 0; i < rows.size(); i++) {
            var r = rows.get(i);
            data[i][0] = r.registrationId;
            data[i][1] = r.eventId;
            data[i][2] = r.title;
            data[i][3] = r.registrationDate;
            data[i][4] = r.status;
        }
        JTable table = new JTable(data, cols);
        table.setFillsViewportHeight(true);
        table.setAutoCreateRowSorter(true);
        table.setRowHeight(24);
        JScrollPane sp = new JScrollPane(table);
        sp.setPreferredSize(new Dimension(800, 450));
        JDialog dlg = new JDialog(this, "My Registrations", true);
        dlg.setLayout(new BorderLayout());
        dlg.add(sp, BorderLayout.CENTER);
        JButton cancelReg = new JButton("Cancel Registration");
        JButton close = new JButton("Close");
        JPanel bp = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        cancelReg.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(dlg, "Select a registration first.", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            int modelRow = table.convertRowIndexToModel(row);
            int regId = (Integer) data[modelRow][0];
            RegistrationDao rdao = new RegistrationDao();
            boolean ok = rdao.cancel(currentUser.getUserId(), regId);
            if (ok) {
                JOptionPane.showMessageDialog(dlg, "Registration cancelled.", "Done", JOptionPane.INFORMATION_MESSAGE);
                // refresh table
                var refreshed = rdao.listByUser(currentUser.getUserId());
                for (int i = 0; i < refreshed.size(); i++) {
                    var r = refreshed.get(i);
                    data[i][0] = r.registrationId;
                    data[i][1] = r.eventId;
                    data[i][2] = r.title;
                    data[i][3] = r.registrationDate;
                    data[i][4] = r.status;
                }
                table.repaint();
            } else {
                JOptionPane.showMessageDialog(dlg, "Unable to cancel.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        bp.add(cancelReg);
        bp.add(close);
        close.addActionListener(e -> dlg.dispose());
        dlg.add(bp, BorderLayout.SOUTH);
        dlg.pack();
        dlg.setLocationRelativeTo(this);
        dlg.setVisible(true);
    }

    private void showTokens() {
        if (!Database.isConfigured()) {
            JOptionPane.showMessageDialog(this, "Database not available.", "DB", JOptionPane.WARNING_MESSAGE);
            return;
        }
        EventTokenDao dao = new EventTokenDao();
        var rows = dao.listAll();
        String[] cols = {"ID", "Token", "Used", "Created", "By", "Use Count"};
        Object[][] data = new Object[rows.size()][cols.length];
        for (int i = 0; i < rows.size(); i++) {
            var r = rows.get(i);
            data[i][0] = r.id;
            data[i][1] = r.token;
            data[i][2] = r.isUsed;
            data[i][3] = r.createdAt;
            data[i][4] = r.createdBy;
            data[i][5] = r.useCount;
        }
        JTable table = new JTable(data, cols);
        table.setFillsViewportHeight(true);
        table.setAutoCreateRowSorter(true);
        table.setRowHeight(24);
        JDialog dlg = new JDialog(this, "Tokens", true);
        dlg.setLayout(new BorderLayout());
        dlg.add(new JScrollPane(table), BorderLayout.CENTER);
        JButton gen = new JButton("Generate");
        JButton close = new JButton("Close");
        JPanel bp = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bp.add(gen); bp.add(close);
        close.addActionListener(e -> dlg.dispose());
        gen.addActionListener(e -> {
            int id = dao.generate(currentUser.getUserId());
            if (id > 0) {
                JOptionPane.showMessageDialog(dlg, "New token created (ID=" + id + ")", "Done", JOptionPane.INFORMATION_MESSAGE);
                dlg.dispose(); showTokens();
            } else {
                JOptionPane.showMessageDialog(dlg, "Failed to generate token.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        dlg.add(bp, BorderLayout.SOUTH);
        dlg.setSize(800, 500);
        dlg.setLocationRelativeTo(this);
        dlg.setVisible(true);
    }

    private void showCreateEvent() {
        if (!Database.isConfigured()) { JOptionPane.showMessageDialog(this, "Database not available."); return; }

        JPanel p = new JPanel(new GridLayout(0, 2, 8, 8));
        JTextField title = new JTextField();
        JTextField desc = new JTextField();
        JTextField location = new JTextField();
        JTextField start = new JTextField("2025-12-01 10:00:00");
        JTextField end = new JTextField("2025-12-01 12:00:00");
        JTextField capacity = new JTextField("100");

        EventTokenDao tkDao = new EventTokenDao();
        Integer recentTokenId = tkDao.findRecentTokenIdUsedByUser(currentUser.getUserId());
        JTextField tokenField = new JTextField();
        tokenField.setToolTipText("Enter a valid token string. Required for your first event.");

        p.add(new JLabel("Title")); p.add(title);
        p.add(new JLabel("Description")); p.add(desc);
        p.add(new JLabel("Location")); p.add(location);
        p.add(new JLabel("Start (YYYY-MM-DD HH:MM:SS)")); p.add(start);
        p.add(new JLabel("End (YYYY-MM-DD HH:MM:SS)")); p.add(end);
        p.add(new JLabel("Capacity")); p.add(capacity);
        p.add(new JLabel("Token (required for first event)")); p.add(tokenField);

        int res = JOptionPane.showConfirmDialog(this, p, "Create Event", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            try {
                String t = title.getText().trim();
                String d = desc.getText().trim();
                String loc = location.getText().trim();
                String startStr = start.getText().trim();
                String endStr = end.getText().trim();
                int cap = Integer.parseInt(capacity.getText().trim());

                // Client-side validations to match DB constraints
                if (t.length() < 5) { JOptionPane.showMessageDialog(this, "Title must be at least 5 characters.", "Validation", JOptionPane.WARNING_MESSAGE); return; }
                if (d.length() < 10) { JOptionPane.showMessageDialog(this, "Description must be at least 10 characters.", "Validation", JOptionPane.WARNING_MESSAGE); return; }
                if (loc.isEmpty()) { JOptionPane.showMessageDialog(this, "Location is required.", "Validation", JOptionPane.WARNING_MESSAGE); return; }
                if (cap <= 0) { JOptionPane.showMessageDialog(this, "Capacity must be greater than 0.", "Validation", JOptionPane.WARNING_MESSAGE); return; }

                java.sql.Timestamp st = java.sql.Timestamp.valueOf(startStr);
                java.sql.Timestamp en = java.sql.Timestamp.valueOf(endStr);
                if (!en.after(st)) { JOptionPane.showMessageDialog(this, "End time must be after start time.", "Validation", JOptionPane.WARNING_MESSAGE); return; }

                int tokenIdToUse;
                String tokenStr = tokenField.getText().trim();
                if (recentTokenId != null && tokenStr.isEmpty()) {
                    tokenIdToUse = recentTokenId;
                } else {
                    if (tokenStr.isEmpty() && !currentUser.isVerified()) {
                        JOptionPane.showMessageDialog(this, "Token is required for your first event.", "Token Required", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    if (!tokenStr.isEmpty()) {
                        Integer lookedUp = tkDao.findIdByTokenString(tokenStr);
                        if (lookedUp == null) {
                            JOptionPane.showMessageDialog(this, "Invalid token. Please contact support to obtain a valid token.", "Invalid Token", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        tokenIdToUse = lookedUp;
                    } else {
                        tokenIdToUse = recentTokenId; // verified and empty input: reuse last
                    }
                }

                EventDao dao = new EventDao();
                int id = dao.create(
                    t,
                    d,
                    loc,
                    st,
                    en,
                    cap,
                    currentUser.getUserId(),
                    tokenIdToUse
                );
                if (id > 0) {
                    try { tkDao.incrementUse(tokenIdToUse); } catch (Exception ignore) {}
                    if (!currentUser.isVerified()) {
                        currentUser.setVerified(true);
                        try { new UserDao().markVerified(currentUser.getUserId()); } catch (Exception ignore) {}
                        if (userService != null) {
                            userService.verifyUser(currentUser.getUserId());
                        }
                    }
                    JOptionPane.showMessageDialog(this, "Event created with ID=" + id);
                    refreshFeed();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to create event", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Admin Guide removed as requested

    private void showCancelEvent() {
        if (!Database.isConfigured()) { JOptionPane.showMessageDialog(this, "Database not available."); return; }
        String idStr = JOptionPane.showInputDialog(this, "Enter Event ID to cancel:");
        if (idStr == null || idStr.isEmpty()) return;
        try {
            int id = Integer.parseInt(idStr.trim());
            EventDao dao = new EventDao();
            boolean ok = dao.cancel(id);
            if (ok) JOptionPane.showMessageDialog(this, "Event cancelled.");
            else JOptionPane.showMessageDialog(this, "Failed to cancel.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid ID.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showManageEvents() {
        if (!Database.isConfigured()) { JOptionPane.showMessageDialog(this, "Database not available."); return; }
        EventDao dao = new EventDao();
        var rows = dao.listAll();
        String[] cols = {"ID", "Title", "Location", "Start", "End", "Capacity", "Status"};
        Object[][] data = new Object[rows.size()][cols.length];
        for (int i = 0; i < rows.size(); i++) {
            var r = rows.get(i);
            data[i][0] = r.id;
            data[i][1] = r.title;
            data[i][2] = r.location;
            data[i][3] = r.start;
            data[i][4] = r.end;
            data[i][5] = r.capacity;
            data[i][6] = r.status;
        }
        JTable table = new JTable(data, cols);
        table.setFillsViewportHeight(true);
        table.setAutoCreateRowSorter(true);
        table.setRowHeight(24);
        JDialog dlg = new JDialog(this, "Manage Events", true);
        dlg.setLayout(new BorderLayout());
        dlg.add(new JScrollPane(table), BorderLayout.CENTER);
        JButton refresh = new JButton("Refresh");
        JButton closeBtn = new JButton("Close Reg");
        JButton finishBtn = new JButton("Finish");
        JButton cancelBtn = new JButton("Cancel");
        JButton close = new JButton("Close");
        JPanel bp = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bp.add(refresh); bp.add(closeBtn); bp.add(finishBtn); bp.add(cancelBtn); bp.add(close);
        close.addActionListener(e -> dlg.dispose());
        java.util.function.Consumer<String> applyStatus = (status) -> {
            int row = table.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(dlg, "Select an event first."); return; }
            int modelRow = table.convertRowIndexToModel(row);
            int eventId = (Integer) data[modelRow][0];
            boolean ok = "cancelled".equals(status) ? dao.cancel(eventId) : dao.updateStatus(eventId, status);
            if (ok) { JOptionPane.showMessageDialog(dlg, "Updated."); dlg.dispose(); showManageEvents(); }
            else JOptionPane.showMessageDialog(dlg, "Failed.", "Error", JOptionPane.ERROR_MESSAGE);
        };
        closeBtn.addActionListener(e -> applyStatus.accept("closed"));
        finishBtn.addActionListener(e -> applyStatus.accept("finished"));
        cancelBtn.addActionListener(e -> applyStatus.accept("cancelled"));
        refresh.addActionListener(e -> { dlg.dispose(); showManageEvents(); });
        dlg.add(bp, BorderLayout.SOUTH);
        dlg.setSize(1000, 550);
        dlg.setLocationRelativeTo(this);
        dlg.setVisible(true);
    }

    private void showManageUsers() {
        if (!Database.isConfigured()) { JOptionPane.showMessageDialog(this, "Database not available."); return; }
        UserDao dao = new UserDao();
        var users = dao.listAll();
        String[] cols = {"ID", "Name", "Email", "Role", "Verified"};
        Object[][] data = new Object[users.size()][cols.length];
        for (int i = 0; i < users.size(); i++) {
            var u = users.get(i);
            data[i][0] = u.getUserId();
            data[i][1] = u.getFullName();
            data[i][2] = u.getEmailRaw();
            data[i][3] = u.getRole();
            data[i][4] = u.isVerified();
        }
        JTable table = new JTable(data, cols);
        table.setFillsViewportHeight(true);
        table.setAutoCreateRowSorter(true);
        table.setRowHeight(24);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JDialog dlg = new JDialog(this, "Manage Users", true);
        dlg.setLayout(new BorderLayout());
        dlg.getContentPane().setBackground(Theme.BG_APP);
        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(null);
        dlg.add(sp, BorderLayout.CENTER);
        // Header toolbar with clear call-to-action
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Theme.BG_APP);
        JLabel hint = new JLabel("Select a user and click Promote Selected");
        hint.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        JPanel headerRight = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        headerRight.setBackground(Theme.BG_APP);
        JButton promoteTop = new JButton("Promote Selected");
        Theme.stylePrimaryButton(promoteTop);
        headerRight.add(promoteTop);
        header.add(hint, BorderLayout.WEST);
        header.add(headerRight, BorderLayout.EAST);
        dlg.add(header, BorderLayout.NORTH);

        JButton promote = new JButton("Promote to Admin");
        JButton close = new JButton("Close");
        Theme.stylePrimaryButton(promote);
        Theme.styleSecondaryButton(close);
        JPanel bp = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bp.setBackground(Theme.BG_APP);
        bp.add(promote); bp.add(close);
        close.addActionListener(e -> dlg.dispose());
        Runnable doPromote = () -> {
            int row = table.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(dlg, "Select a user first."); return; }
            int modelRow = table.convertRowIndexToModel(row);
            int uid = (Integer) data[modelRow][0];
            String uname = String.valueOf(data[modelRow][1]);
            Object role = data[modelRow][3];
            if (role != null && role.toString().equalsIgnoreCase("ADMIN")) {
                JOptionPane.showMessageDialog(dlg, "User is already an admin.", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(dlg, "Promote '" + uname + "' to Admin?", "Confirm", JOptionPane.OK_CANCEL_OPTION);
            if (confirm != JOptionPane.OK_OPTION) return;
            boolean ok = dao.promoteToAdmin(uid);
            if (ok) { JOptionPane.showMessageDialog(dlg, "User promoted."); dlg.dispose(); showManageUsers(); }
            else JOptionPane.showMessageDialog(dlg, "Failed to promote.", "Error", JOptionPane.ERROR_MESSAGE);
        };
        promote.addActionListener(e -> doPromote.run());
        promoteTop.addActionListener(e -> doPromote.run());

        // Enable/disable button based on selection
        promote.setEnabled(false);
        table.getSelectionModel().addListSelectionListener(e -> promote.setEnabled(table.getSelectedRow() >= 0));

        // Left-click selection updates header CTA text; no right-click menu
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent e) {
                int r = table.getSelectedRow();
                if (r >= 0) {
                    int mr = table.convertRowIndexToModel(r);
                    String uname = String.valueOf(data[mr][1]);
                    promoteTop.setText("Promote '" + uname + "'");
                }
            }
        });

    dlg.setMinimumSize(new Dimension(720, 420));
        dlg.setSize(900, 500);
        dlg.setLocationRelativeTo(this);
        dlg.setVisible(true);
    }

    private void showAbout() {
        JOptionPane.showMessageDialog(this, """
                                            Event Management System
                                            Version 1.0
                                            
                                            A comprehensive system for managing events and registrations.
                                            Built with Java Swing for desktop GUI experience.""",
            "About",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void showMyEvents() {
        if (!Database.isConfigured()) {
            JOptionPane.showMessageDialog(this,
                "Database not available.",
                "DB",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        EventDao dao = new EventDao();
        var rows = dao.listByOrganizer(currentUser.getUserId());
        String[] cols = {"ID", "Title", "Start", "End", "Capacity", "Status"};
        Object[][] data = new Object[rows.size()][cols.length];
        for (int i = 0; i < rows.size(); i++) {
            var r = rows.get(i);
            data[i][0] = r.id;
            data[i][1] = r.title;
            data[i][2] = r.start;
            data[i][3] = r.end;
            data[i][4] = r.capacity;
            data[i][5] = r.status;
        }
        JTable table = new JTable(data, cols);
        table.setFillsViewportHeight(true);
        table.setAutoCreateRowSorter(true);
        table.setRowHeight(24);
        JDialog dlg = new JDialog(this, "My Events (Manage)", true);
        dlg.setLayout(new BorderLayout());
        dlg.add(new JScrollPane(table), BorderLayout.CENTER);

        JButton refresh = new JButton("Refresh");
        JButton closeReg = new JButton("Close Reg");
        JButton finish = new JButton("Finish");
        JButton cancel = new JButton("Cancel Event");
        JButton viewRegs = new JButton("Registrations...");
        JButton close = new JButton("Close");
        Theme.styleInfoButton(viewRegs);
        Theme.styleSuccessButton(refresh);
        Theme.styleSecondaryButton(closeReg);
        Theme.stylePrimaryButton(finish);
        Theme.styleDangerButton(cancel);

        JPanel bp = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bp.add(viewRegs); bp.add(refresh); bp.add(closeReg); bp.add(finish); bp.add(cancel); bp.add(close);
        close.addActionListener(e -> dlg.dispose());
        java.util.function.Consumer<String> apply = (status) -> {
            int row = table.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(dlg, "Select an event first."); return; }
            int modelRow = table.convertRowIndexToModel(row);
            int id = (Integer) data[modelRow][0];
            boolean ok = "cancelled".equals(status) ? dao.cancel(id) : dao.updateStatus(id, status);
            if (ok) { JOptionPane.showMessageDialog(dlg, "Updated."); dlg.dispose(); showMyEvents(); }
            else JOptionPane.showMessageDialog(dlg, "Failed.", "Error", JOptionPane.ERROR_MESSAGE);
        };
        closeReg.addActionListener(e -> apply.accept("closed"));
        finish.addActionListener(e -> apply.accept("finished"));
        cancel.addActionListener(e -> apply.accept("cancelled"));
        refresh.addActionListener(e -> { dlg.dispose(); showMyEvents(); });
        viewRegs.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(dlg, "Select an event first."); return; }
            int modelRow = table.convertRowIndexToModel(row);
            int id = (Integer) data[modelRow][0];
            showRegistrationsForEvent(id);
        });
        dlg.add(bp, BorderLayout.SOUTH);
        dlg.setSize(900, 520);
        dlg.setLocationRelativeTo(this);
        dlg.setVisible(true);
    }

    private void showRegistrationsForEvent(int eventId) {
        RegistrationDao rdao = new RegistrationDao();
        var rows = rdao.listByEvent(eventId);
        String[] cols = {"Reg ID", "User", "Date", "Status"};
        Object[][] data = new Object[rows.size()][cols.length];
        for (int i = 0; i < rows.size(); i++) {
            var r = rows.get(i);
            data[i][0] = r.registrationId;
            data[i][1] = r.title; // title is event title; user lookup omitted for simplicity
            data[i][2] = r.registrationDate;
            data[i][3] = r.status;
        }
        JTable table = new JTable(data, cols);
        table.setFillsViewportHeight(true);
        table.setRowHeight(24);
        JDialog dlg = new JDialog(this, "Registrations for Event " + eventId, true);
        dlg.setLayout(new BorderLayout());
        dlg.add(new JScrollPane(table), BorderLayout.CENTER);
        JButton cancelReg = new JButton("Cancel Registration");
        JButton close = new JButton("Close");
        JPanel bp = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bp.add(cancelReg); bp.add(close);
        close.addActionListener(e -> dlg.dispose());
        cancelReg.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(dlg, "Select a registration."); return; }
            int modelRow = table.convertRowIndexToModel(row);
            int regId = (Integer) data[modelRow][0];
            boolean ok = rdao.cancelRegistrationByOrganizer(eventId, regId);
            if (ok) { JOptionPane.showMessageDialog(dlg, "Registration cancelled."); dlg.dispose(); showRegistrationsForEvent(eventId); }
            else JOptionPane.showMessageDialog(dlg, "Failed.", "Error", JOptionPane.ERROR_MESSAGE);
        });
        dlg.add(bp, BorderLayout.SOUTH);
        dlg.setSize(700, 480);
        dlg.setLocationRelativeTo(this);
        dlg.setVisible(true);
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
