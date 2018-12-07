package com.seagull.beedo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableCaching
@EnableScheduling
@EnableTransactionManagement
@SpringBootApplication
public class BeedoApplication {

    public static void main(String[] args) {
        SpringApplication.run(BeedoApplication.class, args);
    }
}
