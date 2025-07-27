package com.example.casestudy.approval_service.model;


import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.List;

public class Employee {

    private int id; //employeeId

    private String name;

    private String userName;

    private String role; //Developer,Manager
// @OneToMany(mappedBy = "employee")
// private List<SeatRequest> seatRequests;

    private String password;

    private String projectName;

    private String seatNo;
    private  boolean isActive;

    public Employee() {
    }

    public Employee(int id, String name, String userName,
                    String role, String password, String projectName, String seatNo, boolean isActive) {
        this.id = id;
        this.name = name;
        this.userName = userName;
        this.role = role;
        this.password = password;
        this.projectName = projectName;
        this.seatNo = seatNo;
        this.isActive = isActive;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getSeatNo() {
        return seatNo;
    }

    public void setSeatNo(String seatNo) {
        this.seatNo = seatNo;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}