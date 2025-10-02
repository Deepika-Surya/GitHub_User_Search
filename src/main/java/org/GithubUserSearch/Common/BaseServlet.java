package org.GithubUserSearch.Common;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.GithubUserSearch.model.ErrorResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class BaseServlet extends HttpServlet {
    private static ObjectMapper mapper = new ObjectMapper();

    protected void sendErrorResponse(HttpServletResponse response, int statusCode, String message) throws IOException {
        response.setStatus(statusCode);
        response.setContentType(Constants.CONTENT_TYPE_JSON);

        ErrorResponse error = new ErrorResponse(message);

        mapper.writeValue(response.getWriter(), error);
    }

    protected <T> void sendSuccessResponse(HttpServletResponse response, int statusCode, T data) throws IOException {
        response.setStatus(statusCode);
        response.setContentType(Constants.CONTENT_TYPE_JSON);
        mapper.writeValue(response.getWriter(), data);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            super.service(req, resp);
        }catch(Exception e){
            sendErrorResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    Constants.ERROR_UNEXPECTED + e.getMessage());
        }
    }
}
