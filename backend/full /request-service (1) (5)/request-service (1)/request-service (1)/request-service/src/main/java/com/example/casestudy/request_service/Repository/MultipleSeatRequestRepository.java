package com.example.casestudy.request_service.Repository;

import com.example.casestudy.request_service.model.MultipleSeatRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface MultipleSeatRequestRepository extends JpaRepository<MultipleSeatRequest, Long> {
    List<MultipleSeatRequest> findByStatus(String status);
    List<MultipleSeatRequest> findByStatusIn(List<String> statuses);
    List<MultipleSeatRequest> findByEmployeeId(int employeeId);
    // Removed findByProjectName as it's not needed
}