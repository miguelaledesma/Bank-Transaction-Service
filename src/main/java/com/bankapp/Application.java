package com.bankapp;

import com.bankapp.util.DataSeeder;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner initializeData(DataSeeder dataSeeder) {
        return args -> dataSeeder.seedData();
    }
}
