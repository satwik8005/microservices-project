package com.example.casestudy.approval_service.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "seat_approvals")
public class SeatApproval {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int requestId;
    private String approvalStatus;
    private LocalDateTime approvedAt;

    @PrePersist
    protected void onCreate() {
        approvedAt = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public LocalDateTime getApprovedAt() {
        return approvedAt;
    }

    public void setApprovedAt(LocalDateTime approvedAt) {
        this.approvedAt = approvedAt;
    }
// Getters and setters
}