package org.example.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.GitHubResponse;
import org.example.repository.GithubUserRepository;
import org.example.service.GithubUserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet("/search")
public class GithubSearchServlet extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String searchTerm = request.getParameter("q");
        if (searchTerm == null || searchTerm.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing search term");
            return;
        }

        try {
            String apiUrl = String.format("https://api.github.com/search/users?q=%s", searchTerm);

            GithubUserService apiService = new GithubUserService();
            String jsonResponse = apiService.fetchApiResponse(apiUrl, "GET");

            ObjectMapper mapper = new ObjectMapper();
            GitHubResponse gitHubResponse = mapper.readValue(jsonResponse, GitHubResponse.class);

            GithubUserRepository repo = new GithubUserRepository();
            repo.saveUsers(gitHubResponse);
            long userId = gitHubResponse.items.get(0).id;

            repo.saveSearchHistory(searchTerm, jsonResponse, userId);

            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print(jsonResponse);
            out.flush();
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            response.getWriter().write("Error: Failed to fetch or parse GitHub API response");
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            response.getWriter().write("Error: Database error occurred while saving data");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            response.getWriter().write("Error: Unexpected server error: " + e.getMessage());
        }

    }
}
