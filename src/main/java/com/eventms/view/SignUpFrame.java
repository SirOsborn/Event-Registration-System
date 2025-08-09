package com.eventms.view;

import com.eventms.model.User;
import com.eventms.service.UserService;
import com.eventms.view.ui.Theme;
import java.awt.*;
import java.util.Calendar;
import javax.swing.*;

/**
 * Sign Up GUI window for the Event Management System
 * Provides user registration interface with comprehensive form validation
 */
public class SignUpFrame extends JFrame {
    @SuppressWarnings("FieldMayBeFinal")
    private UserService userService;
    @SuppressWarnings("FieldMayBeFinal")
    private LoginFrame parentFrame;
    
    // Form fields
    private JTextField fullNameField;
    private JTextField emailField;
    private JTextField contactNumberField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextField occupationField;
    private JComboBox<String> dayComboBox;
    private JComboBox<String> monthComboBox;
    private JComboBox<String> yearComboBox;
    private JComboBox<String> genderComboBox;
    private JCheckBox termsCheckBox;
    
    // Buttons
    private JButton signUpButton;
    private JButton backToLoginButton;
    private JLabel statusLabel;

    public SignUpFrame(UserService userService, LoginFrame parentFrame) {
        this.userService = userService;
        this.parentFrame = parentFrame;
    Theme.install();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        setupWindow();
    }

    private void initializeComponents() {
        // Create input fields
        fullNameField = new JTextField(20);
        emailField = new JTextField(20);
        contactNumberField = new JTextField(20);
        passwordField = new JPasswordField(20);
        confirmPasswordField = new JPasswordField(20);
        occupationField = new JTextField(20);
        
        // Add helpful tooltips for better user experience
        fullNameField.setToolTipText("Enter your full name (letters and spaces only, 2-50 characters)");
        emailField.setToolTipText("Enter a valid email address (e.g., user@example.com)");
        contactNumberField.setToolTipText("Enter your phone number (9-15 digits only)");
        passwordField.setToolTipText("Enter a password (minimum 6 characters, must contain at least one letter)");
        confirmPasswordField.setToolTipText("Re-enter your password to confirm");
        occupationField.setToolTipText("Enter your occupation (letters and spaces only, 2-30 characters)");
        
        // Add placeholder text to input fields
        addPlaceholder(fullNameField, "Enter your full name");
        addPlaceholder(emailField, "Enter your email address");
        addPlaceholder(contactNumberField, "Enter your phone number");
        addPlaceholder(passwordField, "Enter your password");
        addPlaceholder(confirmPasswordField, "Confirm your password");
        addPlaceholder(occupationField, "Enter your occupation");
        
        // Create date of birth components
        dayComboBox = new JComboBox<>();
        monthComboBox = new JComboBox<>();
        yearComboBox = new JComboBox<>();
        populateDateComboBoxes();
        
        // Create gender combo box
        genderComboBox = new JComboBox<>(new String[]{"Select Gender", "Male", "Female", "Other"});
        
        // Create terms checkbox
        termsCheckBox = new JCheckBox("I agree to the Terms and Conditions");
        
        // Create buttons
        signUpButton = new JButton("Create Account");
        backToLoginButton = new JButton("Back to Login");
        
        // Create status label
        statusLabel = new JLabel(" ");
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
    // Style components
    styleComponents();
    }

