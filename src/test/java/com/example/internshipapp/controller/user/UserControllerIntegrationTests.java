package com.example.internshipapp.controller.user;

import com.example.internshipapp.model.User;
import com.example.internshipapp.repository.UserRepository;
import com.example.internshipapp.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
//@IfProfileValue(name = "tests", value = "tests")
public class UserControllerIntegrationTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mockMvc;

    private User testUser;

    private static final String TEST_ID = "testId1";
    private static final String TEST_USERNAME = "testUsername1";
    private static final String TEST_PASSWORD = "testPassword1";
    private static final String TEST_FIRST_NAME = "testFirstName1";
    private static final String TEST_LAST_NAME = "testLastName1";
    private static final double TEST_TO_PAY = 1;

    @Before
    public void setup() {

        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        testUser = new User(TEST_USERNAME, TEST_PASSWORD, TEST_FIRST_NAME, TEST_LAST_NAME, TEST_TO_PAY);
        testUser.setId(TEST_ID);

        userService.createUser(testUser);

        User testUser2 = new User(TEST_USERNAME, TEST_PASSWORD, TEST_FIRST_NAME, TEST_LAST_NAME, TEST_TO_PAY);
        testUser2.setId("testId2");
        userRepository.save(testUser2);
    }

    @Test
    public void getAllUsers() throws Exception {

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    public void getUserByIdTest() throws Exception {

        mockMvc.perform(get(String.format("/users/%s", TEST_ID)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.username", Matchers.is(TEST_USERNAME)))
                .andExpect(jsonPath("$.firstName", Matchers.is(TEST_FIRST_NAME)))
                .andExpect(jsonPath("$.lastName", Matchers.is(TEST_LAST_NAME)))
                .andExpect(jsonPath("$.toPay", Matchers.is(TEST_TO_PAY)));
    }

    @Test
    public void getUserByUnexistingIdTest() throws Exception {

        mockMvc.perform(get("/users/unexistingId"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.message", Matchers.is("user with id = unexistingId not found")));
    }

    @Test
    public void createNewUserTest() throws Exception {

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username", Matchers.is(TEST_USERNAME)))
                .andExpect(jsonPath("$.firstName", Matchers.is(TEST_FIRST_NAME)))
                .andExpect(jsonPath("$.lastName", Matchers.is(TEST_LAST_NAME)))
                .andExpect(jsonPath("$.toPay", Matchers.is(TEST_TO_PAY)));
    }

    @Test
    public void createNewUserWithTooShortUsernameTest() throws Exception {

        User userWithTooShortUsername = testUser;
        userWithTooShortUsername.setUsername("un");

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(userWithTooShortUsername)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message[0].field", Matchers.is("username")))
                .andExpect(jsonPath("$.message[0].message", Matchers.is("Username must be 3 to 50 characters in length.")));
    }

    @Test
    public void createNewUserWithTooShortFirstNameTest() throws Exception {

        User userWithTooShortFirstName = testUser;
        userWithTooShortFirstName.setFirstName("fn");

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(userWithTooShortFirstName)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message[0].field", Matchers.is("firstName")))
                .andExpect(jsonPath("$.message[0].message", Matchers.is("First name must be 3 to 50 characters in length.")));
    }


    @Test
    public void createNewUserWithTooShortLastNameTest() throws Exception {

        User userWithTooShortLastName = testUser;
        userWithTooShortLastName.setLastName("ln");

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(userWithTooShortLastName)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message[0].field", Matchers.is("lastName")))
                .andExpect(jsonPath("$.message[0].message", Matchers.is("Last name must be 3 to 80 characters in length.")));
    }

    @Test
    public void createNewUserWithNullFieldTest() throws Exception {

        User userWithNullField = testUser;
        userWithNullField.setFirstName(null);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(userWithNullField)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message[0].field", Matchers.is("firstName")))
                .andExpect(jsonPath("$.message[0].message", Matchers.is("must not be null")));
    }

    @Test
    public void updateUserTest() throws Exception {

        User updatedUser = testUser;
        updatedUser.setUsername("updatedUsername");

        mockMvc.perform(patch(String.format("/users/%s", TEST_ID))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", Matchers.is("updatedUsername")));
    }

    @Test
    public void updateUserWithUnexistingIdTest() throws Exception {

        User updatedUser = testUser;
        updatedUser.setUsername("updatedUsername");
        updatedUser.setId("wrongId");

        mockMvc.perform(patch("/users/wrongId")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", Matchers.is("user with id = wrongId not found")));
    }

    @Test
    public void updateUserWithMissingFieldTest() throws Exception {

        User updatedUser = testUser;
        updatedUser.setUsername(null);

        mockMvc.perform(patch(String.format("/users/%s", TEST_ID))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message[0].field", Matchers.is("username")))
                .andExpect(jsonPath("$.message[0].message", Matchers.is("must not be null")));
    }

    @Test
    public void deleteByIdTest() throws Exception {

        User testUser3 = new User(TEST_USERNAME, TEST_PASSWORD, TEST_FIRST_NAME, TEST_LAST_NAME, TEST_TO_PAY);
        testUser3.setId("testId3");
        userRepository.save(testUser3);

        mockMvc.perform(delete(String.format("/users/%s", "testId3")))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteByUnexistingIdTest() throws Exception {

        mockMvc.perform(delete("/users/wrongId"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", Matchers.is("user with id = wrongId not found")));
    }

    @After
    public void clean() {
        userService.deleteUserById(TEST_ID);
        userService.deleteUserById("testId2");
    }
}
