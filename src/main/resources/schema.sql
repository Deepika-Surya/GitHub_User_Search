CREATE TABLE history (
    search_id SERIAL PRIMARY KEY,
    search_term TEXT NOT NULL,
    response_json TEXT NOT NULL,
    searched_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

SELECT * FROM history;

CREATE TABLE github_users (
    entry_id SERIAL PRIMARY KEY,
    id BIGINT,
    login TEXT,
    html_url TEXT,
    avatar_url TEXT,
    score DOUBLE PRECISION,
    search_id INTEGER REFERENCES history(search_id)
);

SELECT * FROM github_users;