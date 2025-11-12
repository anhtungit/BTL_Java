package org.openjfx.DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String DB_HOST = "localhost";
    private static final String DB_PORT = "3307";
    private static final String DB_NAME = "coffeeShop";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "1";

    private static final String DB_URL = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME + "?useSSL=false";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }

}
