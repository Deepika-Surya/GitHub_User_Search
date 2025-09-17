package org.example.repository;

import org.example.model.Githubuser;
import org.example.model.GitHubResponse;
import org.example.util.ConfigLoader;
import org.example.util.ConnectionPool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

public class GithubUserRepository{
    Properties props = ConfigLoader.loadProperties();
    String dbURL = props.getProperty("db.url");
    String dbUsername = props.getProperty("db.username");
    String dbPassword = props.getProperty("db.password");

    public void saveUsers(GitHubResponse gitHubResponse) throws SQLException{
        String sql = "INSERT INTO github_users (id, login, html_url, avatar_url, score) " +
                "VALUES(?, ?, ?, ?, ?) ON CONFLICT DO NOTHING";

        Connection conn = null;
        try {
            conn = ConnectionPool.getConnection();

            PreparedStatement pstmt = conn.prepareStatement(sql);

            for (Githubuser user : gitHubResponse.items) {
                pstmt.setLong(1, user.id);
                pstmt.setString(2, user.login);
                pstmt.setString(3, user.html_url);
                pstmt.setString(4, user.avatar_url);
                pstmt.setDouble(5, user.score);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        } catch (SQLException e) {
            throw new SQLException("Error saving GitHub users", e);
        } finally {
            ConnectionPool.releaseConnection(conn);
        }
    }

    public void saveSearchHistory(String searchTerm, String rawJsonResponse, long userId)
            throws SQLException{
        String sql = "INSERT INTO history (search_term, response_json, user_id) VALUES (?, ?, ?)";

        Connection conn = null;
        try {
            conn = ConnectionPool.getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, searchTerm);
                stmt.setString(2, rawJsonResponse);
                stmt.setLong(3, userId);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new SQLException("Error saving search history", e);
        } finally {
            ConnectionPool.releaseConnection(conn);
        }
    }

}
