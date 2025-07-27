package com.example.casestudy.approval_service.controller;

import com.example.casestudy.approval_service.model.MultipleSeatRequest;
import com.example.casestudy.approval_service.model.SeatRequest;
import com.example.casestudy.approval_service.service.MultipleSeatApprovalService;
import com.example.casestudy.approval_service.service.SeatApprovalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seat-approval")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class SeatApprovalController {
    @Autowired
    private SeatApprovalService seatApprovalService;

    @Autowired
    private MultipleSeatApprovalService multipleSeatApprovalService;

    @GetMapping("/pendingSeatForApproval")
    public ResponseEntity<List<SeatRequest>> getPendingRequests() {
        try {
            List<SeatRequest> requests = seatApprovalService.getPendingRequests();
            return ResponseEntity.ok(requests);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/approve/{id}")
    public ResponseEntity<String> approveRequest(
            @PathVariable("id") int id,
            @RequestParam("status") String status) {
        try {
            // First try to get the request
            String result = seatApprovalService.approveRequest(id, status);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to process approval: " + e.getMessage());
        }
    }
    @GetMapping("/pendingRequestByManager")
    public ResponseEntity<List<MultipleSeatRequest>> getPendingManagerRequests() {
        try {
            List<MultipleSeatRequest> requests = multipleSeatApprovalService.getPendingManagerRequests();
            return ResponseEntity.ok(requests);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/approveManagerRequest/{id}")
    public ResponseEntity<String> approveManagerRequest(
            @PathVariable int id,
            @RequestParam String status) {
        try {
            String result = multipleSeatApprovalService.approveManagerRequest(id, status);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Failed to process approval");
        }
    }
}