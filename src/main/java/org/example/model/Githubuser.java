package org.example.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Githubuser {
    public String login;
    public long id;
    public String avatar_url;
    public String html_url;
    public String type;
    public double score;
}
