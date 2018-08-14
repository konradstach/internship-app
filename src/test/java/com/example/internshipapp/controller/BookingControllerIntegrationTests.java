package com.example.internshipapp.controller;

import com.example.internshipapp.dto.BookingDto;
import com.example.internshipapp.exception.FieldValidationErrorResponse;
import com.example.internshipapp.exception.NoSuchRecordException;
import com.example.internshipapp.model.Booking;
import com.example.internshipapp.repository.BookingRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@IfProfileValue(name = "tests", value = "tests")
public class BookingControllerIntegrationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private BookingRepository bookingRepository;

    private BookingDto testBookingDto;
    private HttpHeaders headers;


    private static final String TEST_ID = "testId1";
    private static final LocalDateTime TEST_START_OF_BOOKING = LocalDateTime.of(2018, 8, 8, 12, 30);
    private static final LocalDateTime TEST_END_OF_BOOKING = LocalDateTime.of(2018, 8, 8, 14, 10);

    private static final String ID = "testId2";
    private static final String START_OF_BOOKING = "08-08-2018 12:30";
    private static final String END_OF_BOOKING = "08-08-2018 14:10";

    @Before
    public void setup() {

        headers = new HttpHeaders();
        MediaType mediaType = new MediaType("application", "merge-patch+json");
        headers.setContentType(mediaType);

        Booking testBooking = new Booking(TEST_START_OF_BOOKING, TEST_END_OF_BOOKING);
        testBooking.setId(TEST_ID);

        bookingRepository.save(testBooking);

        testBookingDto = new BookingDto(START_OF_BOOKING, END_OF_BOOKING);
        testBookingDto.setId(ID);
    }

    @Test
    public void getAllBookings() {

        ResponseEntity<BookingDto> bookingResponse = restTemplate.getForEntity("http://localhost:8080/bookings/testId1", BookingDto.class);
        assertThat(bookingResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void getBookingByIdTest() {

        ResponseEntity<BookingDto> bookingResponse = restTemplate.getForEntity("http://localhost:8080/bookings/testId1", BookingDto.class);

        assertThat(bookingResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(bookingResponse.getBody().equals(new BookingDto(START_OF_BOOKING, END_OF_BOOKING)));
    }

    @Test
    public void getBookingByUnexistingIdTest() {

        ResponseEntity<NoSuchRecordException> bookingResponse = restTemplate.getForEntity("http://localhost:8080/bookings/wrongId", NoSuchRecordException.class);

        assertThat(bookingResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(bookingResponse.getBody()).hasFieldOrPropertyWithValue("message", "Booking with id = wrongId not found");
    }

    @Test
    public void createNewBookingTest() {

        ResponseEntity<BookingDto> bookingResponse = restTemplate.postForEntity("http://localhost:8080/bookings", testBookingDto, BookingDto.class);

        assertThat(bookingResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(bookingResponse.getBody().getStartOfBooking().equals(START_OF_BOOKING));
        assertThat(bookingResponse.getBody().getEndOfBooking().equals(END_OF_BOOKING));
    }

    @Test
    public void createNewBookingWithInvalidFieldTest() {

        BookingDto bookingWithInvalidField = testBookingDto;
        testBookingDto.setStartOfBooking("0000-0000");
        testBookingDto.setId("testId3");
        ResponseEntity<ArrayList> bookingResponse = restTemplate.postForEntity("http://localhost:8080/bookings", bookingWithInvalidField, ArrayList.class);

        assertThat(bookingResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(bookingResponse.getBody().get(0).equals(new FieldValidationErrorResponse("startOfBooking", "Invalid date, correct format is: dd-MM-yyyy HH:mm")));
    }

    @Test
    public void updateBookingTest() {

        BookingDto bookingToUpdate = new BookingDto(START_OF_BOOKING, END_OF_BOOKING);
        bookingToUpdate.setId(TEST_ID);
        bookingToUpdate.setStartOfBooking("08-08-2018 12:50");

        HttpEntity<BookingDto> requestEntity = new HttpEntity<>(bookingToUpdate, headers);

        ResponseEntity<BookingDto> bookingResponse = restTemplate.exchange("http://localhost:8080/bookings/testId1", HttpMethod.PATCH, requestEntity, BookingDto.class);

        assertThat(bookingResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(bookingResponse.getBody().equals(bookingToUpdate));
    }

    @Test
    public void deleteByIdTest() {

        restTemplate.delete("http://localhost:8080/bookings/testId1");
    }

    @After
    public void clean() {
        bookingRepository.deleteById(TEST_ID);
        bookingRepository.deleteById(ID);
    }

}
