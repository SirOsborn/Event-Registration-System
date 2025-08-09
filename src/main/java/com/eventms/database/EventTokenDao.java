package com.eventms.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EventTokenDao {
    public static class TokenRow {
        public final int id;
        public final String token;
        public final boolean isUsed;
        public final Timestamp createdAt;
        public final int createdBy;
        public final int useCount;
        public TokenRow(int id, String token, boolean isUsed, Timestamp createdAt, int createdBy, int useCount) {
            this.id = id; this.token = token; this.isUsed = isUsed; this.createdAt = createdAt; this.createdBy = createdBy; this.useCount = useCount;
        }
    }

    public List<TokenRow> listAll() {
        String sql = "SELECT token_id, token_string, is_used, created_at, created_by, use_count FROM Eventtokens ORDER BY created_at DESC";
        List<TokenRow> out = new ArrayList<>();
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                out.add(new TokenRow(
                    rs.getInt("token_id"),
                    rs.getString("token_string"),
                    rs.getBoolean("is_used"),
                    rs.getTimestamp("created_at"),
                    rs.getInt("created_by"),
                    rs.getInt("use_count")
                ));
            }
        } catch (SQLException e) {
            System.err.println("[EventTokenDao] listAll error: " + e.getMessage());
        }
        return out;
    }

    public int generate(int createdBy) {
        String token = "TOK-" + UUID.randomUUID().toString().replaceAll("-", "").substring(0, 16).toUpperCase();
        String sql = "INSERT INTO Eventtokens (token_string, created_by, is_used, use_count) VALUES (?, ?, 0, 0)";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, token);
            ps.setInt(2, createdBy);
            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) return keys.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("[EventTokenDao] generate error: " + e.getMessage());
        }
        return -1;
    }

    public void incrementUse(int tokenId) {
        String sql = "UPDATE Eventtokens SET use_count = use_count + 1, is_used = 1 WHERE token_id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, tokenId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[EventTokenDao] incrementUse error: " + e.getMessage());
        }
    }

    public Integer findIdByTokenString(String tokenString) {
        String sql = "SELECT token_id FROM Eventtokens WHERE token_string = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tokenString);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("[EventTokenDao] findIdByTokenString error: " + e.getMessage());
        }
        return null;
    }

    public Integer findRecentTokenIdUsedByUser(int userId) {
        // Prefer last token the user used to create events (via Eventss.organizer_id)
        String sql = "SELECT token_id FROM Eventss WHERE organizer_id = ? ORDER BY created_at DESC LIMIT 1";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("[EventTokenDao] findRecentTokenIdUsedByUser error: " + e.getMessage());
        }
        return null;
    }
}
