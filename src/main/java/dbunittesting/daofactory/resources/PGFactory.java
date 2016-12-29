package dbunittesting.daofactory.resources;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dbunittesting.Config;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class PGFactory extends DBFactory {


    public static DataSource dataSource;

    /**
     * It provides connection to PostgreSQL Database It must only be called from {@link DBProducer}
     * class.
     */
    public PGFactory() {
        super();
    }

    @Override
    public Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e){
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void initialize() {
        if (dataSource == null) {
            HikariConfig config = new HikariConfig();

            //IniUtils cp = PropertyReader.getConnPoolIni();
            Config.DatabaseConfig dbConfig = Config.getDatabaseconfig();
            config.setJdbcUrl(dbConfig.getJdbcUrl());
            config.setUsername(dbConfig.getUsername());
            config.setPassword(dbConfig.getPassword());
            config.setDriverClassName(dbConfig.getDbDriver());

            config.setMaximumPoolSize(dbConfig.getMaxPoolSize());
            config.setInitializationFailFast(true);
            config.setAutoCommit(true);
            config.setConnectionTimeout(dbConfig.getConnectionTimeout());

            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

            // test query
            config.setConnectionTestQuery("SELECT 1");

            dataSource = new HikariDataSource(config);

        }
    }

    @Override
    public void destroy() {
        // Not necessary for RDBMS
    }

}
