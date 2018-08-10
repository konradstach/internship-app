package com.example.internshipapp.service.booking;

import com.example.internshipapp.dto.BookingDto;
import com.example.internshipapp.exception.NoSuchRecordException;
import com.example.internshipapp.mapper.BookingMapper;
import com.example.internshipapp.model.Booking;
import com.example.internshipapp.repository.BookingRepository;
import com.example.internshipapp.service.BookingService;
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

import java.time.LocalDateTime;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
//@IfProfileValue(name = "tests", value = "tests")
public class BookingServiceIntegrationTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private BookingMapper bookingMapper;

    private static final String TEST_ID = "aaa";
    private static final LocalDateTime TEST_START_OF_BOOKING = LocalDateTime.of(2018, 8, 8, 12, 30);
    private static final LocalDateTime TEST_END_OF_BOOKING = LocalDateTime.of(2018, 8, 8, 14, 10);

    private static final String ID = "bbb";
    private static final String START_OF_BOOKING = "08-08-2018 12:30";
    private static final String END_OF_BOOKING = "08-08-2018 14:10";

    @Before
    public void setup() {

        MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

        Booking testBooking = new Booking(TEST_START_OF_BOOKING, TEST_END_OF_BOOKING);
        testBooking.setId(TEST_ID);

        bookingRepository.save(testBooking);
    }

    @Test
    public void getAllBookingsTest() {

        List<BookingDto> bookings = bookingService.getBookings(PageRequest.of(0, 2)).getContent();

        Assert.assertEquals(TEST_ID, bookings.get(0).getId());
        Assert.assertEquals(START_OF_BOOKING, bookings.get(0).getStartOfBooking());
        Assert.assertEquals(END_OF_BOOKING, bookings.get(0).getEndOfBooking());
    }

    @Test
    public void getBookingByIdTest() {

        BookingDto booking = bookingService.getBookingById(TEST_ID);

        Assert.assertEquals(TEST_ID, booking.getId());
        Assert.assertEquals(START_OF_BOOKING, booking.getStartOfBooking());
        Assert.assertEquals(END_OF_BOOKING, booking.getEndOfBooking());
    }

    @Test(expected = NoSuchRecordException.class)
    public void getBookingByUnknownIdShouldThrowNoSuchRecordException() {

        bookingService.getBookingById("wrongId");
    }

    @Test
    public void createBookingTest() {

        BookingDto testBookingDto = new BookingDto(START_OF_BOOKING, END_OF_BOOKING);
        testBookingDto.setId("bbb");
        bookingService.createBooking(testBookingDto);

        BookingDto bookingFromDb = bookingMapper.toDto(bookingRepository.getById("bbb"));

        Assert.assertEquals(ID, bookingFromDb.getId());
        Assert.assertEquals(START_OF_BOOKING, bookingFromDb.getStartOfBooking());
        Assert.assertEquals(END_OF_BOOKING, bookingFromDb.getEndOfBooking());
    }

    @Test
    public void updateBookingTest() {

        BookingDto updatedBooking = new BookingDto(START_OF_BOOKING, END_OF_BOOKING);
        updatedBooking.setId("aaa");
        updatedBooking.setStartOfBooking("08-08-2018 13:30");
        bookingService.updateBooking(updatedBooking);

        BookingDto bookingFromDb = bookingMapper.toDto(bookingRepository.getById(TEST_ID));

        Assert.assertEquals(TEST_ID, bookingFromDb.getId());
        Assert.assertEquals("08-08-2018 13:30", bookingFromDb.getStartOfBooking());
    }

    @Test(expected = NoSuchRecordException.class)
    public void updateUnexistingBokoing() {

        BookingDto updatedBooking = new BookingDto(START_OF_BOOKING, END_OF_BOOKING);
        updatedBooking.setId("wrongId");
        bookingService.updateBooking(updatedBooking);
    }

    @Test
    public void deleteById() {

        bookingService.deleteBookingById(TEST_ID);
    }

    @Test(expected = NoSuchRecordException.class)
    public void deleteUnexistingBookingShouldThrowNoSuchRecordException() {

        bookingService.deleteBookingById("wrongId");
    }

    @After
    public void clean() {

        bookingRepository.deleteById(TEST_ID);
        bookingRepository.deleteById("bbb");
        bookingRepository.deleteById("ccc");
    }
}
