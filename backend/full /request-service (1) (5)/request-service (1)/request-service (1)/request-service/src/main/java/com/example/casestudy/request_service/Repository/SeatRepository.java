package com.example.casestudy.request_service.Repository;

import com.example.casestudy.request_service.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SeatRepository extends JpaRepository<Seat,String> {
    @Query("SELECT COUNT(s) FROM Seat s")
    long countSeats();
    Optional<Seat> findById(String seatNo);

}
