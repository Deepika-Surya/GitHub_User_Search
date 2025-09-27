package org.GithubUserSearch.Common;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class BaseServlet extends HttpServlet {
    protected void sendErrorResponse(HttpServletResponse response, int statusCode, String message) throws IOException {
        response.setStatus(statusCode);
        response.setContentType(Constants.CONTENT_TYPE_JSON);
        response.getWriter().write("{\"error\": \"" + message + "\"}");
    }
}
