package org.GithubUserSearch.repository;

import org.GithubUserSearch.Common.Constants;
import org.GithubUserSearch.model.Githubuser;
import org.GithubUserSearch.model.GitHubResponse;
import org.GithubUserSearch.util.ConfigLoader;
import org.GithubUserSearch.util.ConnectionPool;

import java.sql.*;
import java.util.List;
import java.util.Properties;

public class GithubUserRepository{
    Properties props = ConfigLoader.loadProperties();
    String dbURL = props.getProperty("db.url");
    String dbUsername = props.getProperty("db.username");
    String dbPassword = props.getProperty("db.password");

    public void saveUsers(GitHubResponse gitHubResponse) throws SQLException{
        String sql = "INSERT INTO github_users (id, login, html_url, avatar_url, score, search_id) " +
                "VALUES(?, ?, ?, ?, ?, ?)";

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
                pstmt.setInt(6, user.searchId);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        } catch (SQLException e) {
            throw new SQLException(Constants.ERROR_SAVE_USERS_FAILED, e);
        } finally {
            ConnectionPool.releaseConnection(conn);
        }
    }

    public int saveSearchHistory(String searchTerm, String rawJsonResponse)
            throws SQLException{
        String sql = "INSERT INTO history (search_term, response_json) VALUES (?, ?) RETURNING search_id";

        Connection conn = null;
        try {
            conn = ConnectionPool.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, searchTerm);
                stmt.setString(2, rawJsonResponse);

                ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        return rs.getInt("search_id");
                    } else {
                        throw new SQLException(Constants.ERROR_SEARCH_ID_NOT_FOUND);
                    }
        } catch (SQLException e) {
            throw new SQLException(Constants.ERROR_SAVE_HISTORY_FAILED, e);
        } finally {
            ConnectionPool.releaseConnection(conn);
        }
    }
}
