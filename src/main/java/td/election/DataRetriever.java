package td.election;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DataRetriever {
    DBConnection dbConnection = new DBConnection();

    public long countAllVotes() {
        String sql = "select count(id) from vote";

        try (Connection conn = dbConnection.getDBConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getLong(1);
            }

            throw new RuntimeException("Vote is empty");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
