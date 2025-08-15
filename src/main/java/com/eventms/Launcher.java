package com.eventms;

import com.eventms.database.Database;
import com.eventms.database.UserDao;
import com.eventms.model.Role;
import com.eventms.model.User;
import com.eventms.service.UserService;
import com.eventms.view.LoginFrame;
import javax.swing.*;

public class Launcher {
    public static void main(String[] args) {
        // Always launch GUI
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignore) {}

        SwingUtilities.invokeLater(() -> {
            UserService userService = new UserService();
            // Secure admin bootstrap (optional). Controlled via .env/env:
            // ADMIN_BOOTSTRAP_TOKEN must be non-empty to enable.
            // ADMIN_EMAIL / ADMIN_PASSWORD used to create admin if missing.
            try {
                String token = Database.getConfig("ADMIN_BOOTSTRAP_TOKEN", "").trim();
                if (!token.isEmpty()) {
                    String email = Database.getConfig("ADMIN_EMAIL", "admin@example.com").trim().toLowerCase();
                    String fullName = Database.getConfig("ADMIN_NAME", "Bootstrap Admin").trim();
                    String rawPass = Database.getConfig("ADMIN_PASSWORD", "ChangeMe!123");
                    UserDao dao = new UserDao();
                    User existing = dao.findByEmail(email);
                    if (existing == null && Database.isConfigured()) {
                        // Create via UserService to keep memory state in sync; then promote and verify.
                        try {
                            User created = userService.createUser(fullName, email, "999999999", rawPass, "en", "Admin", "2000-01-01", 'O');
                            dao.promoteToAdmin(created.getUserId());
                            dao.markVerified(created.getUserId());
                            created.setRole(Role.ADMIN);
                            created.setVerified(true);
                            System.out.println("[Bootstrap] Admin created: " + email);
                        } catch (Exception ce) {
                            System.err.println("[Bootstrap] Create admin failed: " + ce.getMessage());
                        }
                    }
                }
            } catch (Throwable t) {
                System.err.println("[Bootstrap] Skipped: " + t.getMessage());
            }
            LoginFrame loginFrame = new LoginFrame(userService);
            loginFrame.setVisible(true);
        });
    }
}
