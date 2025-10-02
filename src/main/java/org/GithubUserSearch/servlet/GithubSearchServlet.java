package org.GithubUserSearch.servlet;

import org.GithubUserSearch.Common.BaseServlet;
import org.GithubUserSearch.Common.Constants;
import org.GithubUserSearch.model.GitHubResponse;
import org.GithubUserSearch.service.GithubUserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/search")
public class GithubSearchServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String searchTerm = request.getParameter("q");
        if (searchTerm == null || searchTerm.isEmpty()) {
            sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, Constants.ERROR_MISSING_SEARCH_TERM);
            return;
        }

        int page = 1;
        String pageParam = request.getParameter("page");
        if (pageParam != null) {
            try {
                page = Integer.parseInt(pageParam);
            } catch (NumberFormatException e) {
                page = 1;
            }
        }

            try {
                GithubUserService githubUserService = new GithubUserService();
                GitHubResponse gitHubResponse = githubUserService.getAndStoreGithubUsers(searchTerm, page);
                sendSuccessResponse(response, HttpServletResponse.SC_OK, gitHubResponse);
            } catch (IOException e) {
                sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, Constants.ERROR_FETCH_FAILED);
            } catch (SQLException e) {
                sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, Constants.ERROR_DB_FAILED);
            }
        }
}
