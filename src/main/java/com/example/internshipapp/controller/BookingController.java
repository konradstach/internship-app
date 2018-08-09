package com.example.internshipapp.controller;

import com.example.internshipapp.dto.BookingDto;
import com.example.internshipapp.service.BookingService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
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
    public Page<BookingDto> getBookings(@PageableDefault Pageable pageRequest) {
        return bookingService.getBookings(pageRequest);
    }

    @GetMapping("/{id}")
    public BookingDto getBookingById(@PathVariable(name = "id") String id) {
        return bookingService.getBookingById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDto createUser(@RequestBody @Valid BookingDto bookingDto) {

        return bookingService.createBooking(bookingDto);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BookingDto updateBooking(@PathVariable(name = "id") String id, @Valid @RequestBody BookingDto bookingDto) {

        return bookingService.updateBooking(bookingDto);
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
