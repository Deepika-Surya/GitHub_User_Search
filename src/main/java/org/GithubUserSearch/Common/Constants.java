package org.GithubUserSearch.Common;

public class Constants {
    public static final String GITHUB_SEARCH_URL = "https://api.github.com/search/users?q=%s";

    public static final String CONTENT_TYPE_JSON = "application/json";

    // Error messages
    public static final String ERROR_MISSING_SEARCH_TERM = "Missing search term";
    public static final String ERROR_FETCH_FAILED = "Error: Failed to fetch or parse GitHub API response";
    public static final String ERROR_DB_FAILED = "Error: Database error occurred while saving data";
    public static final String ERROR_UNEXPECTED = "Error: Unexpected server error: ";
    public static final String ERROR_SAVE_USERS_FAILED = "Error saving GitHub users";
    public static final String ERROR_SAVE_HISTORY_FAILED = "Error saving search history";
    public static final String ERROR_SEARCH_ID_NOT_FOUND = "Failed to retrieve search_id";

    public static final String ERROR_CONNECTION_POOL_INIT = "Error: Failed to initialize connection pool";
    public static final String ERROR_CONNECTION_INTERRUPTED = "Error: Interrupted while waiting for DB connection";

    public static final String ERROR_PROPERTIES_NOT_FOUND = "Error: Properties file not found in classpath";

}
