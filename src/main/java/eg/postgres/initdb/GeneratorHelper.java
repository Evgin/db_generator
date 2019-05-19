package eg.postgres.initdb;

import com.github.javafaker.*;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GeneratorHelper {

    private static final Faker FAKER = new Faker();

    private static Name fName = FAKER.name();
    private static Lorem fLorem = FAKER.lorem();
    private static DateAndTime fDateAndTime = FAKER.date();
    private static Address fAddress = FAKER.address();


    public static PreparedStatement createInsertPersonsStatement(Connection conn) throws SQLException {
        return conn.prepareStatement("insert into persons(personal_number, first_name, second_name, middle_name, birth_date, birth_place) " +
                "values (?, ?, ?, ?, ?, ?)");
    }

    public static PreparedStatement generatePersonsInsert(PreparedStatement ps) throws SQLException {

        ps.setString(1, fLorem.characters(16));
        ps.setString(2, fName.firstName());
        ps.setString(3, fName.lastName());
        ps.setString(4, fName.firstName());
        ps.setDate(5, new Date(fDateAndTime.birthday().getTime()));
        ps.setString(6, fAddress.fullAddress());

        return ps;
    }

}
