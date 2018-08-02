package com.example.internshipapp;

import com.example.internshipapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InternshipAppApplication implements CommandLineRunner {


    @Autowired
    UserRepository userRepository;

    public static void main(String[] args) {
        SpringApplication.run(InternshipAppApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

//        User user1 = new User("user1", "password1", "firstName1", "lastName1", 10);
//        User user2 = new User("user2", "password2", "firstName2", "lastName2", 5);
//        userRepository.save(user1);
//        userRepository.save(user2);

    }
}
