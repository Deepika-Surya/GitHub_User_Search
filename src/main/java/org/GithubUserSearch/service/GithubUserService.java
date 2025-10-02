package org.GithubUserSearch.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.GithubUserSearch.Common.Constants;
import org.GithubUserSearch.model.GitHubResponse;
import org.GithubUserSearch.model.Githubuser;
import org.GithubUserSearch.repository.GithubUserRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;

public class GithubUserService {

    public GitHubResponse getAndStoreGithubUsers(String searchTerm, int page) throws IOException, SQLException{
        String apiUrl = String.format(Constants.GITHUB_SEARCH_URL, searchTerm, page);

        String jsonResponse = fetchApiResponse(apiUrl, "GET");

        ObjectMapper mapper = new ObjectMapper();
        GitHubResponse gitHubResponse = mapper.readValue(jsonResponse, GitHubResponse.class);

        GithubUserRepository repo = new GithubUserRepository();

        int searchId = repo.saveSearchHistory(searchTerm, jsonResponse);
        for (Githubuser user : gitHubResponse.items) {
            user.searchId = searchId;
        }
        repo.saveUsers(gitHubResponse);

        return gitHubResponse;
    }

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
