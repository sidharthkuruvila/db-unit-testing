package dbunittesting.daofactory.resources;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.Connection;

public abstract class DBFactory {

    /**
     * Should be used like this :<br>
     * DSLContext create = DSL.using(connection, SQLDialect.POSTGRES_9_4);
     */
    public abstract Connection getConnection() throws Exception;

    public abstract TransactionTemplate getTransactionTemplate();

    //public abstract Jedis getJedi();

    /**
     * Initializes all the configurations of related Database at start of application. <br>
     * It can be used for periodic interval loading, if need be.
     */
    public abstract void initialize();

    public abstract void destroy();

}
