package com.example.internshipapp.controller;

import com.example.internshipapp.exception.FieldValidationErrorResponse;
import com.example.internshipapp.exception.NoSuchRecordException;
import com.example.internshipapp.model.User;
import com.example.internshipapp.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@IfProfileValue(name = "tests", value = "tests")
public class UserControllerIntegrationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    private User testUser;
    private User userToCreate;
    private HttpHeaders headers;

    private static final String TEST_ID = "testId1";
    private static final String TEST_ID2 = "testId2";
    private static final String TEST_USERNAME = "testUsername1";
    private static final String TEST_PASSWORD = "testPassword1";
    private static final String TEST_FIRST_NAME = "testFirstName1";
    private static final String TEST_LAST_NAME = "testLastName1";
    private static final double TEST_TO_PAY = 1;

    @Before
    public void setup() {

        RestTemplate restTemplate = new RestTemplate();

        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setConnectTimeout(5000);
        requestFactory.setReadTimeout(5000);

        restTemplate.setRequestFactory(requestFactory);

        headers = new HttpHeaders();
        MediaType mediaType = new MediaType("application", "merge-patch+json");
        headers.setContentType(mediaType);

        testUser = new User(TEST_USERNAME, TEST_PASSWORD, TEST_FIRST_NAME, TEST_LAST_NAME, TEST_TO_PAY);
        testUser.setId(TEST_ID);
        userRepository.save(testUser);

        User testUser2 = new User(TEST_USERNAME, TEST_PASSWORD, TEST_FIRST_NAME, TEST_LAST_NAME, 2);
        testUser2.setId(TEST_ID2);
        userRepository.save(testUser2);

        userToCreate = new User(TEST_USERNAME, TEST_PASSWORD, TEST_FIRST_NAME, TEST_LAST_NAME, 3);
        userToCreate.setId("testId3");
    }

    @Test
    public void getAllUsers() {

        ResponseEntity<User> userResponse = restTemplate.getForEntity("http://localhost:8080/users", User.class);
        assertThat(userResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void getUserByIdTest() {

        ResponseEntity<User> userResponse = restTemplate.getForEntity("http://localhost:8080/users/testId1", User.class);

        assertThat(userResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(userResponse.getBody().equals(new User(TEST_USERNAME, TEST_PASSWORD, TEST_FIRST_NAME, TEST_LAST_NAME, TEST_TO_PAY)));
    }

    @Test
    public void getUserByUnexistingIdTest() {

        ResponseEntity<NoSuchRecordException> userResponse = restTemplate.getForEntity("http://localhost:8080/users/wrongId", NoSuchRecordException.class);

        assertThat(userResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(userResponse.getBody()).hasFieldOrPropertyWithValue("message", "user with id = wrongId not found");
    }

    @Test
    public void getUserByUsernameTest() {

        ResponseEntity<User> userResponse = restTemplate.getForEntity("http://localhost:8080/users/?username=testId1", User.class);

        assertThat(userResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(userResponse.getBody().equals(new User(TEST_USERNAME, TEST_PASSWORD, TEST_FIRST_NAME, TEST_LAST_NAME, TEST_TO_PAY)));
    }

    @Test
    public void getUserByUnexistingUsernameTest() {

        ResponseEntity<User> userResponse = restTemplate.getForEntity("http://localhost:8080/users/?username=wrongUsername", User.class);

        assertThat(userResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(userResponse.getBody().getId()).isNull();
        assertThat(userResponse.getBody().getUsername()).isNull();
        assertThat(userResponse.getBody().getFirstName()).isNull();
        assertThat(userResponse.getBody().getLastName()).isNull();
    }

    @Test
    public void createNewUserTest() {

        ResponseEntity<User> userResponse = restTemplate.postForEntity("http://localhost:8080/users", userToCreate, User.class);

        assertThat(userResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(userResponse.getBody().equals(userToCreate));
    }

    @Test
    public void createNewUserWithTooShortFieldTest() {

        User userWithTooShortField = userToCreate;
        userToCreate.setUsername("un");
        userToCreate.setId("testId4");

        ResponseEntity<ArrayList> userResponse = restTemplate.postForEntity("http://localhost:8080/users", userWithTooShortField, ArrayList.class);

        assertThat(userResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(userResponse.getBody().get(0).equals(new FieldValidationErrorResponse("username", "Username must be 3 to 50 characters in length.")));
    }

    @Test
    public void updateUserTest() {

        User userToUpdate = testUser;
        userToUpdate.setUsername("updated");

        HttpEntity<User> requestEntity = new HttpEntity<>(userToUpdate, headers);

        ResponseEntity<User> userResponse = restTemplate.exchange("http://localhost:8080/users/testId1", HttpMethod.PATCH, requestEntity, User.class);

        assertThat(userResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(userResponse.getBody().equals(userToUpdate));
    }

    @Test
    public void deleteByIdTest() {

        restTemplate.delete("http://localhost:8080/users/testId2");
    }

    @After
    public void clean() {
        userRepository.deleteById(TEST_ID);
        userRepository.deleteById(TEST_ID2);
        userRepository.deleteById("testId3");
    }
}
