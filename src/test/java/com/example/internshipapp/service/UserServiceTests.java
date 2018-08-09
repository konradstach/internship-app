package com.example.internshipapp.service;

import com.example.internshipapp.controller.RestResponseEntityExceptionHandler;
import com.example.internshipapp.exception.NoSuchRecordException;
import com.example.internshipapp.model.User;
import com.example.internshipapp.repository.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
public class UserServiceTests {

    private MockMvc mockMvc;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private static final String TEST_ID = "abc";
    private static final String TEST_USERNAME = "testUsername";
    private static final String TEST_PASSWORD = "testPassword";
    private static final String TEST_FIRST_NAME = "testFirstName";
    private static final String TEST_LAST_NAME = "testLastName";
    private static final double TEST_TO_PAY = 8;


    private User getMockedUser() {
        User user = new User(TEST_USERNAME, TEST_PASSWORD, TEST_FIRST_NAME, TEST_LAST_NAME, TEST_TO_PAY);
        user.setId(TEST_ID);
        return user;
    }

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
                        this.getMockedUser()));

        when(userRepository.findByUsernameContainingIgnoreCase("test", new PageRequest(0, 2)))
                .thenReturn(users);

        Assert.assertEquals(users, userService.getUsers("test", new PageRequest(0, 2)));
    }

    @Test
    public void getUserByIdTest() {

        User user = this.getMockedUser();

        when(userRepository.findById(TEST_ID)).thenReturn(java.util.Optional.ofNullable(user));

        User userFromService = userService.getUserById(TEST_ID);
        Assert.assertEquals(userFromService, user);
    }

    @Test(expected = NoSuchRecordException.class)
    public void getUserByUnknownIdShouldThrowNoSuchRecordException() {

        when(userRepository.findById("aaa")).thenThrow(NoSuchRecordException.class);
        userService.getUserById("aaa");
    }

    @Test
    public void createNewUserTest() {

        User user = this.getMockedUser();
        when(userRepository.save(any(User.class))).thenReturn(this.getMockedUser());

        Assert.assertNotNull(userService.createUser(user));
    }

    @Test
    public void updateUser() {

        User user = this.getMockedUser();
        when(userRepository.findById(TEST_ID)).thenReturn(java.util.Optional.ofNullable(user));

        User updatedUser = new User("updatedUsername", "updatedPassword", "updatedFirstName", "updatedLastName", 5);
        updatedUser.setId(TEST_ID);

        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        userService.updateUser(updatedUser);

        Assert.assertEquals("updatedUsername", updatedUser.getUsername());
        Assert.assertEquals("updatedPassword", updatedUser.getPassword());
        Assert.assertEquals("updatedFirstName", updatedUser.getFirstName());
        Assert.assertEquals("updatedLastName", updatedUser.getLastName());
        Assert.assertEquals(5, updatedUser.getToPay(), 0.001);
    }

    @Test(expected = NoSuchRecordException.class)
    public void updateUnexistingUser(){

        User user = this.getMockedUser();
        user.setId("aaa");
        userService.updateUser(user);
    }

    @Test
    public void deleteAllUsers() {

        userService.deleteAllUsers();

        Assert.assertEquals(userRepository.count(), 0);
    }

    @Test
    public void deleteUserById() {

        User user = this.getMockedUser();
        when(userRepository.findById(TEST_ID)).thenReturn(java.util.Optional.ofNullable(user));

        userService.deleteUser(TEST_ID);
    }

    @Test(expected = NoSuchRecordException.class)
    public void deleteUnexistingUserShouldThrowNoSuchRecordException() {

        userService.deleteUser("aaa");
    }


}
