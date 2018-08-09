package com.example.internshipapp.service;

import com.example.internshipapp.dto.BookingDto;
import com.example.internshipapp.exception.NoSuchRecordException;
import com.example.internshipapp.mapper.BookingMapper;
import com.example.internshipapp.model.Booking;
import com.example.internshipapp.repository.BookingRepository;
import io.swagger.annotations.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BookingService {

    private BookingMapper bookingMapper;
    private BookingRepository bookingRepository;
    private static final Logger logger = LoggerFactory.getLogger(BookingService.class);

    public BookingService(BookingMapper bookingMapper, BookingRepository bookingRepository) {
        this.bookingMapper = bookingMapper;
        this.bookingRepository = bookingRepository;
    }

    public Page<BookingDto> getBookings(Pageable pageable) {
        logger.info(String.format("Getting all bookings from page number %d", pageable.getPageNumber()));
        return bookingMapper.pageToDtos(bookingRepository.findAll(pageable));
    }

    public BookingDto getBookingById(String id) {

        Optional<Booking> bookingOptional = bookingRepository.findById(id);

        if (bookingOptional.isPresent()) {
            logger.info(String.format("Booking with id =%s found.", id));
            return bookingMapper.toDto(bookingRepository.getById(id));
        } else {
            logger.warn(String.format("Booking with id =%s not found.", id));
            throw new NoSuchRecordException(String.format("Booking with id = %s not found", id));
        }
    }

    public BookingDto createBooking(final BookingDto bookingDto) {

        logger.info("New booking created");
        Booking savedBooking = bookingRepository.save(bookingMapper.toEntity(bookingDto));
        return bookingMapper.toDto(savedBooking);
    }

    public BookingDto updateBooking(final BookingDto bookingFromUi) {

        Optional<Booking> bookingFromDb = bookingRepository.findById(bookingFromUi.getId());

        if (!bookingFromDb.isPresent()) {
            throw new NoSuchRecordException(String.format("Booking with id = %s not found", bookingFromUi.getId()));
        }

        Booking booking = bookingMapper.toEntity(bookingFromUi);
        logger.info(String.format("Booking with id =%s updated", bookingFromUi.getId()));

        Booking savedBooking = bookingRepository.save(booking);
        return bookingMapper.toDto(savedBooking);
    }

    public void deleteAllBookings() {

        bookingRepository.deleteAll();
        logger.warn("All bookings deleted");
    }

    public void deleteBooking(String id) {
        Optional<Booking> booking = bookingRepository.findById(id);

        if (booking.isPresent()) {
            bookingRepository.deleteById(id);
            logger.warn(String.format("Booking with id =%s deleted", id));
        } else {
            logger.warn(String.format("Booking with id = %s not found", id));
            throw new NoSuchRecordException(String.format("Booking with id = %s not found", id));
        }
    }
}
