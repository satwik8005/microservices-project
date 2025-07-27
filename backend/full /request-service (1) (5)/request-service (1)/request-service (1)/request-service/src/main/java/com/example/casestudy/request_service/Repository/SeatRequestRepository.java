package com.example.casestudy.request_service.Repository;

import com.example.casestudy.request_service.model.SeatRequest;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeatRequestRepository extends JpaRepository<SeatRequest, Integer> {

    List<SeatRequest> findByStatus(String status);
    List<SeatRequest> findByStatusIn(List<String> statuses);
    List<SeatRequest> findByEmployeeId(int employeeId);
    @Query("SELECT s FROM SeatRequest s WHERE s.id = :id")
    Optional<SeatRequest> findById(@Param("id") Integer id);
}