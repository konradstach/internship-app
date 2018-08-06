package com.example.internshipapp.service;

import com.example.internshipapp.exception.NoSuchRecordException;
import com.example.internshipapp.model.User;
import com.example.internshipapp.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Page<User> getUsers(String username, Pageable pageable) {

        logger.info("Getting all users");
        return userRepository.findByUsernameContainingIgnoreCase(username, pageable);
    }

    public List<User> getUsersUnpaged() {
        return userRepository.findAll();
    }

    public User getUserById(String id) {

        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isPresent()) {
            logger.info("User with id =" + id + " found.");
            return userOptional.get();
        } else {
            logger.warn("User not found");
            throw new NoSuchRecordException("User with id = " + id + " not found");
        }
    }

    public User createUser(User userToCreate) {

        User user = new User(userToCreate.getUsername(),
                userToCreate.getPassword(),
                userToCreate.getFirstName(),
                userToCreate.getLastName(),
                userToCreate.getToPay(),
                userToCreate.getBooking(),
                userToCreate.getVehicleList());

        logger.info("New user created");
        return userRepository.save(user);
    }

    public User updateUser(final User userFromUi) {

        Optional<User> userFromDb = userRepository.findById(userFromUi.getId());

        if (!userFromDb.isPresent()) {

            logger.warn("User with id =" + userFromUi.getId() + " not found");
            throw new NoSuchRecordException("User with id = " + userFromUi.getId() + " not found");
        }

        User user = userFromDb.get();

        user.setFirstName(userFromUi.getFirstName());
        user.setLastName(userFromUi.getLastName());
        user.setUsername(userFromUi.getUsername());
        user.setPassword(userFromUi.getPassword());
        user.setToPay(userFromUi.getToPay());

        logger.info("User with id =" + userFromUi.getId() + " updated");
        return userRepository.save(user);
    }

    public void deleteAllUsers() {

        userRepository.deleteAll();
        logger.warn("All users deleted");
    }

    public void deleteUser(String id) {
        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()) {
            userRepository.deleteById(id);
            logger.warn("User with id =" + id + " deleted");
        } else {
            logger.warn("User with id = " + id + " not found");
            throw new NoSuchRecordException("User with id = " + id + " not found");
        }
    }
}
