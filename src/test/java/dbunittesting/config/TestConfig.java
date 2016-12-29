package dbunittesting.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import dbunittesting.daofactory.DBProducer;
import dbunittesting.daofactory.DBType;
import dbunittesting.daofactory.resources.PGFactory;
import dbunittesting.utils.TestUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
public class TestConfig {
    {
        DBProducer.getFactory(DBType.POSTGRES).initialize();
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
    public DataSourceTransactionManager transactionManager(DataSource dataSource) {
        return PGFactory.dataSourceTransactionManager;
    }
}
