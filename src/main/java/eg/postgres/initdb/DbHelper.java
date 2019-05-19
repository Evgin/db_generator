package eg.postgres.initdb;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbHelper {

    private static final XLogger log = XLoggerFactory.getXLogger(DbHelper.class);

    public static Connection createConn() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "postgres");
        conn.setAutoCommit(false);
        return conn;
    }

}
