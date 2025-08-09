package com.eventms.database;

import com.eventms.model.Role;
import com.eventms.model.User;
import java.sql.*;

/**
 * Minimal JDBC DAO for Users table aligned to eventms_db.sql
 */
public class UserDao {
    private static volatile Boolean profileAvailable = null;

    private boolean hasUserProfile(Connection conn) {
        if (profileAvailable != null) return profileAvailable;
        try (ResultSet rs = conn.getMetaData().getTables(null, null, "UserProfile", null)) {
            profileAvailable = rs.next();
        } catch (SQLException e) {
            profileAvailable = false;
        }
        return profileAvailable;
    }

    public User findByEmail(String email) {
        String base = "SELECT u.user_id, u.full_name, u.email, u.password_hash, u.u_role, u.is_verified, u.created_at FROM Users u WHERE u.email = ?";
        try (Connection conn = Database.getConnection()) {
            if (hasUserProfile(conn)) {
                String join = "SELECT u.user_id, u.full_name, u.email, u.password_hash, u.u_role, u.is_verified, u.created_at, p.contact_number, p.occupation, p.dob, p.gender " +
                        "FROM Users u LEFT JOIN UserProfile p ON p.user_id = u.user_id WHERE u.email = ?";
                try (PreparedStatement ps = conn.prepareStatement(join)) {
                    ps.setString(1, email);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            return mapWithProfile(rs);
                        }
                    }
                }
            }
            try (PreparedStatement ps = conn.prepareStatement(base)) {
                ps.setString(1, email);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) return map(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("[UserDao] findByEmail error: " + e.getMessage());
        }
        return null;
    }

    public int insert(User user, String hashedPassword) {
        String sql = "INSERT INTO Users (full_name, email, password_hash, u_role, is_verified) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getFullName());
            ps.setString(2, user.getEmailRaw());
            ps.setString(3, hashedPassword);
            ps.setString(4, user.getRole() == Role.ADMIN ? "admin" : "user");
            ps.setBoolean(5, user.isVerified());
            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        int id = keys.getInt(1);
                        // Optionally persist extended profile if table exists
                        if (hasUserProfile(conn)) {
                            try (PreparedStatement p2 = conn.prepareStatement(
                                    "INSERT INTO UserProfile (user_id, contact_number, occupation, dob, gender) VALUES (?,?,?,?,?)")) {
                                p2.setInt(1, id);
                                p2.setString(2, user.getContactNumber());
                                p2.setString(3, user.getOccupation());
                                p2.setString(4, user.getDob());
                                p2.setString(5, String.valueOf(user.getGender()));
                                p2.executeUpdate();
                            } catch (SQLException ignore) {}
                        }
                        return id;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("[UserDao] insert error: " + e.getMessage());
        }
        return -1;
    }

    public java.util.List<User> listAll() {
        String sql = "SELECT user_id, full_name, email, password_hash, u_role, is_verified, created_at FROM Users ORDER BY created_at DESC";
        java.util.List<User> out = new java.util.ArrayList<>();
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                out.add(map(rs));
            }
        } catch (SQLException e) {
            System.err.println("[UserDao] listAll error: " + e.getMessage());
        }
        return out;
    }

    public boolean promoteToAdmin(int userId) {
        String sql = "UPDATE Users SET u_role = 'admin' WHERE user_id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[UserDao] promoteToAdmin error: " + e.getMessage());
            return false;
        }
    }

    public boolean markVerified(int userId) {
        String sql = "UPDATE Users SET is_verified = 1 WHERE user_id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[UserDao] markVerified error: " + e.getMessage());
            return false;
        }
    }

    private User map(ResultSet rs) throws SQLException {
        int id = rs.getInt("user_id");
        String fullName = rs.getString("full_name");
        String email = rs.getString("email");
        String passwordHash = rs.getString("password_hash");
        String roleStr = rs.getString("u_role");
        boolean verified = rs.getBoolean("is_verified");

        // Map to existing model with placeholders for fields not in DB schema
        User u = new User(id, fullName, email, "0000000000", passwordHash, "Unknown", "2000-01-01", 'O');
        u.setVerified(verified);
        u.setRole("admin".equalsIgnoreCase(roleStr) ? Role.ADMIN : Role.USER);
        return u;
    }

    private User mapWithProfile(ResultSet rs) throws SQLException {
        int id = rs.getInt("user_id");
        String fullName = rs.getString("full_name");
        String email = rs.getString("email");
        String passwordHash = rs.getString("password_hash");
        String roleStr = rs.getString("u_role");
        boolean verified = rs.getBoolean("is_verified");
        String contact = rs.getString("contact_number");
        String occupation = rs.getString("occupation");
        String dob = rs.getString("dob");
        String genderStr = rs.getString("gender");
        char gender = (genderStr != null && !genderStr.isEmpty()) ? genderStr.charAt(0) : 'O';
        if (contact == null || !contact.matches("\\d{9,15}")) contact = "000000000";
        if (occupation == null || occupation.isBlank()) occupation = "Not set";
        if (dob == null || dob.isBlank()) dob = "2000-01-01";
        User u = new User(id, fullName, email, contact, passwordHash, occupation, dob, gender);
        u.setVerified(verified);
        u.setRole("admin".equalsIgnoreCase(roleStr) ? Role.ADMIN : Role.USER);
        return u;
    }
}
