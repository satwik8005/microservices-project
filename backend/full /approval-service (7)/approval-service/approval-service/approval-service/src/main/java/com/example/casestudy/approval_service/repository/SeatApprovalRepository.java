package com.example.casestudy.approval_service.repository;

import com.example.casestudy.approval_service.model.SeatApproval;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatApprovalRepository extends JpaRepository<SeatApproval,Integer> {
}
