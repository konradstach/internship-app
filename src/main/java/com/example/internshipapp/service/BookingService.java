package com.example.internshipapp.service;

import com.example.internshipapp.exception.NoSuchRecordException;
import com.example.internshipapp.model.Booking;
import com.example.internshipapp.repository.BookingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    private BookingRepository bookingRepository;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public Page<Booking> getBookings(Pageable pageable) {
        logger.info("Getting all bookings");
        return bookingRepository.findAll(pageable);
    }

    public List<Booking> getBookingsUnpaged() {
        return bookingRepository.findAll();
    }

    public Booking getBookingById(String id) {

        Optional<Booking> bookingOptional = bookingRepository.findById(id);

        if (bookingOptional.isPresent()) {
            logger.info(String.format("Booking with id =%s found.", id));
            return bookingOptional.get();
        } else {
            logger.warn(String.format("Booking with id =%s not found.", id));
            throw new NoSuchRecordException(String.format("Booking with id = %s not found", id));
        }
    }

    public Booking createBooking(Booking bookingToCreate) {

        Booking booking = Booking.clone(bookingToCreate);
        logger.info("New booking created");
        return bookingRepository.save(booking);
    }

    public Booking updateBooking(final Booking bookingFromUi) {

        Optional<Booking> bookingFromDb = bookingRepository.findById(bookingFromUi.getId());

        if (!bookingFromDb.isPresent()) {
            throw new NoSuchRecordException(String.format("Booking with id = %s not found", bookingFromUi.getId()));
        }

        Booking booking = Booking.clone(bookingFromUi);
        logger.info(String.format("Booking with id =%s updated", bookingFromUi.getId()));

        return bookingRepository.save(booking);
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
