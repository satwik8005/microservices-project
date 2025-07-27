package com.example.casestudy.approval_service.feign;

//import com.example.casestudy.approval_service.configuration.FeignConfig;
//import com.example.casestudy.approval_service.configuration.FeignConfig;
import com.example.casestudy.approval_service.configuration.RestTemplateConfig;
import com.example.casestudy.approval_service.model.Employee;
import com.example.casestudy.approval_service.model.MultipleSeatRequest;
import com.example.casestudy.approval_service.model.SeatRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@FeignClient(name = "REQUEST-SERVICE",url = "http://localhost:8080")
public interface RequestInterface {
    @PutMapping("/api/seat-request/update-status/{id}")
    ResponseEntity<String> updateRequestStatus(
            @PathVariable("id") int id,
            @RequestParam("status") String status);

    @GetMapping("/api/seat-request/getRequest/{id}")
    ResponseEntity<SeatRequest> getRequestById(@PathVariable("id") int id);

    @GetMapping("/api/seat-request/allRequestByDeveloper")
    List<SeatRequest> getPendingRequests();

    @GetMapping("/api/seat-request/allRequestByManager")
    List<MultipleSeatRequest> getPendingManagerRequests();


    @PutMapping("/api/seat-request/update-status-for-mass-seat/{id}")
    void updateMultipleRequestStatus(@PathVariable("id") int id, @RequestParam("status") String status);
}