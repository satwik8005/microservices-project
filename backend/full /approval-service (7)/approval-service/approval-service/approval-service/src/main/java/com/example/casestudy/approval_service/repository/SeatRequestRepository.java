//package com.example.casestudy.approval_service.repository;
//
//import com.example.casestudy.approval_service.model.SeatRequest;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//import java.util.Optional;
//
//@Repository
//public interface SeatRequestRepository extends JpaRepository<SeatRequest, Integer> {
//    Optional<SeatRequest> findById(Integer id);
//    List<SeatRequest> findByStatus(String status);
//}