package com.casestudy.authentication_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Column;

@Entity
public class Employee {
    @Id
    private int id;
    private String userName;
    private String name;

    @Column(columnDefinition = "boolean default true")
    private boolean active = true;

    private String role;
    private String password;
    private String projectName;
    private String seatNo;

    public Employee() {
    }

    public Employee(int id, String userName, String name, boolean active, String role, String password, String projectName, String seatNo) {
        this.id = id;
        this.userName = userName;
        this.name = name;
        this.active = active;
        this.role = role;
        this.password = password;
        this.projectName = projectName;
        this.seatNo = seatNo;
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
// Keep existing constructors, getters, and setters
}