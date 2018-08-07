package com.example.internshipapp.service;


import com.example.internshipapp.exception.RestResponseEntityExceptionHandler;
import com.example.internshipapp.exception.NoSuchRecordException;
import com.example.internshipapp.model.User;
import com.example.internshipapp.repository.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {TestConfiguration.class})
public class UserServiceTests {

    private MockMvc mockMvc;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userService)
                .setControllerAdvice(new RestResponseEntityExceptionHandler())
                .build();
    }

    @Test
    public void getAllUsersByNameTest() {

        PageImpl<User> users = new PageImpl<>(
                Arrays.asList(
                        new User("testUsername", "testPassword", "testFirstName", "testLastName", 8, null, null)));

        when(userRepository.findByUsernameContainingIgnoreCase("test", new PageRequest(0, 2)))
                .thenReturn(users);

        Assert.assertEquals(users, userService.getUsers("test", new PageRequest(0, 2)));
    }

    @Test
    public void getUserByIdTest() {

        User user = new User("testUsername", "testPassword", "testFirstName", "testLastName", 8, null, null);
        user.setId("abc");

        when(userRepository.findById("abc")).thenReturn(java.util.Optional.ofNullable(user));

        User userFromService = userService.getUserById("abc");
        Assert.assertEquals(userFromService, user);
    }


    @Test(expected = NoSuchRecordException.class)
    public void getUserByUnknownIdShouldThrowNoSuchRecordException() {

        when(userService.getUserById("aaa")).thenThrow(NoSuchRecordException.class);
        userService.getUserById("aaa");
    }

    //TODO
    //not working as expected, returns null
    @Test
    public void createNewUserTest() {

        User user = new User("testUsername", "testPassword", "testFirstName", "testLastName", 8, null, null);
        user.setId("abc");

        Assert.assertNotNull(userService.createUser(user));
    }

    //TODO
    //not working as expected, returns null
    @Test
    public void updateUser() {

        User user = new User("testUsername", "testPassword", "testFirstName", "testLastName", 8, null, null);
        user.setId("abc");

        User updatedUser = new User("updatedUsername", "updatedPassword", "updatedFirstName", "updatedLastName", 5, null, null);
        updatedUser.setId("abc");
        userService.updateUser(updatedUser);

        when(userRepository.findById("abc")).thenReturn(java.util.Optional.ofNullable(user));

        Assert.assertEquals(updatedUser, user);
    }

    @Test
    public void deleteAllUsers() {

        User user = new User("testUsername", "testPassword", "testFirstName", "testLastName", 8, null, null);
        user.setId("abc");

        userService.deleteAllUsers();

        Assert.assertEquals(userRepository.count(), 0);
    }


    @Test
    public void deleteUserById() {

        User user = new User("testUsername", "testPassword", "testFirstName", "testLastName", 8, null, null);
        user.setId("abc");

        when(userRepository.findById("abc")).thenReturn(java.util.Optional.ofNullable(user));

        userService.deleteUser("abc");
    }


    @Test(expected = NoSuchRecordException.class)
    public void deleteUnexistingUserShouldThrowNoSuchRecordException() {

        userService.deleteUser("aaa");
    }


}
