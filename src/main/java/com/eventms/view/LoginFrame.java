package com.eventms.view;

import com.eventms.model.User;
import com.eventms.service.UserService;
import com.eventms.view.ui.Theme;
import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

/**
 * Login GUI window for the Event Management System
 * Provides user authentication interface with email and password fields
 */
public class LoginFrame extends JFrame {
    @SuppressWarnings("FieldMayBeFinal")
    private UserService userService;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton signUpButton;
    private JButton forgotPasswordButton;
    private JLabel statusLabel;
    private String currentSessionId;

    public LoginFrame(UserService userService) {
        this.userService = userService;
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        setupWindow();
    }

    private void initializeComponents() {
    // Install theme once per app instance (idempotent)
    Theme.install();
        // Create input fields
        emailField = new JTextField(20);
        passwordField = new JPasswordField(20);
        
        // Add placeholder text
        addPlaceholder(emailField, "Enter your email address");
        addPlaceholder(passwordField, "Enter your password");
        
        // Add helpful tooltips
        emailField.setToolTipText("Enter your registered email address");
        passwordField.setToolTipText("Enter your password (minimum 6 characters)");
        
        // Create buttons
        loginButton = new JButton("Login");
        signUpButton = new JButton("Sign Up");
        forgotPasswordButton = new JButton("Forgot Password?");
        
        // Create status label
        statusLabel = new JLabel(" ");
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
    // Style components
    styleComponents();
    }

