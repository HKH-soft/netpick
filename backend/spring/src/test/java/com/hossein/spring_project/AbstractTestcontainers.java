package com.hossein.spring_project;

import com.github.javafaker.Faker;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Testcontainers
public abstract class AbstractTestcontainers {

    @Container
    protected static final PostgreSQLContainer<?> postgreSQLContainer =
        new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("hosi-dao-unit-test")
            .withUsername("hossein")
            .withPassword("password");

    private static HikariDataSource hikariDataSource;

    protected static final Faker faker = new Faker();

    @BeforeAll
    static void beforeAll() {
        postgreSQLContainer.start();

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(postgreSQLContainer.getJdbcUrl());
        config.setUsername(postgreSQLContainer.getUsername());
        config.setPassword(postgreSQLContainer.getPassword());
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setIdleTimeout(30000);
        config.setMaxLifetime(1800000);

        hikariDataSource = new HikariDataSource(config);

        Flyway flyway = Flyway.configure()
            .dataSource(hikariDataSource)
            .load();
        flyway.migrate();
    }
    @AfterAll
    static void afterAll() {
        if (hikariDataSource != null) {
            hikariDataSource.close();
        }
    }

    protected static JdbcTemplate getJdbcTemplate() {
        return new JdbcTemplate(hikariDataSource);
    }

    @DynamicPropertySource
    private static void registerDateSourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }
    
}
