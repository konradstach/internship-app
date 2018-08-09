package com.example.internshipapp.controller;

import com.example.internshipapp.dto.BookingDto;
import com.example.internshipapp.exception.NoSuchRecordException;
import com.example.internshipapp.service.BookingService;
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
public class BookingControllerTests {

    private MockMvc mockMvc;

    @InjectMocks
    private BookingController bookingController;

    @Mock
    private BookingService bookingService;

    private static final String TEST_START_OF_BOOKING = "08-08-2018 12:05";
    private static final String TEST_END_OF_BOOKING = "08-08-2018 13:35";

    private BookingDto getMockedBooking() {
        BookingDto booking = new BookingDto(TEST_START_OF_BOOKING, TEST_END_OF_BOOKING);
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

    @Test
    public void getAllBookingsTest() throws Exception {

        BookingDto booking = this.getMockedBooking();
        List<BookingDto> bookingList = new ArrayList<>();
        bookingList.add(booking);

        PageImpl<BookingDto> bookings = new PageImpl<>(bookingList);

        when(bookingService.getBookings(Mockito.any(Pageable.class)))
                .thenReturn(bookings);

        mockMvc.perform(get("http://localhost:8080/bookings"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.content[0].startOfBooking", Matchers.is(TEST_START_OF_BOOKING)))
                .andExpect(jsonPath("$.content[0].endOfBooking", Matchers.is(TEST_END_OF_BOOKING)));
    }

    @Test
    public void getBookingByIdTest() throws Exception {

        BookingDto booking = this.getMockedBooking();

        given(bookingService.getBookingById(booking.getId())).willReturn(booking);

        mockMvc.perform(get("http://localhost:8080/bookings/{id}", booking.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.startOfBooking", Matchers.is(TEST_START_OF_BOOKING)))
                .andExpect(jsonPath("$.endOfBooking", Matchers.is(TEST_END_OF_BOOKING)));
    }

    @Test
    public void getBookingByIdWithUnknownIdTest() throws Exception {
        doThrow(NoSuchRecordException.class).when(bookingService).getBookingById("def");
        mockMvc.perform(get("/bookings/def"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void createBookingTest() throws Exception {

        BookingDto booking = this.getMockedBooking();

        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(post("/bookings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(booking)))
                .andExpect(status().isCreated());
    }

    @Test
    public void updateBookingTest() throws Exception {
        BookingDto booking = this.getMockedBooking();

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
    public void deleteBookingByIdTest() throws Exception {

        mockMvc.perform(delete("/bookings/{id}", "abc")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
