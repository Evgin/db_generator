package eg.postgres.initdb;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InitTask implements Runnable {

    private static final XLogger log = XLoggerFactory.getXLogger(InitTask.class);

    private Connection conn;
    private long count;

    public InitTask(Connection newconn, long count) {
        this.conn = newconn;
        this.count = count;
    }

    @Override
    public void run() {

        log.info("start init task, need create {} persons", count);

        try (PreparedStatement ps = GeneratorHelper.createInsertPersonsStatement(conn)) {
            for (long i = 0; i < count; i++) {

                GeneratorHelper.generatePersonsInsert(ps).addBatch();

                if (i % 10000L == 0) {
                    log.info("Records created: {}", i);
                    ps.executeBatch();
                    conn.commit();
                }
            }

            log.info("Records created: {}", count);
            ps.executeBatch();
            conn.commit();
        } catch (SQLException e) {
            log.error("unable process", e);
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                log.error("unable close conn", e);
            }
        }
    }
}
