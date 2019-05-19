package eg.postgres.initdb;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TableHelper {

    private Connection conn;

    public TableHelper(Connection conn) {
        this.conn = conn;
    }

    long count(String tableName) throws SQLException {
        Statement stmt = conn.createStatement();

        try (ResultSet rs = stmt.executeQuery(String.format("select count(*) from %s;", tableName))) {
            rs.next();
            return rs.getLong(1);
        } finally {
            stmt.close();
        }
    }

}
