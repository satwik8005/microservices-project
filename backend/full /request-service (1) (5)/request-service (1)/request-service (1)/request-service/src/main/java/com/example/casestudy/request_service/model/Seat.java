package com.example.casestudy.request_service.model;


import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "seats")
public class Seat {
    @Id
    @Column(name = "seat_number")
    private String seatNo;

    @Column(name = "available")
    private boolean isAvailable;

    @Column(name = "project_name")
    private String projectName;

    // Getters and setters

    public String getSeatNo() {
        return seatNo;
    }

    public void setSeatNo(String seatNo) {
        this.seatNo = seatNo;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}