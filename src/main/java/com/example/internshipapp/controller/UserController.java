package com.example.internshipapp.controller;

import com.example.internshipapp.model.User;
import com.example.internshipapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Page<User> getUsers (Pageable pageable){
        Page<User> users = userService.listAllByPage(pageable);
        return users;
    }

    @GetMapping("/{id}")
    public User findById(@PathVariable(name = "id") String id) {
        return userService.findById(id);
    }

    @PostMapping
    public ResponseEntity<User> create(@RequestBody @Valid User user) {

        return new ResponseEntity<>(userService.create(user.getUsername(), user.getPassword(), user.getFirstName(), user.getLastName(), user.getToPay()), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public void modifyUser(@PathVariable (name="id") String id, @Valid @RequestBody User user){
         userService.update(id, user.getUsername(), user.getPassword(), user.getFirstName(), user.getLastName(), user.getToPay());
    }

    @DeleteMapping("/{id}")
    public void delete (@PathVariable (name="id") String id){
        userService.delete(id);
    }

}
