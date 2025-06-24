package com.hossein.spring_project;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.springframework.jdbc.core.JdbcTemplate;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.github.javafaker.Faker;

@Testcontainers
public abstract class AbstractTestcontainers {
    
    @BeforeAll
    static void BeforeAll(){
        Flyway flyway = Flyway.configure().dataSource(
            postgreSQLContainer.getJdbcUrl(),
            postgreSQLContainer.getUsername(),
            postgreSQLContainer.getPassword()
            ).load();
            flyway.migrate();
            System.out.println();
    }

    @Container
    protected static final PostgreSQLContainer<?> postgreSQLContainer =
        new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("hosi-dao-unit-test")
            .withUsername("hossein")
            .withPassword("password");

    @DynamicPropertySource
    private static void registerDateSourceProperties(DynamicPropertyRegistry registery){
        registery.add("spring.datasource.url",
            postgreSQLContainer::getJdbcUrl
        );
        
        registery.add("spring.datasource.username",
            postgreSQLContainer::getUsername
        );
        
        registery.add("spring.datasource.password",
            postgreSQLContainer::getPassword
        );
        

    }

    private static DataSource getDataSource() {
        return DataSourceBuilder.create()
                .driverClassName(postgreSQLContainer.getDriverClassName())
                .url(postgreSQLContainer.getJdbcUrl())
                .username(postgreSQLContainer.getUsername())
                .password(postgreSQLContainer.getPassword()).build();
        
    }

    protected static JdbcTemplate getJdbcTemplate(){
        return new JdbcTemplate(getDataSource());
    }

    protected static final Faker faker = new Faker();
    

}
