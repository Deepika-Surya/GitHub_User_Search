package org.GithubUserSearch.util;

import org.GithubUserSearch.Common.Constants;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ConnectionPool {
    private static final int POOL_SIZE = 50;
    private static BlockingQueue<Connection> pool;

    static{
    try
    {
        Properties props = ConfigLoader.loadProperties();
        String dbURL = props.getProperty("db.url");
        String dbUsername = props.getProperty("db.username");
        String dbPassword = props.getProperty("db.password");

        pool = new ArrayBlockingQueue<>(POOL_SIZE);

        for (int i = 0; i < POOL_SIZE; i++) {
            Connection conn = DriverManager.getConnection(dbURL, dbUsername, dbPassword);
            pool.offer(conn); // add to pool
        }
    } catch(
    SQLException e){
        throw new RuntimeException(Constants.ERROR_CONNECTION_POOL_INIT, e);
    }
}

    public static Connection getConnection() throws SQLException {
        try {
            return pool.take();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new SQLException(Constants.ERROR_CONNECTION_INTERRUPTED, e);
        }
    }

    public static void releaseConnection(Connection conn) {
        if (conn != null) {
            pool.offer(conn);
        }
    }
}
