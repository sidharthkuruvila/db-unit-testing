package dbunittesting.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.springtestdbunit.bean.DatabaseConfigBean;
import com.github.springtestdbunit.bean.DatabaseDataSourceConnectionFactoryBean;
import com.opentable.db.postgres.embedded.EmbeddedPostgres;
import dbunittesting.daofactory.resources.PGFactory;
import dbunittesting.utils.TestUtils;
import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.FileSystemResourceAccessor;
import liquibase.resource.ResourceAccessor;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseDataSourceConnection;
import org.dbunit.ext.postgresql.PostgresqlDataTypeFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.annotation.PreDestroy;
import javax.sql.DataSource;
import javax.xml.crypto.Data;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@Configuration
public class TestConfig {

    EmbeddedPostgres epg;

    {
        setupEmbeddedPostgres();
    }

    @Bean
    public DataSource dataSource() {
        return PGFactory.dataSource;
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public TestUtils testUtils(ObjectMapper objectMapper, DataSource dataSource) throws DatabaseUnitException, SQLException, IOException {
        return new TestUtils(objectMapper, dbUnitDatabaseConnection(dataSource));
    }

    @Bean
    public DataSourceTransactionManager transactionManager() {
        return PGFactory.dataSourceTransactionManager;
    }

    @PreDestroy
    public void preDestroy() {
        try {
            System.out.println("Closing postgres database");
            epg.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public DatabaseConfigBean databaseConfigBean() {
        DatabaseConfigBean databaseConfigBean = new DatabaseConfigBean();
        databaseConfigBean.setDatatypeFactory(new PostgresqlDataTypeFactory());
        return databaseConfigBean;
    }

    @Bean(name = "dbUnitDatabaseConnection")
    public DatabaseDataSourceConnectionFactoryBean dbUnitDatabaseConnection(DataSource dataSource) throws SQLException, DatabaseUnitException, IOException {
        DatabaseDataSourceConnectionFactoryBean databaseDataSourceConnectionFactoryBean = new DatabaseDataSourceConnectionFactoryBean();
        databaseDataSourceConnectionFactoryBean.setDatabaseConfig(databaseConfigBean());
        databaseDataSourceConnectionFactoryBean.setDataSource(dataSource);
        databaseDataSourceConnectionFactoryBean.setSchema("public");
        return databaseDataSourceConnectionFactoryBean;
    }

    private void setupEmbeddedPostgres() {
        try {
            epg = EmbeddedPostgres.start();
            DataSource ds = epg.getPostgresDatabase();
            ResourceAccessor ra = new FileSystemResourceAccessor();
            try (Connection con = ds.getConnection()) {
                Liquibase liquibase = new Liquibase("gradle/migrations.xml", ra, new JdbcConnection(con));
                liquibase.update((Contexts) null);
            }
            PGFactory.initializeUsingDataSource(ds);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
