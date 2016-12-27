package dbunittesting.daofactory.resources;

import java.sql.Connection;

public abstract class DBFactory {

    /**
     * Should be used like this :<br>
     * DSLContext create = DSL.using(connection, SQLDialect.POSTGRES_9_4);
     */
    public abstract Connection getConnection() throws Exception;

    //public abstract Jedis getJedi();

    /**
     * Initializes all the configurations of related Database at start of application. <br>
     * It can be used for periodic interval loading, if need be.
     */
    public abstract void initialize();

    public abstract void destroy();

}
