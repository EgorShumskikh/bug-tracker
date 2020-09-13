package project.jdbс;

import java.io.File;
import java.sql.*;

public class DBConnectToMemory extends DBConnect{

    private final String DB_URL = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1";
    private final String USER = "sa";
    private final String PASS = "";

    public DBConnectToMemory()  {
        try (Connection conn = getConnection()) {
            executeSqlScript(conn, new File("src/test/resources/Project_create.sql"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
        return conn;
    }

}