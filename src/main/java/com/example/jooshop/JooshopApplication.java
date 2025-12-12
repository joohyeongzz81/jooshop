package com.example.jooshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class JooshopApplication {

    public static void main(String[] args) {
        SpringApplication.run(JooshopApplication.class, args);
    }

}
