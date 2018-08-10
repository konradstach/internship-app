package com.example.internshipapp.service;

import com.example.internshipapp.exception.NoSuchRecordException;
import com.example.internshipapp.model.User;
import com.example.internshipapp.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Page<User> getUsers(String username, Pageable pageable) {

        logger.info("Getting all users");
        return userRepository.findByUsernameContainingIgnoreCase(username, pageable);
    }

    public User getUserById(String id) {

        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isPresent()) {
            logger.info(String.format("user with id =%s found.", id));
            return userOptional.get();
        } else {
            logger.warn(String.format("user with id =%s not found.", id));
            throw new NoSuchRecordException(String.format("user with id = %s not found", id));
        }
    }

    public User createUser(User userToCreate) {

        User user = User.clone(userToCreate);
        logger.info("New user created");
        return userRepository.save(user);
    }

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

    public void deleteAllUsers() {

        userRepository.deleteAll();
        logger.warn("All users deleted");
    }

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
