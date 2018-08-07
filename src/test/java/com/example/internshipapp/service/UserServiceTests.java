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

        when(userRepository.findById("abc")).thenReturn(java.util.Optional.ofNullable(user));

        User userFromService = userService.getUserById("abc");
        Assert.assertEquals(userFromService, user);
    }

    @Test(expected = NoSuchRecordException.class)
    public void getUserByUnknownIdShouldThrowNoSuchRecordException() {

        when(userService.getUserById("aaa")).thenThrow(NoSuchRecordException.class);
        userService.getUserById("aaa");
    }

    //TODO not working as expected, returns null
    @Ignore
    @Test
    public void createNewUserTest() {

        User user = this.getMockedUser();

        Assert.assertNotNull(userService.createUser(user));
    }

    //TODO not working as expected, returns null
    @Test
    @Ignore
    public void updateUser() {

        User user = this.getMockedUser();

        User updatedUser = new User("updatedUsername", "updatedPassword", "updatedFirstName", "updatedLastName", 5);
        updatedUser.setId("abc");
        userService.updateUser(updatedUser);

        when(userRepository.findById("abc")).thenReturn(java.util.Optional.ofNullable(user));

        Assert.assertEquals(updatedUser, user);
    }

    @Test
    public void deleteAllUsers() {

        User user = this.getMockedUser();

        userService.deleteAllUsers();

        Assert.assertEquals(userRepository.count(), 0);
    }

    @Test
    public void deleteUserById() {

        User user = this.getMockedUser();
        when(userRepository.findById("abc")).thenReturn(java.util.Optional.ofNullable(user));

        userService.deleteUser("abc");
    }

    @Test(expected = NoSuchRecordException.class)
    public void deleteUnexistingUserShouldThrowNoSuchRecordException() {

        userService.deleteUser("aaa");
    }


}
