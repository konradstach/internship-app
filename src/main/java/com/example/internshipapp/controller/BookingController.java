package com.example.internshipapp.controller;

import com.example.internshipapp.model.Booking;
import com.example.internshipapp.service.BookingService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

//    private void handleValidationErrors(BindingResult bindingResult) {
//        if (bindingResult.hasErrors()) {
//            String[] suppressedFields = bindingResult.getSuppressedFields();
//            for (int i = 0; i < suppressedFields.length; i++) {
//                       System.out.println(suppressedFields[i]);
//            }
//        }
//    }

    @GetMapping
    @ResponseBody
    public Page<Booking> getBookings(@PageableDefault Pageable pageable) {
        return bookingService.getBookings(pageable);
    }

    @GetMapping("/now")
    public LocalDateTime getTime() {
        return LocalDateTime.now();
    }

    @GetMapping("/unpaged")
    @ResponseBody
    public List<Booking> getBookingsUnpaged() {
        return bookingService.getBookingsUnpaged();
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Booking getBookingById(@PathVariable(name = "id") String id) {
        return bookingService.getBookingById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Booking> createUser(@RequestBody @Valid Booking booking) {
        
        return new ResponseEntity<>(bookingService.createBooking(booking), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public void modifyBooking(@PathVariable(name = "id") String id, @Valid @RequestBody Booking booking) {

        bookingService.updateBooking(booking);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAllBookings() {
        bookingService.deleteAllBookings();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBookingById(@PathVariable(name = "id") String id) {
        bookingService.deleteBooking(id);
    }


}
