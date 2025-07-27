package com.example.casestudy.request_service.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "multiple_seat_requests")
public class MultipleSeatRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Add this
    private int id;

    @ElementCollection
    @CollectionTable(name = "multiple_seat_request_seats",
            joinColumns = @JoinColumn(name = "request_id"))
    @Column(name = "seat_no")
    private List<String> seatNumbers;

    private int employeeId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;

    // Remove id from constructor - it will be auto-generated
    public MultipleSeatRequest() {
    }

    public MultipleSeatRequest(List<String> seatNumbers, int employeeId,
                               LocalDate startDate, LocalDate endDate, String status) {
        this.seatNumbers = seatNumbers;
        this.employeeId = employeeId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getSeatNumbers() {
        return seatNumbers;
    }

    public void setSeatNumbers(List<String> seatNumbers) {
        this.seatNumbers = seatNumbers;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
// getters and setters remain the same
}