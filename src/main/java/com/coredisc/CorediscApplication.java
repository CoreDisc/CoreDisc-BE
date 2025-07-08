package com.coredisc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class CorediscApplication {

    public static void main(String[] args) {
        SpringApplication.run(CorediscApplication.class, args);
    }

}
