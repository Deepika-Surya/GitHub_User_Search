package org.GithubUserSearch.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.GithubUserSearch.Common.BaseServlet;
import org.GithubUserSearch.Common.Constants;
import org.GithubUserSearch.model.GitHubResponse;
import org.GithubUserSearch.model.Githubuser;
import org.GithubUserSearch.repository.GithubUserRepository;
import org.GithubUserSearch.service.GithubUserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet("/search")
public class GithubSearchServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String searchTerm = request.getParameter("q");
        if (searchTerm == null || searchTerm.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, Constants.ERROR_MISSING_SEARCH_TERM);
            return;
        }

        try {
            String apiUrl = String.format(Constants.GITHUB_SEARCH_URL, searchTerm);

            GithubUserService apiService = new GithubUserService();
            String jsonResponse = apiService.fetchApiResponse(apiUrl, "GET");

            ObjectMapper mapper = new ObjectMapper();
            GitHubResponse gitHubResponse = mapper.readValue(jsonResponse, GitHubResponse.class);

            GithubUserRepository repo = new GithubUserRepository();

            int searchId = repo.saveSearchHistory(searchTerm, jsonResponse);
            for (Githubuser user : gitHubResponse.items) {
                user.searchId = searchId;
            }
            repo.saveUsers(gitHubResponse);

            response.setContentType(Constants.CONTENT_TYPE_JSON);
            PrintWriter out = response.getWriter();
            out.print(jsonResponse);
            out.flush();
        } catch (IOException e) {
            sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, Constants.ERROR_FETCH_FAILED);
        } catch (SQLException e) {
            sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, Constants.ERROR_DB_FAILED);
        } catch (Exception e) {
            sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    Constants.ERROR_UNEXPECTED + e.getMessage());
        }

    }
}
