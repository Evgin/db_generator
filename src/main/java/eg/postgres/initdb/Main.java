package eg.postgres.initdb;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

    private static final XLogger log = XLoggerFactory.getXLogger(Main.class);

    private static final int MAX_THREADS_COUNT = 4;
    private static final long MAX_COUNT = 50000000L; // 1b

    public static void main(String[] args) throws SQLException, InterruptedException {

        Connection conn = DbHelper.createConn();

        TableHelper tableHelper = new TableHelper(conn);

        long personsExistsCount = tableHelper.count(Tables.PERSONS);
        log.info("already persons exists: {}", personsExistsCount);

        if (personsExistsCount >= MAX_COUNT) {
            log.info("all persons exists!");
            return;
        }

        long personsPerThread = (MAX_COUNT - personsExistsCount) / MAX_THREADS_COUNT;

        ExecutorService executorService = Executors.newFixedThreadPool(MAX_THREADS_COUNT);

        for (int i = 0; i < MAX_THREADS_COUNT; i++) {
            Connection newConn = DbHelper.createConn();
            executorService.execute(new InitTask(newConn, personsPerThread));
        }

        executorService.shutdown();
        executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.MICROSECONDS);

        conn.close();
    }

}
