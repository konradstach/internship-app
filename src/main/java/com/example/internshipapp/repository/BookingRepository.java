package com.example.internshipapp.repository;

import com.example.internshipapp.model.Booking;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface BookingRepository extends MongoRepository<Booking, String>, PagingAndSortingRepository<Booking, String> {
}
