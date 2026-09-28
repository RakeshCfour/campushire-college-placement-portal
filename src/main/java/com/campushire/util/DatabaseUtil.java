package com.campushire.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Scanner;

public class DatabaseUtil {
    // We will use an absolute path for the SQLite DB or put it in a temp dir so it always works.
    private static final String URL = "jdbc:sqlite:" + System.getProperty("java.io.tmpdir") + "/campushire.db";

    static {
        try {
            Class.forName("org.sqlite.JDBC");
            initDatabase();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws java.sql.SQLException {
        return DriverManager.getConnection(URL);
    }

    private static void initDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             InputStream is = DatabaseUtil.class.getClassLoader().getResourceAsStream("schema.sql")) {
            
            if (is != null) {
                Scanner scanner = new Scanner(is, "UTF-8").useDelimiter("\\A");
                String sql = scanner.hasNext() ? scanner.next() : "";
                scanner.close();
                
                String[] statements = sql.split(";");
                for (String s : statements) {
                    if (s.trim().length() > 0) {
                        stmt.execute(s);
                    }
                }
                System.out.println("Database initialized successfully at " + URL);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
