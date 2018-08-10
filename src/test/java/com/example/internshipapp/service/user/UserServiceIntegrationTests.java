package com.example.internshipapp.service.user;

import com.example.internshipapp.exception.NoSuchRecordException;
import com.example.internshipapp.model.User;
import com.example.internshipapp.repository.UserRepository;
import com.example.internshipapp.service.UserService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
//@IfProfileValue(name = "tests", value = "tests")
public class UserServiceIntegrationTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    private User testUser;

    private static final String TEST_ID = "aaa";
    private static final String TEST_USERNAME = "testUsername1";
    private static final String TEST_PASSWORD = "testPassword1";
    private static final String TEST_FIRST_NAME = "testFirstName1";
    private static final String TEST_LAST_NAME = "testLastName1";
    private static final double TEST_TO_PAY = 1;

    @Before
    public void setup() {

        MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        testUser = new User(TEST_USERNAME, TEST_PASSWORD, TEST_FIRST_NAME, TEST_LAST_NAME, TEST_TO_PAY);
        testUser.setId(TEST_ID);

        userRepository.save(testUser);
    }

    @Test
    public void getAllUsersTest() {

        List<User> users = userService.getUsers("test", PageRequest.of(0, 2)).getContent();

        Assert.assertEquals(TEST_ID, users.get(0).getId());
        Assert.assertEquals(TEST_USERNAME, users.get(0).getUsername());
        Assert.assertEquals(TEST_PASSWORD, users.get(0).getPassword());
        Assert.assertEquals(TEST_FIRST_NAME, users.get(0).getFirstName());
        Assert.assertEquals(TEST_LAST_NAME, users.get(0).getLastName());
        Assert.assertEquals(TEST_TO_PAY, users.get(0).getToPay(), 0.001);
    }

    @Test
    public void getUserByIdTest() {

        User user = userService.getUserById(TEST_ID);

        Assert.assertEquals(TEST_ID, user.getId());
        Assert.assertEquals(TEST_USERNAME, user.getUsername());
        Assert.assertEquals(TEST_PASSWORD, user.getPassword());
        Assert.assertEquals(TEST_FIRST_NAME, user.getFirstName());
        Assert.assertEquals(TEST_LAST_NAME, user.getLastName());
        Assert.assertEquals(TEST_TO_PAY, user.getToPay(), 0.001);
    }

    @Test(expected = NoSuchRecordException.class)
    public void getUserByUnknownIdShouldThrowNoSuchRecordException() {

        userService.getUserById("wrongId");
    }

    @Test
    public void createUserTest() {

        User testUser2 = testUser;
        testUser2.setId("abc");
        userService.createUser(testUser2);

        User userFromDb = userRepository.getById("abc");

        Assert.assertEquals("abc", userFromDb.getId());
        Assert.assertEquals(TEST_USERNAME, userFromDb.getUsername());
        Assert.assertEquals(TEST_PASSWORD, userFromDb.getPassword());
        Assert.assertEquals(TEST_FIRST_NAME, userFromDb.getFirstName());
        Assert.assertEquals(TEST_LAST_NAME, userFromDb.getLastName());
        Assert.assertEquals(TEST_TO_PAY, userFromDb.getToPay(), 0.001);
    }

    @Test
    public void updateUserTest() {

        User updatedUser = testUser;
        updatedUser.setUsername("updatedUsername");
        userService.updateUser(updatedUser);

        User userFromDb = userRepository.getById(TEST_ID);

        Assert.assertEquals(TEST_ID, userFromDb.getId());
        Assert.assertEquals("updatedUsername", userFromDb.getUsername());
    }

    @Test(expected = NoSuchRecordException.class)
    public void updateUnexistingUser() {

        User updatedUser = testUser;
        updatedUser.setId("wrongId");
        userService.updateUser(updatedUser);
    }

    @Test
    public void deleteById() {

        userService.deleteUserById(TEST_ID);
    }

    @Test(expected = NoSuchRecordException.class)
    public void deleteUnexistingUserShouldThrowNoSuchRecordException() {

        userService.deleteUserById("wrongId");
    }

    @After
    public void clean() {

        userRepository.deleteById(TEST_ID);
        userRepository.deleteById("abc");
    }

}
