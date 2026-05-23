package com.dfs.server.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static Connection connection;

    public static Connection getConnection() {

        try {

            if (connection == null || connection.isClosed()) {

                connection = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/dfs_db",
                        "root",
                        ""
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return connection;
    }
}