    private void styleComponents() {
    // Buttons
    Theme.stylePrimaryButton(loginButton);
    Theme.styleSuccessButton(signUpButton);
    Theme.styleLinkButton(forgotPasswordButton);

    // Fields
    Theme.styleTextField(emailField);
    Theme.styleTextField(passwordField);

    // Status label
    statusLabel.setFont(Theme.fontPlain(13));
    statusLabel.setForeground(Theme.TEXT_SECONDARY);
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Create a responsive main panel that adapts to window size
        JPanel outerPanel = new JPanel(new BorderLayout());
    outerPanel.setBackground(Theme.BG_APP);
        
        // Main panel with flexible padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
    mainPanel.setBackground(Theme.BG_APP);
        
        // Dynamic padding that adjusts to window size
        int padding = 30;
        mainPanel.setBorder(BorderFactory.createEmptyBorder(padding, padding, padding, padding));
        
        // Title section
    JPanel titlePanel = new JPanel();
    titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
    titlePanel.setBackground(Theme.BG_APP);
        
    JLabel titleLabel = Theme.h1("Event Management System");
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Please login to continue");
    subtitleLabel.setFont(Theme.fontPlain(15));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    subtitleLabel.setForeground(Theme.TEXT_SECONDARY);
        
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        titlePanel.add(subtitleLabel);
        
        // Form panel with flexible sizing
        JPanel formPanel = new JPanel(new GridBagLayout());
    // Minimal look: flat, no card border
    formPanel.setBackground(Theme.BG_APP);
    formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        formPanel.setMaximumSize(new Dimension(600, 150)); // Limit maximum form size
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Email field
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.3;
        formPanel.add(new JLabel("Email:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.7;
        formPanel.add(emailField, gbc);
        
        // Password field
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.3;
        formPanel.add(new JLabel("Password:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.7;
        formPanel.add(passwordField, gbc);
        
        // Button panel with flexible layout
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
    buttonPanel.setBackground(Theme.BG_APP);
        buttonPanel.add(loginButton);
        buttonPanel.add(signUpButton);
        
        // Forgot password panel
        JPanel forgotPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    forgotPanel.setBackground(Theme.BG_APP);
        forgotPanel.add(forgotPasswordButton);
        
        // Status panel with flexible sizing
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    statusPanel.setBackground(Theme.BG_APP);
        statusPanel.add(statusLabel);
        
        // Add components to main panel with flexible spacing
        mainPanel.add(Box.createVerticalGlue()); // Top spacing
        mainPanel.add(titlePanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        mainPanel.add(formPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(buttonPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(forgotPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(statusPanel);
        mainPanel.add(Box.createVerticalGlue()); // Bottom spacing
        
        outerPanel.add(mainPanel, BorderLayout.CENTER);
        add(outerPanel, BorderLayout.CENTER);
    }

    private void setupEventHandlers() {
        // Login button action
        loginButton.addActionListener((ActionEvent e) -> {
            performLogin();
        });
        
        // Sign up button action
        signUpButton.addActionListener((ActionEvent e) -> {
            openSignUpWindow();
        });
        
        // Forgot password button action
        forgotPasswordButton.addActionListener((ActionEvent e) -> {
            handleForgotPassword();
        });
        
        // Enter key on password field
        passwordField.addActionListener((ActionEvent e) -> {
            performLogin();
        });
        
        // Enter key on email field
        emailField.addActionListener((ActionEvent e) -> {
            passwordField.requestFocus();
        });
        
        // Real-time validation as user types
        emailField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) { validateLoginInput(); }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) { validateLoginInput(); }
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) { validateLoginInput(); }
        });
        
        passwordField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) { validateLoginInput(); }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) { validateLoginInput(); }
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) { validateLoginInput(); }
        });
    }
    
    private void validateLoginInput() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        // Check if email is placeholder text
        boolean isEmailEmpty = email.isEmpty() || email.equals("Enter your email address");
        boolean isPasswordEmpty = password.isEmpty() || password.equals("Enter your password");
        
    // Minimal validation UI: no border color changes, only status text
        
        // Clear status if both fields have content and are valid
        if (!isEmailEmpty && !isPasswordEmpty) {
            if (email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$") && password.length() >= 6) {
                statusLabel.setText(" "); // Clear any error messages
            }
        }
    }

    private void setupWindow() {
        setTitle("Event Management System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true); // Allow resizing
        
    // Start maximized to fill screen and avoid cramped layout
    setSize(900, 700);
    setMinimumSize(new Dimension(640, 480));
    setLocationRelativeTo(null); // Center before maximize
    setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        // Add component listener for responsive behavior
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                // Ensure components scale properly when window is resized
                revalidate();
                repaint();
            }
        });
        
    // Set app icon
    try { setIconImage(com.eventms.view.ui.Theme.getAppIcon()); } catch (Exception ignore) {}
    }

    private void performLogin() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        // Check if fields contain placeholder text
        boolean isEmailEmpty = email.isEmpty() || email.equals("Enter your email address");
        boolean isPasswordEmpty = password.isEmpty() || password.equals("Enter your password");
        
        // Enhanced input validation with specific error messages
        if (isEmailEmpty) {
            showStatus("Please enter your email address", Color.RED);
            emailField.requestFocus();
            return;
        }
        
        // Email format validation
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            showStatus("Please enter a valid email address", Color.RED);
            emailField.requestFocus();
            return;
        }
        
        if (isPasswordEmpty) {
            showStatus("Please enter your password", Color.RED);
            passwordField.requestFocus();
            return;
        }
        
        if (password.length() < 6) {
            showStatus("Password must be at least 6 characters long", Color.RED);
            passwordField.requestFocus();
            return;
        }
        
        // Disable login button during authentication
        loginButton.setEnabled(false);
        loginButton.setText("Logging in...");
        showStatus("Authenticating...", Color.BLUE);
        
        // Perform login in a separate thread to avoid blocking UI
        SwingUtilities.invokeLater(() -> {
            try {
                currentSessionId = userService.login(email, password);
                
                if (currentSessionId != null) {
                    User currentUser = userService.getCurrentUser(currentSessionId);
                    showStatus("Login successful! Welcome, " + currentUser.getFullName(), Color.GREEN);
                    
                    // Wait a moment to show success message
                    Timer timer = new Timer(1500, e -> openMainApplication(currentUser));
                    timer.setRepeats(false);
                    timer.start();
                } else {
                    showStatus("Invalid email or password. Please try again.", Color.RED);
                    passwordField.setText("");
                    passwordField.requestFocus();
                }
            } catch (Exception ex) {
                showStatus("Login failed: " + ex.getMessage(), Color.RED);
                passwordField.setText("");
            } finally {
                loginButton.setEnabled(true);
                loginButton.setText("Login");
            }
        });
    }

    private void openSignUpWindow() {
        SignUpFrame signUpFrame = new SignUpFrame(userService, this);
        signUpFrame.setVisible(true);
        this.setVisible(false);
    }

    private void handleForgotPassword() {
        String email = JOptionPane.showInputDialog(
            this,
            "Enter your email address to reset your password:",
            "Forgot Password",
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (email != null && !email.trim().isEmpty()) {
            String resetToken = userService.initiatePasswordReset(email.trim());
            if (resetToken != null) {
                JOptionPane.showMessageDialog(
                    this,
                    """
                    Password reset instructions have been sent to your email.
                    Reset Token: """ + resetToken + "\n" +
                    "(In a real application, this would be sent via email)",
                    "Password Reset",
                    JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "No account found with that email address.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    private void openMainApplication(User user) {
        // Create and show the main application window
        // For now, we'll pass null for EventService and RegistrationService
        // These can be initialized properly later when those services are needed
    MainApplicationFrame mainFrame = new MainApplicationFrame(user, userService, null, null);
        mainFrame.setVisible(true);
        this.dispose();
    }

    private void showStatus(String message, Color color) {
        statusLabel.setText(message);
        statusLabel.setForeground(color);
        
        // Make error messages more prominent
        if (color == Color.RED) {
            statusLabel.setFont(new Font("Arial", Font.BOLD, 14));
        } else if (color == Color.GREEN) {
            statusLabel.setFont(new Font("Arial", Font.BOLD, 14));
        } else {
            statusLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        }
    }

    public void showLoginSuccess(String userEmail) {
        emailField.setText(userEmail);
        showStatus("Account created successfully! Please login.", Color.GREEN);
    }

    /**
     * Adds placeholder text to a text field that appears when empty and disappears when focused
     */
    private void addPlaceholder(JTextField textField, String placeholder) {
        textField.setText(placeholder);
        textField.setForeground(Color.GRAY);
        
        textField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (textField.getText().equals(placeholder)) {
                    textField.setText("");
                    textField.setForeground(Color.BLACK);
                }
            }
            
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setText(placeholder);
                    textField.setForeground(Color.GRAY);
                }
            }
        });
    }
    
    /**
     * Adds placeholder text to a password field
     */
    private void addPlaceholder(JPasswordField passwordField, String placeholder) {
        passwordField.setText(placeholder);
        passwordField.setForeground(Color.GRAY);
        passwordField.setEchoChar((char) 0); // Show placeholder text clearly
        
        passwordField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                String currentText = new String(passwordField.getPassword());
                if (currentText.equals(placeholder)) {
                    passwordField.setText("");
                    passwordField.setForeground(Color.BLACK);
                    passwordField.setEchoChar('*'); // Enable password masking
                }
            }
            
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (passwordField.getPassword().length == 0) {
                    passwordField.setText(placeholder);
                    passwordField.setForeground(Color.GRAY);
                    passwordField.setEchoChar((char) 0); // Show placeholder text
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UserService userService = new UserService();
            new LoginFrame(userService).setVisible(true);
        });
    }
}
