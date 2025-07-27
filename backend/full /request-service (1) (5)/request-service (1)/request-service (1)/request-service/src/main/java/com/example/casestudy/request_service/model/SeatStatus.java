package com.example.casestudy.request_service.model;

public class SeatStatus {
    private String seatNo;
    private String status;
    private int employeeId;

    public SeatStatus(String seatNo, String status, int employeeId) {
        this.seatNo = seatNo;
        this.status = status;
        this.employeeId = employeeId;
    }

    // Getters and setters
    public String getSeatNo() {
        return seatNo;
    }

    public void setSeatNo(String seatNo) {
        this.seatNo = seatNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }
}