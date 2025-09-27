package org.GithubUserSearch.service;

import org.GithubUserSearch.Common.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GithubUserService {
    public String fetchApiResponse(String apiUrl, String method) throws IOException {
        StringBuilder response = new StringBuilder();

            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            connection.setRequestProperty("Accept", Constants.CONTENT_TYPE_JSON);

            try(BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            }
        return response.toString();
    }
}
