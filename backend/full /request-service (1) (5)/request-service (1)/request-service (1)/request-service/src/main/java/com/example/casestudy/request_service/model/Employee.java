package com.example.casestudy.request_service.model;



import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.List;


public class Employee {
    private int id;
    private String userName;
    private String name;
    private boolean active = true;
    private String role = ""; // Initialize with empty string instead of null
    private String password;
    private String projectName;
    private String seatNo;

    // Constructor with all fields
    public Employee(int id, String userName, String name, boolean active,
                    String role, String password, String projectName, String seatNo) {
        this.id = id;
        this.userName = userName;
        this.name = name;
        this.active = active;
        this.role = role != null ? role : "";
        this.password = password;
        this.projectName = projectName;
        this.seatNo = seatNo;
    }

    // Default constructor
    public Employee() {
        this.role = ""; // Initialize with empty string
    }

    // Getters and setters with null checks
    public String getRole() {
        return role != null ? role : "";
    }

    public void setRole(String role) {
        this.role = role != null ? role : "";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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
// Other getters and setters...
}