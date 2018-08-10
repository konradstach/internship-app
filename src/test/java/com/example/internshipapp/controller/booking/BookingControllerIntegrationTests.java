package com.example.internshipapp.controller.booking;

import com.example.internshipapp.dto.BookingDto;
import com.example.internshipapp.model.Booking;
import com.example.internshipapp.repository.BookingRepository;
import com.example.internshipapp.service.BookingService;
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

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
//@IfProfileValue(name = "tests", value = "tests")
public class BookingControllerIntegrationTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private BookingRepository bookingRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mockMvc;

    private static final String TEST_ID = "aaa";
    private static final LocalDateTime TEST_START_OF_BOOKING = LocalDateTime.of(2018, 8, 8, 12, 30);
    private static final LocalDateTime TEST_END_OF_BOOKING = LocalDateTime.of(2018, 8, 8, 14, 10);

    private static final String ID = "bbb";
    private static final String START_OF_BOOKING = "08-08-2018 12:30";
    private static final String END_OF_BOOKING = "08-08-2018 14:10";

    @Before
    public void setup() {

        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        Booking testBooking = new Booking(TEST_START_OF_BOOKING, TEST_END_OF_BOOKING);
        testBooking.setId(TEST_ID);

        bookingRepository.save(testBooking);
    }

    @Test
    public void getAllBookings() throws Exception {

        mockMvc.perform(get("/bookings"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    public void getBookingByIdTest() throws Exception {

        mockMvc.perform(get(String.format("/bookings/%s", TEST_ID)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.startOfBooking", Matchers.is(START_OF_BOOKING)))
                .andExpect(jsonPath("$.endOfBooking", Matchers.is(END_OF_BOOKING)));
    }

    @Test
    public void getBookingByUnexistingIdTest() throws Exception {

        mockMvc.perform(get("/bookings/unexistingId"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.message", Matchers.is("Booking with id = unexistingId not found")));
    }

    @Test
    public void createNewBookingTest() throws Exception {

        BookingDto bookingDto = new BookingDto(START_OF_BOOKING, END_OF_BOOKING);
        bookingDto.setId(ID);

        mockMvc.perform(post("/bookings")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(bookingDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.startOfBooking", Matchers.is(START_OF_BOOKING)))
                .andExpect(jsonPath("$.endOfBooking", Matchers.is(END_OF_BOOKING)));
    }

    @Test
    public void createNewBookingWithInvalidDateSyntaxTest() throws Exception {

        BookingDto bookingDto = new BookingDto(START_OF_BOOKING, END_OF_BOOKING);
        bookingDto.setId("ccc");
        bookingDto.setStartOfBooking("0000-1111");

        mockMvc.perform(post("/bookings")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(bookingDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message[0].field", Matchers.is("startOfBooking")))
                .andExpect(jsonPath("$.message[0].message", Matchers.is("Invalid date, correct format is: dd-MM-yyyy HH:mm")));
    }

    @Test
    public void updateBookingTest() throws Exception {

        BookingDto updatedBookingDto = new BookingDto(START_OF_BOOKING, END_OF_BOOKING);
        updatedBookingDto.setId(TEST_ID);

        updatedBookingDto.setStartOfBooking("08-08-2018 12:40");

        mockMvc.perform(patch(String.format("/bookings/%s", TEST_ID))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(updatedBookingDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.startOfBooking", Matchers.is("08-08-2018 12:40")));
    }

    @Test
    public void updateBookingWithUnexistingIdTest() throws Exception {

        BookingDto updatedBookingDto = new BookingDto(START_OF_BOOKING, END_OF_BOOKING);
        updatedBookingDto.setId("wrongId");

        mockMvc.perform(patch("/bookings/wrongId")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(updatedBookingDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", Matchers.is("Booking with id = wrongId not found")));
    }

    @Test
    public void deleteByIdTest() throws Exception {

        Booking testBooking = new Booking(TEST_START_OF_BOOKING, TEST_END_OF_BOOKING);
        testBooking.setId("toDelete");
        bookingRepository.save(testBooking);

        mockMvc.perform(delete(String.format("/bookings/%s", "toDelete")))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteByUnexistingIdTest() throws Exception {

        mockMvc.perform(delete("/bookings/wrongId"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", Matchers.is("Booking with id = wrongId not found")));
    }

    @After
    public void clean() {
        bookingService.deleteBookingById(TEST_ID);
    }

}
