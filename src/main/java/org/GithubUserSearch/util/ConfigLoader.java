package org.GithubUserSearch.util;

import org.GithubUserSearch.Common.Constants;

import java.io.*;
import java.util.Properties;

public class ConfigLoader {
    public static Properties loadProperties(){
        Properties properties = new Properties();
        try (InputStream inputStream = ConfigLoader.class
                .getClassLoader()
                .getResourceAsStream("application.properties")) {

            if (inputStream != null) {
                properties.load(inputStream);
            } else {
                System.err.println(Constants.ERROR_PROPERTIES_NOT_FOUND);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }
}
