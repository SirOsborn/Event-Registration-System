package com.eventms.database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Simple JDBC helper for MySQL connections.
 * Configuration via environment variables (or sensible defaults):
 * - DB_URL (default: jdbc:mysql://localhost:3306/eventManagement)
 * - DB_USER (default: root)
 * - DB_PASSWORD (default: empty)
 */
public final class Database {
    private static final String DEFAULT_URL = "jdbc:mysql://localhost:3306/eventManagement?useSSL=false&serverTimezone=UTC";
    private static final String DEFAULT_USER = "root";
    private static final String DEFAULT_PASSWORD = "";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    private static volatile boolean driverLoaded = false;
    private static volatile Map<String, String> DOTENV = null; // loaded once

    private Database() {}

    public static boolean isConfigured() {
        // Consider DB usable if driver can be loaded
        try {
            loadDriver();
            return true;
        } catch (ClassNotFoundException t) {
            return false;
        }
    }

    private static void loadDriver() throws ClassNotFoundException {
        if (!driverLoaded) {
            Class.forName(DRIVER);
            driverLoaded = true;
        }
    }

    private static void loadDotEnvOnce() {
        if (DOTENV != null) return;
        synchronized (Database.class) {
            if (DOTENV != null) return;
            DOTENV = new HashMap<>();
            try {
                File envFile = new File(System.getProperty("user.dir"), ".env");
                if (!envFile.exists()) return;
                try (BufferedReader br = new BufferedReader(new FileReader(envFile))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        line = line.trim();
                        if (line.isEmpty() || line.startsWith("#")) continue;
                        int eq = line.indexOf('=');
                        if (eq <= 0) continue;
                        String key = line.substring(0, eq).trim();
                        String value = line.substring(eq + 1).trim();
                        if ((value.startsWith("\"") && value.endsWith("\"")) || (value.startsWith("'") && value.endsWith("'"))) {
                            value = value.substring(1, value.length() - 1);
                        }
                        DOTENV.put(key, value);
                    }
                }
            } catch (IOException e) {
                System.err.println("[Database] Warning: failed to read .env: " + e.getMessage());
            }
        }
    }

    public static Connection getConnection() throws SQLException {
        try {
            loadDriver();
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC driver not found. Ensure mysql-connector-j is on the classpath.", e);
        }
        String url = getEnvOrDefault("DB_URL", DEFAULT_URL);
        String user = getEnvOrDefault("DB_USER", DEFAULT_USER);
        String password = getEnvOrDefault("DB_PASSWORD", DEFAULT_PASSWORD);
        return DriverManager.getConnection(url, user, password);
    }

    private static String getEnvOrDefault(String key, String def) {
        loadDotEnvOnce();
        String val = DOTENV != null ? DOTENV.get(key) : null; // .env takes precedence
        if (val == null || val.isEmpty()) {
            val = System.getenv(key);
        }
        return (val == null || val.isEmpty()) ? def : val;
    }

    // Public accessor to retrieve configuration values with precedence: .env -> env -> default
    public static String getConfig(String key, String def) {
        return getEnvOrDefault(key, def);
    }
}