    private void populateDateComboBoxes() {
        // Populate day combo box
        dayComboBox.addItem("Day");
        for (int day = 1; day <= 31; day++) {
            dayComboBox.addItem(String.format("%02d", day));
        }
        
        // Populate month combo box
        String[] months = {"Month", "01", "02", "03", "04", "05", "06", 
                          "07", "08", "09", "10", "11", "12"};
        for (String month : months) {
            monthComboBox.addItem(month);
        }
        
        // Populate year combo box
        yearComboBox.addItem("Year");
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int year = currentYear - 100; year <= currentYear - 13; year++) {
            yearComboBox.addItem(String.valueOf(year));
        }
    }

    private void styleComponents() {
    // Buttons
    Theme.styleSuccessButton(signUpButton);
    Theme.styleSecondaryButton(backToLoginButton);

    // Fields
    Theme.styleTextField(fullNameField);
    Theme.styleTextField(emailField);
    Theme.styleTextField(contactNumberField);
    Theme.styleTextField(passwordField);
    Theme.styleTextField(confirmPasswordField);
    Theme.styleTextField(occupationField);

    // Combo boxes and checkbox
    Theme.styleComboBox(dayComboBox);
    Theme.styleComboBox(monthComboBox);
    Theme.styleComboBox(yearComboBox);
    Theme.styleComboBox(genderComboBox);
    Theme.styleCheckBox(termsCheckBox);

    // Status label
    statusLabel.setFont(Theme.fontPlain(13));
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Create responsive outer panel
        JPanel outerPanel = new JPanel(new BorderLayout());
    outerPanel.setBackground(Theme.BG_APP);
        
        // Main panel with flexible padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
    mainPanel.setBackground(Theme.BG_APP);
        
        // Dynamic padding that adjusts to window size
        int padding = 20;
        mainPanel.setBorder(BorderFactory.createEmptyBorder(padding, padding, padding, padding));
        
        // Title section
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
    titlePanel.setBackground(Theme.BG_APP);
        
    JLabel titleLabel = Theme.h1("Create New Account");
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Please fill in all required fields");
    subtitleLabel.setFont(Theme.fontPlain(15));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    subtitleLabel.setForeground(Theme.TEXT_SECONDARY);
        
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        titlePanel.add(subtitleLabel);
        
        // Create form panel with flexible sizing
        JPanel formPanel = createFormPanel();
    formPanel.setMaximumSize(new Dimension(600, Integer.MAX_VALUE)); // Limit max width
    // Minimal: flat, no card border
    formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    formPanel.setBackground(Theme.BG_APP);
        
        // Button panel with flexible layout
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
    buttonPanel.setBackground(Theme.BG_APP);
        buttonPanel.add(signUpButton);
        buttonPanel.add(backToLoginButton);
        
        // Status panel
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    statusPanel.setBackground(Theme.BG_APP);
        statusPanel.add(statusLabel);
        
        // Add components to main panel with flexible spacing
        mainPanel.add(Box.createVerticalStrut(10)); // Top spacing
        mainPanel.add(titlePanel);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(formPanel);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(buttonPanel);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(statusPanel);
        mainPanel.add(Box.createVerticalGlue()); // Bottom flexible spacing
        
    // Flat modern: no scrollbars
    outerPanel.add(mainPanel, BorderLayout.CENTER);
        add(outerPanel, BorderLayout.CENTER);
    }

    private JPanel createFormPanel() {
    JPanel formPanel = new JPanel(new GridBagLayout());
    formPanel.setBackground(Theme.BG_APP);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        
        int row = 0;
        
        // Full Name
        addFormField(formPanel, gbc, "Full Name *:", fullNameField, row++);
        
        // Email
        addFormField(formPanel, gbc, "Email *:", emailField, row++);
        
        // Contact Number
        addFormField(formPanel, gbc, "Contact Number *:", contactNumberField, row++);
        
        // Password
        addFormField(formPanel, gbc, "Password *:", passwordField, row++);
        
        // Confirm Password
        addFormField(formPanel, gbc, "Confirm Password *:", confirmPasswordField, row++);
        
        // Occupation
        addFormField(formPanel, gbc, "Occupation *:", occupationField, row++);
        
        // Date of Birth
        gbc.gridx = 0; gbc.gridy = row;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Date of Birth *:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = row;
        gbc.anchor = GridBagConstraints.WEST;
    JPanel dobPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
    dobPanel.setBackground(Theme.BG_APP);
        dobPanel.add(dayComboBox);
        dobPanel.add(monthComboBox);
        dobPanel.add(yearComboBox);
        formPanel.add(dobPanel, gbc);
        row++;
        
        // Gender
        addFormField(formPanel, gbc, "Gender *:", genderComboBox, row++);
        
        // Terms and Conditions
        gbc.gridx = 0; gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(termsCheckBox, gbc);
        
        return formPanel;
    }

    private void addFormField(JPanel panel, GridBagConstraints gbc, String labelText, JComponent field, int row) {
        gbc.gridx = 0; gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel(labelText), gbc);
        
        gbc.gridx = 1; gbc.gridy = row;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(field, gbc);
    }

    private void setupEventHandlers() {
        // Sign up button action
        signUpButton.addActionListener(e -> performSignUp());
        
        // Back to login button action
        backToLoginButton.addActionListener(e -> goBackToLogin());
        
        // Enter key handling for form submission
        confirmPasswordField.addActionListener(e -> performSignUp());
        
        // Real-time validation as user types
        emailField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) { validateEmailField(); }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) { validateEmailField(); }
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) { validateEmailField(); }
        });
        
        passwordField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) { validatePasswordField(); }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) { validatePasswordField(); }
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) { validatePasswordField(); }
        });
        
        confirmPasswordField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) { validatePasswordConfirmation(); }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) { validatePasswordConfirmation(); }
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) { validatePasswordConfirmation(); }
        });
        
        contactNumberField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) { validateContactField(); }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) { validateContactField(); }
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) { validateContactField(); }
        });
    }
    
    private void validateEmailField() {
        String email = emailField.getText().trim();
        if (!email.isEmpty()) {
            if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
                showStatus("Invalid email format", Color.RED);
            } else {
                showStatus(" ", Color.GRAY);
            }
        }
    }
    
    private void validatePasswordField() {
        String password = new String(passwordField.getPassword());
        if (!password.isEmpty()) {
            if (password.length() < 6) {
                showStatus("Password too short (minimum 6 characters)", Color.RED);
            } else if (!password.matches(".*[a-zA-Z].*")) {
                showStatus("Password must contain at least one letter", Color.RED);
            } else {
                showStatus(" ", Color.GRAY);
            }
        }
    }
    
    private void validatePasswordConfirmation() {
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        if (!confirmPassword.isEmpty()) {
            if (!password.equals(confirmPassword)) {
                showStatus("Passwords do not match", Color.RED);
            } else if (!password.isEmpty()) {
                showStatus(" ", Color.GRAY);
            }
        }
    }
    
    private void validateContactField() {
        String contact = contactNumberField.getText().trim();
        if (!contact.isEmpty()) {
            if (!contact.matches("^[0-9]{9,15}$")) {
                showStatus("Contact number must be 9-15 digits only", Color.RED);
            } else {
                showStatus(" ", Color.GRAY);
            }
        }
    }

    private void setupWindow() {
        setTitle("Event Management System - Sign Up");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setResizable(true);
        
        // Set initial size and responsive dimensions
    setSize(900, 720);
    setMinimumSize(new Dimension(640, 560));
    setLocationRelativeTo(parentFrame);
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
        
        // Handle window closing
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                goBackToLogin();
            }
        });

    // Set app icon
    try { setIconImage(com.eventms.view.ui.Theme.getAppIcon()); } catch (Exception ignore) {}
    }

    private void performSignUp() {
        // Validate form
        String validationError = validateForm();
        if (validationError != null) {
            showStatus(validationError, Color.RED);
            return;
        }
        
        // Disable sign up button during registration
        signUpButton.setEnabled(false);
        signUpButton.setText("Creating Account...");
        showStatus("Creating your account...", Color.BLUE);
        
        // Get form data
        String fullName = fullNameField.getText().trim();
        String email = emailField.getText().trim();
        String contactNumber = contactNumberField.getText().trim();
        String password = new String(passwordField.getPassword());
        String occupation = occupationField.getText().trim();
        String dob = getDateOfBirth();
        char gender = getSelectedGender();
        
        // Perform registration in a separate thread
        SwingUtilities.invokeLater(() -> {
            try {
                User newUser = userService.createUser(fullName, email, contactNumber, password, 
                                                    "EN", occupation, dob, gender);
                
                if (newUser != null) {
                    showStatus("Account created successfully!", Color.GREEN);
                    
                    // Show success message and redirect to login
                    JOptionPane.showMessageDialog(this, """
                                                        Your account has been created successfully!\n
                                                        You can now log in with your email and password.""",
                        "Registration Successful",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                    
                    // Go back to login with pre-filled email
                    parentFrame.showLoginSuccess(email);
                    parentFrame.setVisible(true);
                    this.dispose();
                } else {
                    showStatus("Failed to create account. Please try again.", Color.RED);
                }
            } catch (IllegalArgumentException ex) {
                showStatus("Registration failed: " + ex.getMessage(), Color.RED);
            } catch (HeadlessException ex) {
                showStatus("An unexpected error occurred. Please try again.", Color.RED);
            } finally {
                signUpButton.setEnabled(true);
                signUpButton.setText("Create Account");
            }
        });
    }

    private String validateForm() {
        // Check required fields with enhanced error messages
        String fullName = fullNameField.getText().trim();
        boolean isFullNameEmpty = fullName.isEmpty() || fullName.equals("Enter your full name");
        if (isFullNameEmpty) {
            fullNameField.requestFocus();
            return "Please enter your full name";
        }
        
        // Validate full name format (only letters and spaces)
        if (!fullName.matches("^[a-zA-Z\\s]{2,50}$")) {
            fullNameField.requestFocus();
            return "Full name should contain only letters and spaces (2-50 characters)";
        }
        
        String email = emailField.getText().trim();
        boolean isEmailEmpty = email.isEmpty() || email.equals("Enter your email address");
        if (isEmailEmpty) {
            emailField.requestFocus();
            return "Please enter your email address";
        }
        
        // Enhanced email format validation
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            emailField.requestFocus();
            return "Please enter a valid email address (e.g., user@example.com)";
        }
        
        String contact = contactNumberField.getText().trim();
        boolean isContactEmpty = contact.isEmpty() || contact.equals("Enter your phone number");
        if (isContactEmpty) {
            contactNumberField.requestFocus();
            return "Please enter your contact number";
        }
        
        // Enhanced contact number validation
        if (!contact.matches("^[0-9]{9,15}$")) {
            contactNumberField.requestFocus();
            return "Contact number must be 9-15 digits only (no spaces or special characters)";
        }
        
        String password = new String(passwordField.getPassword());
        boolean isPasswordEmpty = password.isEmpty() || password.equals("Enter your password");
        if (isPasswordEmpty) {
            passwordField.requestFocus();
            return "Please enter a password";
        }
        
        if (password.length() < 6) {
            passwordField.requestFocus();
            return "Password must be at least 6 characters long";
        }
        
        // Password strength validation
        if (!password.matches(".*[a-zA-Z].*")) {
            passwordField.requestFocus();
            return "Password must contain at least one letter";
        }
        
        String confirmPassword = new String(confirmPasswordField.getPassword());
        boolean isConfirmEmpty = confirmPassword.isEmpty() || confirmPassword.equals("Confirm your password");
        if (isConfirmEmpty || !password.equals(confirmPassword)) {
            confirmPasswordField.requestFocus();
            return "Passwords do not match - please check and try again";
        }
        
        String occupation = occupationField.getText().trim();
        boolean isOccupationEmpty = occupation.isEmpty() || occupation.equals("Enter your occupation");
        if (isOccupationEmpty) {
            occupationField.requestFocus();
            return "Please enter your occupation";
        }
        
        // Validate occupation format
        if (!occupation.matches("^[a-zA-Z\\s]{2,30}$")) {
            occupationField.requestFocus();
            return "Occupation should contain only letters and spaces (2-30 characters)";
        }
        
        // Date of birth validation with specific messages
        if (dayComboBox.getSelectedIndex() == 0) {
            return "Please select your birth day";
        }
        
        if (monthComboBox.getSelectedIndex() == 0) {
            return "Please select your birth month";
        }
        
        if (yearComboBox.getSelectedIndex() == 0) {
            return "Please select your birth year";
        }
        
        if (genderComboBox.getSelectedIndex() == 0) {
            return "Please select your gender";
        }
        
        if (!termsCheckBox.isSelected()) {
            return "Please accept the terms and conditions to continue";
        }
        
        return null; // No validation errors
    }

    private String getDateOfBirth() {
        String year = (String) yearComboBox.getSelectedItem();
        String month = (String) monthComboBox.getSelectedItem();
        String day = (String) dayComboBox.getSelectedItem();
        return year + "-" + month + "-" + day;
    }

    private char getSelectedGender() {
        String selectedGender = (String) genderComboBox.getSelectedItem();
        return switch (selectedGender) {
            case "Male" -> 'M';
            case "Female" -> 'F';
            default -> 'O';
        }; // Other
    }

    private void goBackToLogin() {
        parentFrame.setVisible(true);
        this.dispose();
    }

    private void showStatus(String message, Color color) {
        statusLabel.setText(message);
        statusLabel.setForeground(color);
        
        // Make error and success messages more prominent
        if (color == Color.RED) {
            statusLabel.setFont(new Font("Arial", Font.BOLD, 14));
            // Add a subtle border effect for errors
            statusLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.RED, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
            ));
        } else if (color == Color.GREEN) {
            statusLabel.setFont(new Font("Arial", Font.BOLD, 14));
            statusLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GREEN, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
            ));
        } else {
            statusLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        }
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
}
