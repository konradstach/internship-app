package com.example.internshipapp.repository;

import com.example.internshipapp.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    Page<User> findByUsernameContainingIgnoreCase(String firstName, Pageable pageable);

    User getById(String id);
}
