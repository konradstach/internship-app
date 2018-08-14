package com.example.internshipapp.service;

import com.example.internshipapp.exception.NoSuchRecordException;
import com.example.internshipapp.model.User;
import com.example.internshipapp.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@CacheConfig(cacheNames = {"users"})
@Service
public class UserService {

    private UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Adnotating getUsers method by cacheable makes it work correctly only when some username is added as a request parameter

    public Page<User> getUsers(String username, Pageable pageable) {

        logger.info("Getting all users");
        return userRepository.findByUsernameContainingIgnoreCase(username, pageable);
    }

    @Cacheable(key = "#id")
    public User getUserById(String id) {

        User user = userRepository.getById(id);

        if (user != null) {
            logger.info(String.format("user with id =%s found.", id));
            return user;
        } else {
            logger.warn(String.format("user with id =%s not found.", id));
            throw new NoSuchRecordException(String.format("user with id = %s not found", id));
        }
    }

    @CachePut(key = "#result.id", unless = "#result == null")
    public User createUser(User userToCreate) {

        User user = User.clone(userToCreate);
        logger.info("New user created");
        return userRepository.save(user);
    }

    @CachePut(key="#userFromUi.id")
    public User updateUser(final User userFromUi) {

        Optional<User> userFromDb = userRepository.findById(userFromUi.getId());

        if (!userFromDb.isPresent()) {

            logger.warn(String.format("user with id =%s not found", userFromUi.getId()));
            throw new NoSuchRecordException(String.format("user with id = %s not found", userFromUi.getId()));
        }

        User user = User.clone(userFromUi);

        logger.info(String.format("user with id =%s updated", userFromUi.getId()));
        return userRepository.save(user);
    }

    @CacheEvict(allEntries = true)
    public void deleteAllUsers() {

        userRepository.deleteAll();
        logger.warn("All users deleted");
    }

    @CacheEvict(key = "#id")
    public void deleteUserById(String id) {
        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()) {
            userRepository.deleteById(id);
            logger.warn(String.format("user with id =%s deleted", id));
        } else {
            logger.warn(String.format("user with id = %s not found", id));
            throw new NoSuchRecordException(String.format("user with id = %s not found", id));
        }
    }
}
