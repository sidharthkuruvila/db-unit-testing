package dbunittesting;

import lombok.Builder;
import lombok.Data;

/**
 * Created by sidharth on 26/12/16.
 */
public class Config {
    @Data
    @Builder
    public static class DatabaseConfig {
        private String jdbcUrl;
        private String username;
        private String password;
        private String dbDriver;
        private int maxPoolSize;
        private int connectionTimeout;
    }


    public static DatabaseConfig getDatabaseconfig() {

        return DatabaseConfig.builder()
                .jdbcUrl("jdbc:postgresql://localhost:5432/test_migration")
                .dbDriver("org.postgresql.Driver")
                .username("postgres")
                .password("")
                .connectionTimeout(20000)
                .maxPoolSize(200)
                .build();
    }
}
