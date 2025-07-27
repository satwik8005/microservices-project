package com.example.casestudy.approval_service.model;

import jakarta.persistence.OneToMany;

import java.util.List;

public class Seat {

    private int seatNo;

    private boolean isAvailable;

    private String projectName;


    public Seat() {
    }

    public Seat(int seatNo, boolean isAvailable, String projectName) {
        this.seatNo = seatNo;
        this.isAvailable = isAvailable;
        this.projectName = projectName;
    }

    public int getSeatNo() {
        return seatNo;
    }

    public void setSeatNo(int seatNo) {
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
