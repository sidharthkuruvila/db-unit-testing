package dbunittesting.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opentable.db.postgres.embedded.EmbeddedPostgres;
import dbunittesting.daofactory.resources.PGFactory;
import dbunittesting.utils.TestUtils;
import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.FileSystemResourceAccessor;
import liquibase.resource.ResourceAccessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.annotation.PreDestroy;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;

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
    public TestUtils testUtils(ObjectMapper objectMapper) {
        return new TestUtils(objectMapper);
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

    private void setupEmbeddedPostgres() {
        try {
            epg = EmbeddedPostgres.start();
            DataSource ds = epg.getPostgresDatabase();
            ResourceAccessor ra = new FileSystemResourceAccessor();
            try (Connection con = ds.getConnection()) {
                Liquibase liquibase = new Liquibase("gradle/migrations.xml", ra, new JdbcConnection(con));
                liquibase.update((Contexts)null);
            }
            PGFactory.initializeUsingDataSource(ds);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
