package com.example.internshipapp.controller;

import com.example.internshipapp.model.User;
import com.example.internshipapp.repository.UserRepository;
import com.example.internshipapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;
    private UserRepository userRepository;

    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository=userRepository;
    }


    @GetMapping
    @ResponseBody
    public Page<User> getUsers(Pageable pageable) {
        return userService.getUsers(pageable);
    }

    @GetMapping("/unpaged")
    @ResponseBody
    public List<User> getUsersUnpaged(){
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    @ResponseBody
    public User getUserById(@PathVariable(name = "id") String id) {
        return userService.getUserById(id);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody @Valid User user) {

        return new ResponseEntity<>(userService.createUser(user), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public void modifyUser(@PathVariable(name = "id") String id, @Valid @RequestBody User user) {
        userService.updateUser(user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable(name = "id") String id) {
        userService.deleteUser(id);
    }

}
