package com.eventms.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventDao {
    public static class EventRow {
        public final int id;
        public final String title;
        public final String description;
        public final String location;
        public final Timestamp start;
        public final Timestamp end;
        public final int capacity;
        public final String status;
        public EventRow(int id, String title, String description, String location, Timestamp start, Timestamp end, int capacity, String status) {
            this.id = id; this.title = title; this.description = description; this.location = location; this.start = start; this.end = end; this.capacity = capacity; this.status = status;
        }
    }

    public List<EventRow> listAll() {
        String sql = "SELECT event_id, title, e_description, location, start_time, end_time, capacity, e_status FROM Eventss ORDER BY start_time ASC";
        List<EventRow> out = new ArrayList<>();
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                out.add(new EventRow(
                    rs.getInt("event_id"),
                    rs.getString("title"),
                    rs.getString("e_description"),
                    rs.getString("location"),
                    rs.getTimestamp("start_time"),
                    rs.getTimestamp("end_time"),
                    rs.getInt("capacity"),
                    rs.getString("e_status")
                ));
            }
        } catch (SQLException e) {
            System.err.println("[EventDao] listAll error: " + e.getMessage());
        }
        return out;
    }

    public List<EventRow> listByOrganizer(int organizerId) {
        String sql = "SELECT event_id, title, e_description, location, start_time, end_time, capacity, e_status FROM Eventss WHERE organizer_id = ? ORDER BY created_at DESC";
        List<EventRow> out = new ArrayList<>();
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, organizerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(new EventRow(
                        rs.getInt("event_id"),
                        rs.getString("title"),
                        rs.getString("e_description"),
                        rs.getString("location"),
                        rs.getTimestamp("start_time"),
                        rs.getTimestamp("end_time"),
                        rs.getInt("capacity"),
                        rs.getString("e_status")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("[EventDao] listByOrganizer error: " + e.getMessage());
        }
        return out;
    }

    public int create(String title, String description, String location, Timestamp start, Timestamp end, int capacity, int organizerId, int tokenId) {
        String sql = "INSERT INTO Eventss (title, e_description, location, start_time, end_time, capacity, e_status, organizer_id, token_id) VALUES (?,?,?,?,?,?, 'open', ?,?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, title);
            ps.setString(2, description);
            ps.setString(3, location);
            ps.setTimestamp(4, start);
            ps.setTimestamp(5, end);
            ps.setInt(6, capacity);
            ps.setInt(7, organizerId);
            ps.setInt(8, tokenId);
            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) return keys.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("[EventDao] create error: " + e.getMessage());
        }
        return -1;
    }

    public boolean updateStatus(int eventId, String status) {
        String sql = "UPDATE Eventss SET e_status = ? WHERE event_id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, eventId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[EventDao] updateStatus error: " + e.getMessage());
            return false;
        }
    }

    public boolean cancel(int eventId) {
        return updateStatus(eventId, "cancelled");
    }
}
