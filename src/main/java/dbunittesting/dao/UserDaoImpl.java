package dbunittesting.dao;

import dbunittesting.generated.tables.pojos.Users;
import dbunittesting.generated.tables.records.UsersRecord;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Clock;
import java.util.UUID;

import static dbunittesting.generated.tables.Users.USERS;


public class UserDaoImpl implements UserDao{
    @Override
    public Users createUser(Users user, String source) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        try (Connection connection = dbFactory.getConnection()) {
            DSLContext DB = DSL.using(connection, SQLDialect.POSTGRES_9_4);

            UsersRecord ur = DB.newRecord(USERS);
            ur.setId(user.getId() != null ? user.getId() : UUID.randomUUID());
            ur.setFirstName(user.getFirstName());
            ur.setLastName(user.getLastName());
            ur.setUsername(user.getUsername());
            ur.setPassword(user.getPassword());

            ur.setCreatedAt(now);
            ur.setUpdatedAt(now);

            int status = DB.executeInsert(ur);

            if (status != 1) {
                return null;
            }
            user.setId(ur.getId());
            user.setCreatedAt(ur.getCreatedAt());
            user.setUpdatedAt(ur.getUpdatedAt());

            return user;
        } catch (SQLException e) {
            throw new IllegalStateException("DB CONN ERR:", e);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public Users getUserByUsername(String username) {
        Users user;
        try (Connection connection = dbFactory.getConnection()) {
            DSLContext create = DSL.using(connection, SQLDialect.POSTGRES_9_4);

            Result<Record> records = create.select().from(USERS)
                    .where(USERS.USERNAME.equal(username)).fetch();

            if (records != null && !records.isEmpty()) {
                user = setUserDetails(records);
            } else {
                throw new IllegalArgumentException("Failed to find user");
            }

            return user;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private Users setUserDetails(Result<Record> records) {
        Record rec = records.get(0);
        Users user = new Users();
        user.setId(rec.getValue(USERS.ID));
        user.setUsername(rec.getValue(USERS.USERNAME));
        user.setFirstName(rec.getValue(USERS.FIRST_NAME));
        user.setLastName(rec.getValue(USERS.LAST_NAME));
        user.setPassword(rec.getValue(USERS.PASSWORD));
        user.setCreatedAt(rec.getValue(USERS.CREATED_AT));
        user.setUpdatedAt(rec.getValue(USERS.UPDATED_AT));

        return user;
    }
}
