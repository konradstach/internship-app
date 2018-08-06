package com.example.internshipapp.controller;

import com.example.internshipapp.model.User;
import com.example.internshipapp.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;
    private static final Logger logger = LoggerFactory.getLogger("com.example.internshipapp");


    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @ResponseBody
    public Page<User> getUsers(
            @RequestParam(value = "username", required = false, defaultValue = "") String username,
            @PageableDefault Pageable pageable) {
        return userService.getUsers(username, pageable);
    }

    @GetMapping("/unpaged")
    @ResponseBody
    public List<User> getUsersUnpaged() {
        return userService.getUsersUnpaged();
    }

    @GetMapping("/{id}")
    @ResponseBody
    public User getUserById(@PathVariable(name = "id") String id) {
        return userService.getUserById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<User> createUser(@RequestBody @Valid User user) {

        return new ResponseEntity<>(userService.createUser(user), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public void modifyUser(@PathVariable(name = "id") String id, @Valid @RequestBody User user) {
        userService.updateUser(user);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAllUsers() {
        userService.deleteAllUsers();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserById(@PathVariable(name = "id") String id) {
        userService.deleteUser(id);
    }

}
