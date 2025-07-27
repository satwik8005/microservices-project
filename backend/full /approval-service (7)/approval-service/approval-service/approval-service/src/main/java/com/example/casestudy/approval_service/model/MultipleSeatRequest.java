package com.example.casestudy.approval_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalDate;
import java.util.List;

public class MultipleSeatRequest {

    private int id;

    private List<String> seatNumbers;
    private int employeeId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;

    public MultipleSeatRequest() {
    }

    public MultipleSeatRequest(int id, List<String> seatNumbers, int employeeId,
                               LocalDate startDate, LocalDate endDate, String status) {
        this.id = id;
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
}


