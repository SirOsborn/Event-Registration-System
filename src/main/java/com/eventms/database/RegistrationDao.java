package com.eventms.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RegistrationDao {
    public static class RegistrationRow {
        public final int registrationId;
        public final int eventId;
        public final String title;
        public final Timestamp registrationDate;
        public final String status;
        public RegistrationRow(int registrationId, int eventId, String title, Timestamp registrationDate, String status) {
            this.registrationId = registrationId;
            this.eventId = eventId;
            this.title = title;
            this.registrationDate = registrationDate;
            this.status = status;
        }
    }

    public boolean register(int userId, int eventId) {
        String capacitySql = "SELECT e.capacity, (SELECT COUNT(*) FROM Registration r WHERE r.event_id = e.event_id AND r.r_status <> 'cancelled') AS current_count FROM Eventss e WHERE e.event_id = ?";
        String dupSql = "SELECT COUNT(*) FROM Registration WHERE user_id = ? AND event_id = ? AND r_status <> 'cancelled'";
        String insertSql = "INSERT INTO Registration (event_id, user_id) VALUES (?, ?)";
        try (Connection conn = Database.getConnection()) {
            conn.setAutoCommit(false);
            // Prevent duplicate active registrations
            try (PreparedStatement dup = conn.prepareStatement(dupSql)) {
                dup.setInt(1, userId);
                dup.setInt(2, eventId);
                try (ResultSet rs = dup.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        conn.rollback();
                        return false; // already registered
                    }
                }
            }
            try (PreparedStatement capPs = conn.prepareStatement(capacitySql)) {
                capPs.setInt(1, eventId);
                try (ResultSet rs = capPs.executeQuery()) {
                    if (rs.next()) {
                        int capacity = rs.getInt(1);
                        int current = rs.getInt(2);
                        if (capacity > 0 && current >= capacity) {
                            conn.rollback();
                            return false; // full
                        }
                    }
                }
            }
            try (PreparedStatement ins = conn.prepareStatement(insertSql)) {
                ins.setInt(1, eventId);
                ins.setInt(2, userId);
                ins.executeUpdate();
            }
            conn.commit();
            return true;
        } catch (SQLException e) {
            System.err.println("[RegistrationDao] register error: " + e.getMessage());
            return false;
        }
    }

    public boolean cancel(int userId, int registrationId) {
        String sql = "UPDATE Registration SET r_status = 'cancelled' WHERE registration_id = ? AND user_id = ? AND r_status <> 'cancelled'";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, registrationId);
            ps.setInt(2, userId);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("[RegistrationDao] cancel error: " + e.getMessage());
            return false;
        }
    }

    public List<RegistrationRow> listByUser(int userId) {
        String sql = "SELECT r.registration_id, r.event_id, e.title, r.registration_date, r.r_status FROM Registration r JOIN Eventss e ON r.event_id = e.event_id WHERE r.user_id = ? ORDER BY r.registration_date DESC";
        List<RegistrationRow> out = new ArrayList<>();
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(new RegistrationRow(
                        rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getTimestamp(4), rs.getString(5)
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("[RegistrationDao] listByUser error: " + e.getMessage());
        }
        return out;
    }

    public List<RegistrationRow> listByEvent(int eventId) {
        String sql = "SELECT r.registration_id, r.event_id, e.title, r.registration_date, r.r_status FROM Registration r JOIN Eventss e ON r.event_id = e.event_id WHERE r.event_id = ? ORDER BY r.registration_date DESC";
        List<RegistrationRow> out = new ArrayList<>();
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, eventId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(new RegistrationRow(
                        rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getTimestamp(4), rs.getString(5)
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("[RegistrationDao] listByEvent error: " + e.getMessage());
        }
        return out;
    }

    public boolean cancelRegistrationByOrganizer(int eventId, int registrationId) {
        String sql = "UPDATE Registration SET r_status = 'cancelled' WHERE registration_id = ? AND event_id = ? AND r_status <> 'cancelled'";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, registrationId);
            ps.setInt(2, eventId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[RegistrationDao] cancelRegistrationByOrganizer error: " + e.getMessage());
            return false;
        }
    }
}
