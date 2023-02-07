package com.example.practice.database;

import com.example.practice.repositories.ProductRepository;
import org.hibernate.tool.schema.spi.CommandAcceptanceException;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Database {
    private static final Logger logger  = LoggerFactory.getLogger(Database.class);

    @Bean
    CommandLineRunner initDatabas(ProductRepository productRepository){
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {

            }
        };
    }
}
