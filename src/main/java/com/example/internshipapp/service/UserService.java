package com.example.internshipapp.service;

import com.example.internshipapp.exception.NoSuchRecordException;
import com.example.internshipapp.model.User;
import com.example.internshipapp.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Page<User> getUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public List<User> getUsersUnpaged() {
        return userRepository.findAll();
    }

    public User getUserById(String id) {
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            throw new NoSuchRecordException("User with id = " + id + " not found");
        }
    }

    public User createUser(User userToCreate) {

        User user = new User(userToCreate.getUsername(),
                userToCreate.getPassword(),
                userToCreate.getFirstName(),
                userToCreate.getLastName(),
                userToCreate.getToPay());

        return userRepository.save(user);
    }

    public User updateUser(final User userFromUi) {
        Optional<User> userFromDb = userRepository.findById(userFromUi.getId());

        if (!userFromDb.isPresent()) {
            throw new NoSuchRecordException("User with id = " + userFromUi.getId() + " not found");
        }

        User user = userFromDb.get();

        user.setFirstName(userFromUi.getFirstName());
        user.setLastName(userFromUi.getLastName());
        user.setUsername(userFromUi.getUsername());
        user.setPassword(userFromUi.getPassword());
        user.setToPay(userFromUi.getToPay());

        return userRepository.save(user);
    }

    public void deleteAllUsers() {
        userRepository.deleteAll();
    }

    public void deleteUser(String id) {
        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()) {
            userRepository.deleteById(id);
        } else {
            throw new NoSuchRecordException("User with id = " + id + " not found");
        }
    }
}
