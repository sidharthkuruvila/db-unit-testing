package dbunittesting.daofactory;

import dbunittesting.daofactory.resources.DBFactory;
import dbunittesting.daofactory.resources.PGFactory;

public class DBProducer {

    public static DBFactory getFactory(DBType choice) {
        if (choice == null)
            return null;

        if (choice.equals(DBType.POSTGRES)) {
            return new PGFactory();
        }

        return null;
    }
}