package com.example.internshipapp.controller;

import com.example.internshipapp.RestResponseEntityExceptionHandler;
import com.example.internshipapp.exception.NoSuchRecordException;
import com.example.internshipapp.model.User;
import com.example.internshipapp.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static java.util.Collections.singletonList;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {TestConfiguration.class})
public class UserControllerTests {

    private MockMvc mockMvc;

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new RestResponseEntityExceptionHandler())
                .build();
    }


    //TODO
    //yet don't know how to extract List<> from Page<>
    @Test
    public void getUsersTest() throws Exception {

        User user = new User("testUsername", "testPassword", "testFirstName", "testLastName", 8, null, null);
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

        User user = new User("testUsername", "testPassword", "testFirstName", "testLastName", 5, null, null);
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
    public void getUserByIdWithUnknownIdTest() throws Exception {
        doThrow(NoSuchRecordException.class).when(userService).getUserById("bbb");
        mockMvc.perform(get("/users/bbb"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void createUserTest() throws Exception {

        User user = new User("testUsername", "testPassword", "testFirstName", "testLastName", 5, null, null);

        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated());
    }

    @Test
    public void updateTest() throws Exception {
        User user = new User("updatedUsername", "updatedPassword", "updatedFirstName", "updatedLastName", 5, null, null);

        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(patch("http://localhost:8080/users/abc")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteUserWithUnknownIdTest() throws Exception {
        doThrow(NoSuchRecordException.class).when(userService).deleteUser("100");
        mockMvc.perform(delete("/users/{id}", 100)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    @Test
    public void deleteUserByIdTest() throws Exception {

        User user = new User("testUsername", "testPassword", "testFirstName", "testLastName", 5, null, null);
        user.setId("abc");

        mockMvc.perform(delete("/users/{id}", "abc")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
