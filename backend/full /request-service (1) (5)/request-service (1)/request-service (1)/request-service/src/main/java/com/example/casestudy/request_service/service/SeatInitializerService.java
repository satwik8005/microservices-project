package com.example.casestudy.request_service.service;

import com.example.casestudy.request_service.Repository.SeatRepository;
import com.example.casestudy.request_service.model.Seat;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Component
public class SeatInitializerService implements CommandLineRunner {
    @Autowired
    private SeatRepository seatRepository;

    @Override
    public void run(String... args) {
        if (seatRepository.count() == 0) {
            List<Seat> seats = new ArrayList<>();
            for (int i = 1; i <= 50; i++) {
                Seat seat = new Seat();
                seat.setSeatNo(String.valueOf(i));
                seat.setAvailable(true);
                seats.add(seat);
            }
            seatRepository.saveAll(seats);
        }
    }
}