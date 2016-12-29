package dbunittesting.dao;

import dbunittesting.daofactory.DBProducer;
import dbunittesting.daofactory.resources.DBFactory;
import dbunittesting.generated.tables.pojos.Users;

import static dbunittesting.daofactory.DBType.POSTGRES;

/**
 * Created by sidharth on 26/12/16.
 */
public interface UserDao {
    DBFactory dbFactory = DBProducer.getFactory(POSTGRES);

    Users createUser(Users user, String source);

    Users getUserByUsername(String username);

}
