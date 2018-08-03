package com.example.internshipapp.controller;

import com.example.internshipapp.exception.NoSuchRecordException;
import com.example.internshipapp.model.User;
import com.example.internshipapp.repository.UserRepository;
import com.example.internshipapp.service.UserService;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.mockito.Mockito.*;

import java.util.List;

import static java.util.Collections.singletonList;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;


@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserController userController;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserService userService;

    @Test
    public void getUsersTest() throws Exception {

        User user = new User("testUsername", "testPassword", "testFirstName", "testLastName", 8);
        List<User> allUsers = singletonList(user);

        given(userController.getUsersUnpaged()).willReturn(allUsers);
        userController.createUser(user);

        mockMvc.perform(get("http://localhost:8080/users/unpaged"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.[0].username", Matchers.is("testUsername")))
                .andExpect(jsonPath("$.[0].firstName", Matchers.is("testFirstName")))
                .andExpect(jsonPath("$.[0].lastName", Matchers.is("testLastName")))
                .andExpect(jsonPath("$.[0].toPay", Matchers.is(8.0)));
    }

    @Test
    public void getUserByIdTest() throws Exception {

        User user = new User("testUsername", "testPassword", "testFirstName", "testLastName", 5);
        user.setId("abc");

        given(userService.getUserById(user.getId())).willReturn(user);

        mockMvc.perform(get("http://localhost:8080/users/{id}", user.getId()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.username", Matchers.is("testUsername")))
                .andExpect(jsonPath("$.firstName", Matchers.is("testFirstName")))
                .andExpect(jsonPath("$.lastName", Matchers.is("testLastName")))
                .andExpect(jsonPath("$.toPay", Matchers.is(5.0)));
    }

    @Test
    public void getUserByIdWithUnknownIdTest() throws Exception{

//        User user = new User("testUsername", "testPassword", "testFirstName", "testLastName", 5);
//        user.setId("abc");
//        userController.createUser(user);
//
//        given(userService.getUserById(user.getId())).willReturn(user);
        mockMvc.perform(get("http://localhost:8080/users/bbb"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code", Matchers.is(404)));
    }


    @Test
    public void createUserTest() throws Exception {

        User user = new User("testUsername", "testPassword", "testFirstName", "testLastName", 5);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createUserInJson(user)))
                .andExpect(status().isCreated());
    }

    @Test
    public void DeleteUserWithUnknownIdTest() throws Exception{
        mockMvc.perform(delete("/users/{id}", 100)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code", Matchers.is(404)))
                .andExpect(jsonPath("$.description", Matchers.is("Not found user with id: 100")));
    }



    private static String createUserInJson(User user) {
        return "{ \"username\":\"" + user.getUsername() + "\"," +
                "\"firstName\": \"" + user.getFirstName() + "\", " +
                "\"lastName\":\"" + user.getLastName() + "\"," +
                "\"toPay\":\"" + user.getToPay() + "\"}";
    }
}
