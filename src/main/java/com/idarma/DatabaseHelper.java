package com.idarma;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {

    public static void insertHistory(int userId, String quizName, int score) {
        String sql = "INSERT INTO history (user_id, quiz_name, score) VALUES (?, ?, ?)";

        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setString(2, quizName);
            stmt.setInt(3, score);
            stmt.executeUpdate();

            System.out.println("Riwayat quiz berhasil disimpan.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class LeaderboardEntry {
        private int rank;
        private String username;
        private int totalScore;

        public LeaderboardEntry(int rank, String username, int totalScore) {
            this.rank = rank;
            this.username = username;
            this.totalScore = totalScore;
        }

        public int getRank() {
            return rank;
        }

        public String getUsername() {
            return username;
        }

        public int getTotalScore() {
            return totalScore;
        }
    }

    public static List<LeaderboardEntry> getLeaderboard() {
        List<LeaderboardEntry> leaderboard = new ArrayList<>();
        String sql = "SELECT u.username, COALESCE(SUM(h.score), 0) as total_score " +
                "FROM users u " +
                "LEFT JOIN history h ON u.id = h.user_id " +
                "GROUP BY u.id, u.username " +
                "ORDER BY total_score DESC";

        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();
            int rank = 1;
            while (rs.next()) {
                String username = rs.getString("username");
                int totalScore = rs.getInt("total_score");
                leaderboard.add(new LeaderboardEntry(rank++, username, totalScore));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return leaderboard;
    }
}
