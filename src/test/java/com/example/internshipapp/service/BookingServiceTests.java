package com.example.internshipapp.service;

import com.example.internshipapp.dto.BookingDto;
import com.example.internshipapp.exception.NoSuchRecordException;
import com.example.internshipapp.mapper.BookingMapper;
import com.example.internshipapp.model.Booking;
import com.example.internshipapp.repository.BookingRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
public class BookingServiceTests {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private BookingMapper bookingMapper;

    @InjectMocks
    private BookingService bookingService;

    private static final String TEST_ID = "abc";
    private static final LocalDateTime TEST_START_OF_BOOKING = LocalDateTime.of(2018, 8, 8, 12, 30);
    private static final LocalDateTime TEST_END_OF_BOOKING = LocalDateTime.of(2018, 8, 8, 14, 10);

    private Booking getMockedBooking() {
        Booking booking = new Booking(TEST_START_OF_BOOKING, TEST_END_OF_BOOKING);
        booking.setId(TEST_ID);
        return booking;
    }

    @Before
    public void setUp() {

        when(bookingRepository.findById(TEST_ID)).thenReturn(java.util.Optional.ofNullable(this.getMockedBooking()));
        when(bookingRepository.getById(TEST_ID)).thenReturn(this.getMockedBooking());
        when(bookingMapper.toDto(any(Booking.class))).thenCallRealMethod();
        when(bookingMapper.toEntity(any(BookingDto.class))).thenCallRealMethod();
    }

    @Test
    public void getAllBookingsTest() {

        when(bookingMapper.pageToDtos(any())).thenCallRealMethod();

        PageImpl<Booking> bookingPage = new PageImpl<>(
                Arrays.asList(this.getMockedBooking()));

        when(bookingRepository.findAll(new PageRequest(0, 2)))
                .thenReturn(bookingPage);

        BookingDto bookingDto = bookingService.getBookings(new PageRequest(0, 2)).getContent().get(0);

        Assert.assertEquals(TEST_ID, bookingDto.getId());
        Assert.assertEquals("08-08-2018 12:30", bookingDto.getStartOfBooking());
        Assert.assertEquals("08-08-2018 14:10", bookingDto.getEndOfBooking());
    }

    @Test
    public void getBookingByIdTest() {

        BookingDto bookingFromService = bookingService.getBookingById(TEST_ID);
        Assert.assertEquals(TEST_ID, bookingFromService.getId());
        Assert.assertEquals("08-08-2018 12:30", bookingFromService.getStartOfBooking());
        Assert.assertEquals("08-08-2018 14:10", bookingFromService.getEndOfBooking());
    }

    @Test(expected = NoSuchRecordException.class)
    public void getBookingByUnknownIdShouldThrowNoSuchRecordException() {

        when(bookingService.getBookingById("aaa")).thenThrow(NoSuchRecordException.class);
        bookingService.getBookingById("aaa");
    }

    @Test
    public void createNewBookingTest() {

        when(bookingMapper.toDto(any(Booking.class))).thenCallRealMethod();
        when(bookingRepository.save(any(Booking.class))).thenReturn(this.getMockedBooking());

        Booking booking = this.getMockedBooking();

        BookingDto bookingDto = bookingMapper.toDto(booking);

        BookingDto bookingFromService = bookingService.createBooking(bookingDto);

        Assert.assertEquals(TEST_ID, bookingFromService.getId());
        Assert.assertEquals("08-08-2018 12:30", bookingFromService.getStartOfBooking());
        Assert.assertEquals("08-08-2018 14:10", bookingFromService.getEndOfBooking());
    }

    @Test
    public void updateBookingTest() {

        when(bookingMapper.toDto(any(Booking.class))).thenCallRealMethod();

        Booking booking = this.getMockedBooking();

        BookingDto bookingDto = bookingMapper.toDto(booking);

        bookingService.updateBooking(bookingDto);
    }

    @Test(expected = NoSuchRecordException.class)
    public void updateUnexistingBookingTest() {

        when(bookingMapper.toDto(any(Booking.class))).thenCallRealMethod();
        Booking booking = this.getMockedBooking();
        BookingDto bookingDto = bookingMapper.toDto(booking);
        bookingDto.setId("aaa");
        bookingService.updateBooking(bookingDto);
    }

    @Test
    public void deleteAllBookingsTest() {

        bookingService.deleteAllBookings();

        Assert.assertEquals(bookingRepository.count(), 0);
    }

    @Test
    public void deleteBookingByIdTest() {

        Booking booking = this.getMockedBooking();
        when(bookingRepository.getById(TEST_ID)).thenReturn(booking);

        bookingService.deleteBooking(TEST_ID);

    }

    @Test(expected = NoSuchRecordException.class)
    public void deleteUnexistingUserShouldThrowNoSuchRecordException() {

        doThrow(NoSuchRecordException.class).when(bookingRepository).deleteById("aaa");
        bookingService.deleteBooking("aaa");
    }
}
