package com.example.internshipapp.controller;

import com.example.internshipapp.dto.BookingDto;
import com.example.internshipapp.service.BookingService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping
    @ResponseBody
    public Page<BookingDto> getBookings(@PageableDefault Pageable pageRequest) {
        return bookingService.getBookings(pageRequest);
    }

    @GetMapping("/{id}")
    @ResponseBody
    public BookingDto getBookingById(@PathVariable(name = "id") String id) {
        return bookingService.getBookingById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BookingDto> createUser(@RequestBody @Valid BookingDto bookingDto) {

        return new ResponseEntity<>(bookingService.createBooking(bookingDto), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BookingDto> updateBooking(@PathVariable(name = "id") String id, @Valid @RequestBody BookingDto bookingDto) {

        return new ResponseEntity<>(bookingService.updateBooking(bookingDto), HttpStatus.OK);
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
