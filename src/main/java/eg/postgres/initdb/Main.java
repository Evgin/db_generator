package eg.postgres.initdb;

import com.github.javafaker.*;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import java.sql.*;

public class Main {

    private static final XLogger log = XLoggerFactory.getXLogger(Main.class);

    private static final Faker FAKER = new Faker();
    private static final long MAX_COUNT = 1000000000L; // 1b

    public static void main(String[] args) throws SQLException {

        Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "postgres");
        conn.setAutoCommit(false);

        PreparedStatement ps = conn.prepareStatement("insert into persons(personal_number, first_name, second_name, middle_name, birth_date, birth_place) " +
                "values (?, ?, ?, ?, ?, ?)");


        long count = 0;

        Name fName = FAKER.name();
        Lorem fLorem = FAKER.lorem();
        DateAndTime fDateAndTime = FAKER.date();
        Address fAddress = FAKER.address();

        for (; count < MAX_COUNT; count++) {

            ps.setString(1, fLorem.characters(16));
            ps.setString(2, fName.firstName());
            ps.setString(3, fName.lastName());
            ps.setString(4, fName.firstName());
            ps.setDate(5, new Date(fDateAndTime.birthday().getTime()));
            ps.setString(6, fAddress.fullAddress());

            ps.addBatch();

            if (count % 100000L == 0) {
                log.info("Records created: " + count);
                ps.executeBatch();
                conn.commit();
            }
        }

        conn.close();


    }

}
