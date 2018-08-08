package com.example.internshipapp.controller;

import com.example.internshipapp.exception.NoSuchRecordException;
import com.example.internshipapp.model.Booking;
import com.example.internshipapp.service.BookingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Ignore;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class BookingControllerTests {

    private MockMvc mockMvc;

    @InjectMocks
    private BookingController bookingController;

    @Mock
    private BookingService bookingService;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    String TEST_START_OF_BOOKING = "2018-08-08 12:05";
    LocalDateTime FORMATTED_START_OF_BOOKING = LocalDateTime.parse(TEST_START_OF_BOOKING, formatter);

    String TEST_END_OF_BOOKING = "2018-08-08 13:35";
    LocalDateTime FORMATTED_END_OF_BOOKING = LocalDateTime.parse(TEST_END_OF_BOOKING, formatter);


    private Booking getMockedBooking() {
        Booking booking = new Booking(FORMATTED_START_OF_BOOKING, FORMATTED_END_OF_BOOKING);
        booking.setId("abc");
        return booking;
    }

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookingController)
                .setControllerAdvice(new RestResponseEntityExceptionHandler())
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Ignore
    @Test
    public void getAllBookingsTest() throws Exception {

        Booking booking = this.getMockedBooking();
        List<Booking> allBookings = new ArrayList<>();
        allBookings.add(booking);

        PageImpl<Booking> bookings = new PageImpl<>(allBookings);

        when(bookingService.getBookings(Mockito.any(Pageable.class)))
                .thenReturn(bookings);

        mockMvc.perform(get("http://localhost:8080/bookings"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.content[0].startOfBooking", Matchers.is("[2018,8,8,12,5]")))
                .andExpect(jsonPath("$.content[0].endOfBooking", Matchers.is("[2018,8,8,13,35]")));
    }

    @Ignore
    @Test
    public void getBookingByIdTest() throws Exception {

        Booking booking = this.getMockedBooking();

        given(bookingService.getBookingById(booking.getId())).willReturn(booking);

        mockMvc.perform(get("http://localhost:8080/bookings/{id}", booking.getId()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.startOfBooking", Matchers.is("2018-08-08T12:05:00.0000000")))
                .andExpect(jsonPath("$.endOfBooking", Matchers.is("2018-08-08T13:35:00.0000000")));
    }

    @Test
    public void getBookingByIdWithUnknownIdTest() throws Exception {
        doThrow(NoSuchRecordException.class).when(bookingService).getBookingById("def");
        mockMvc.perform(get("/bookings/def"))
                .andExpect(status().isNotFound());
    }

    @Ignore
    @Test
    public void createBookingTest() throws Exception {

        Booking booking = this.getMockedBooking();

        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(post("/bookings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(booking)))
                .andExpect(status().isCreated());
    }

    @Ignore
    @Test
    public void updateTest() throws Exception {
        Booking booking = this.getMockedBooking();

        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(patch("http://localhost:8080/bookings/abc")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(booking)))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteBookingWithUnknownIdTest() throws Exception {
        doThrow(NoSuchRecordException.class).when(bookingService).deleteBooking("def");
        mockMvc.perform(delete("/bookings/{id}", "def")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteUserByIdTest() throws Exception {

        Booking booking = this.getMockedBooking();

        mockMvc.perform(delete("/bookings/{id}", "abc")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
