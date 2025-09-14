package org.example.repository;

import org.example.model.Githubuser;
import org.example.model.GitHubResponse;
import org.example.util.ConfigLoader;

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

    public void saveUsers(GitHubResponse gitHubResponse) {
        try {
            Connection conn = DriverManager.getConnection(dbURL, dbUsername, dbPassword);

            String sql = "INSERT INTO github_users (id, login, html_url, avatar_url, score) " +
                    "VALUES(?, ?, ?, ?, ?) ON CONFLICT DO NOTHING";

            PreparedStatement pstmt = conn.prepareStatement(sql);

            for (Githubuser user : gitHubResponse.items) {
                pstmt.setLong(1, user.id);
                pstmt.setString(2, user.login);
                pstmt.setString(3, user.html_url);
                pstmt.setString(4, user.avatar_url);
                pstmt.setDouble(5, user.score);
                pstmt.executeUpdate();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void saveSearchHistory(String searchTerm, String rawJsonResponse) {
        Properties props = ConfigLoader.loadProperties();
        String dbURL = props.getProperty("db.url");
        String dbUsername = props.getProperty("db.username");
        String dbPassword = props.getProperty("db.password");

        try (Connection conn = DriverManager.getConnection(dbURL, dbUsername, dbPassword);
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO history (search_term, response_json) VALUES (?, ?)")) {

            stmt.setString(1, searchTerm);
            stmt.setString(2, rawJsonResponse);
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
