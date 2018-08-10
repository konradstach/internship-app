package com.example.internshipapp.controller;

import com.example.internshipapp.model.User;
import com.example.internshipapp.service.UserService;
import io.swagger.annotations.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Api(value = "/users", description = "Manage users")
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @ApiOperation("Retreive all users")
    public Page<User> getUsers(
            @RequestParam(value = "username", required = false, defaultValue = "") String username,
            @PageableDefault Pageable pageable) {
        return userService.getUsers(username, pageable);
    }

    @GetMapping("/{id}")
    @ApiOperation("Retreive user with given id")
    @ApiResponses({
            @ApiResponse(code = 404, message = "user with given id doesn't exist")
    })
    public User getUserById(@PathVariable(name = "id") String id) {
        return userService.getUserById(id);
    }

    @PostMapping
    @ApiOperation("Create new user")
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@RequestBody @Valid User user) {

        return userService.createUser(user);
    }

    @PatchMapping("/{id}")
    @ApiResponses({
            @ApiResponse(code = 404, message = "user with given id doesn't exist")
    })
    @ApiOperation("Modify user data")
    public User modifyUser(@PathVariable(name = "id") String id, @Valid @RequestBody User user) {
        return userService.updateUser(user);
    }

    @DeleteMapping
    @ApiOperation("Delete all users")
    @ApiResponses({
            @ApiResponse(code = 204, message = "All users deleted successfully")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAllUsers() {
        userService.deleteAllUsers();
    }

    @DeleteMapping("/{id}")
    @ApiResponses({
            @ApiResponse(code = 404, message = "user with given id doesn't exist"),
            @ApiResponse(code = 204, message = "user deleted successfully")
    })
    @ApiOperation(httpMethod = "DELETE", value = "Delete user with given id")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserById(
            @ApiParam(value = "Id to lookup for", required = true)
            @PathVariable(name = "id") String id) {
        userService.deleteUserById(id);
    }

}
