package com.example.internshipapp.controller.user;

import com.example.internshipapp.controller.RestResponseEntityExceptionHandler;
import com.example.internshipapp.controller.UserController;
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
import org.mockito.Mockito;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringJUnit4ClassRunner.class)
public class UserControllerUnitTests {

    private MockMvc mockMvc;

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    private static final String TEST_USERNAME = "testUsername";
    private static final String TEST_PASSWORD = "testPassword";
    private static final String TEST_FIRST_NAME = "testFirstName";
    private static final String TEST_LAST_NAME = "testLastName";
    private static final double TEST_TO_PAY = 8;


    private User getMockedUser() {
        User user = new User(TEST_USERNAME, TEST_PASSWORD, TEST_FIRST_NAME, TEST_LAST_NAME, TEST_TO_PAY);
        user.setId("abc");
        return user;
    }

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new RestResponseEntityExceptionHandler())
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    public void getUsersTest() throws Exception {

        User user = this.getMockedUser();
        List<User> allUsers = new ArrayList<>();
        allUsers.add(user);

        PageImpl<User> users = new PageImpl<>(allUsers);

        when(userService.getUsers(Mockito.anyString(), Mockito.any(Pageable.class)))
                .thenReturn(users);

        mockMvc.perform(get("http://localhost:8080/users"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.content[0].username", Matchers.is(TEST_USERNAME)))
                .andExpect(jsonPath("$.content[0].firstName", Matchers.is(TEST_FIRST_NAME)))
                .andExpect(jsonPath("$.content[0].lastName", Matchers.is(TEST_LAST_NAME)))
                .andExpect(jsonPath("$.content[0].toPay", Matchers.is(TEST_TO_PAY)));
    }

    @Test
    public void getUserByIdTest() throws Exception {

        User user = this.getMockedUser();

        given(userService.getUserById(user.getId())).willReturn(user);

        mockMvc.perform(get("http://localhost:8080/users/{id}", user.getId()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.username", Matchers.is(TEST_USERNAME)))
                .andExpect(jsonPath("$.firstName", Matchers.is(TEST_FIRST_NAME)))
                .andExpect(jsonPath("$.lastName", Matchers.is(TEST_LAST_NAME)))
                .andExpect(jsonPath("$.toPay", Matchers.is(TEST_TO_PAY)));
    }

    @Test
    public void getUserByIdWithUnknownIdTest() throws Exception {
        doThrow(NoSuchRecordException.class).when(userService).getUserById("bbb");
        mockMvc.perform(get("/users/bbb"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void createUserTest() throws Exception {

        User user = this.getMockedUser();

        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated());
    }

    @Test
    public void updateTest() throws Exception {
        User user = this.getMockedUser();

        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(patch("http://localhost:8080/users/abc")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteUserWithUnknownIdTest() throws Exception {
        doThrow(NoSuchRecordException.class).when(userService).deleteUserById("100");
        mockMvc.perform(delete("/users/{id}", 100)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteUserByIdTest() throws Exception {

        mockMvc.perform(delete("/users/{id}", "abc")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
