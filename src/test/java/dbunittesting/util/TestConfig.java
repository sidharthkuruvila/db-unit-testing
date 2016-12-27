package dbunittesting.util;

import dbunittesting.daofactory.DBProducer;
import dbunittesting.daofactory.DBType;
import dbunittesting.daofactory.resources.PGFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
}
