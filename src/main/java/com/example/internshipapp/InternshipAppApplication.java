package com.example.internshipapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
public class InternshipAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(InternshipAppApplication.class, args);
    }

}
