// In approval service
package com.example.casestudy.approval_service.model;

import java.time.LocalDate;

// Remove @Entity since this is just a model class in approval service
public class SeatRequest {
    private Long id;
    private String seatNo;
    private int employeeId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;

    // Constructors
    public SeatRequest() {}

    public SeatRequest(Long id, String seatNo, int employeeId,
                       LocalDate startDate, LocalDate endDate, String status) {
        this.id = id;
        this.seatNo = seatNo;
        this.employeeId = employeeId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSeatNo() {
        return seatNo;
    }

    public void setSeatNo(String seatNo) {
        this.seatNo = seatNo;
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