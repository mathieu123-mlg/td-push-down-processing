package td.invoice;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    public Connection getDBConnection() {
        Dotenv dotenv = Dotenv.load();
        String jdbc_url = dotenv.get("JDBC_URL_INVOICE");
        String user = dotenv.get("USER");
        String password = dotenv.get("PASSWORD");

        if (jdbc_url == null || user == null || password == null) {
            throw new NullPointerException("jdbc_url or user or password is null");
        }
        try {
            return DriverManager.getConnection(jdbc_url, user, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
