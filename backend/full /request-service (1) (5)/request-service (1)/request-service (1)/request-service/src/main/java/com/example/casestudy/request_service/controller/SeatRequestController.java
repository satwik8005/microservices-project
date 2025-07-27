package com.example.casestudy.request_service.controller;


import com.example.casestudy.request_service.Repository.SeatRepository;
import com.example.casestudy.request_service.Repository.SeatRequestRepository;
import com.example.casestudy.request_service.model.*;
import com.example.casestudy.request_service.service.SeatRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/seat-request")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class SeatRequestController {

    @Autowired
    SeatRequestService seatRequestService;
    @Autowired
    SeatRepository seatRepository;
    @Autowired
    SeatRequestRepository seatRequestRepository;
    @GetMapping("/getAllSeats")
    @ResponseBody
    public ResponseEntity<List<Seat>> getAllSeats() {
        try {
            List<Seat> seats = seatRepository.findAll();
            return ResponseEntity.ok(seats);
        } catch (Exception e) {
            e.printStackTrace(); // For debugging
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/getAllSeatStatus")
    public ResponseEntity<List<SeatStatus>> getAllSeatStatus() {
        try {
            List<SeatStatus> statuses = seatRequestService.getAllSeatStatus();
            return ResponseEntity.ok(statuses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/allRequestByDeveloper")
    public List<SeatRequest> getAllRequests() {
        return seatRequestService.getAllRequest();
    }



    @PostMapping("/raiseRequestForDeveloper")
    public ResponseEntity<String> createRequest(@RequestBody SeatRequest request) {
        return seatRequestService.createRequest(request);
    }

    @PostMapping("/raiseMultipleSeatRequest")
    public ResponseEntity<String> createMultipleRequest(
            @RequestBody MultipleSeatRequest request) {
        return seatRequestService.createMultipleRequest(request);
    }

    @PutMapping("/getSeatForManager")
    public ResponseEntity<String> assignSeatForManager(
            @RequestParam String seatNo,
            @RequestParam int employeeId) {
        try {
            return seatRequestService.assignSeatForManager(seatNo, employeeId);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping("/update-status/{id}")
    public ResponseEntity<String> updateStatusForSingleSeat(
            @PathVariable("id") int id,
            @RequestParam("status") String status) {
        try {
            // First check if request exists
            Optional<SeatRequest> request = seatRequestRepository.findById(id);
            if (request.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Request not found with id: " + id);
            }

            seatRequestService.updateStatusForSingleSeat(id, status);
            return ResponseEntity.ok("Status updated successfully");
        } catch (Exception e) {
            e.printStackTrace(); // For debugging
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update status: " + e.getMessage());
        }
    }

    @GetMapping("/getRequest/{id}")
    public ResponseEntity<SeatRequest> getRequestById(@PathVariable int id) {
        try {
            Optional<SeatRequest> request = seatRequestRepository.findById(id);
            return request.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



    @GetMapping("/allRequestByManager")
    public List<MultipleSeatRequest> getAllRequestsByManager(){
        return seatRequestService.getAllRequestsByManger();
    }

    @PutMapping("/update-status-for-mass-seat/{id}")
    public void updateStatusForMultipleSeat(@PathVariable("id") int id, @RequestParam("status") String status){
        seatRequestService.updateStatusForMultipleSeat(id,status);
    }
}