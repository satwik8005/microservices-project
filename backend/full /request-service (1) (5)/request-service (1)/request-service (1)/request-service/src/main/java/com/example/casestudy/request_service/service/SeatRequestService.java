package com.example.casestudy.request_service.service;

import com.example.casestudy.request_service.Repository.MultipleSeatRequestRepository;
import com.example.casestudy.request_service.Repository.SeatRepository;
import com.example.casestudy.request_service.Repository.SeatRequestRepository;
import com.example.casestudy.request_service.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import java.time.LocalDate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class SeatRequestService {

    @Autowired
    private SeatRequestRepository seatRequestRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private MultipleSeatRequestRepository multipleSeatRequestRepository;

    @Autowired
    private RestTemplate restTemplate;

    private static final String AUTH_SERVICE_URL = "http://localhost:8082/auth";
    public ResponseEntity<String> assignSeatForManager(String seatNo, int employeeId) {
        try {
            // First create a new SeatRequest
            SeatRequest request = new SeatRequest();
            request.setSeatNo(seatNo);
            request.setEmployeeId(employeeId);
            request.setStatus("PENDING");
            request.setStartDate(LocalDate.now());
            request.setEndDate(LocalDate.now().plusMonths(1));

            // Save to seat_requests table
            SeatRequest savedRequest = seatRequestRepository.save(request);
            Integer generatedId = savedRequest.getId();


            // Verify seat exists
            Seat seat = seatRepository.findById(seatNo)
                    .orElseThrow(() -> new RuntimeException("Seat not found"));

            // Get employee with authentication
            String url = AUTH_SERVICE_URL + "/getEmployee/" + employeeId;
            HttpHeaders headers = getAuthHeaders();
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<Employee> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    Employee.class
            );

            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                return ResponseEntity.badRequest().body("Could not verify employee");
            }

            Employee employee = response.getBody();

            // Verify manager role
            if (!"ROLE_MANAGER".equals(employee.getRole())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized Request");
            }

            // Update seat
            seat.setAvailable(false);
            seat.setProjectName(employee.getProjectName());
            seatRepository.save(seat);

            // Update employee
            employee.setSeatNo(seatNo);
            HttpEntity<Employee> employeeEntity = new HttpEntity<>(employee, headers);
            restTemplate.exchange(
                    AUTH_SERVICE_URL + "/updateEmployee",
                    HttpMethod.PUT,
                    employeeEntity,
                    Void.class
            );

            return ResponseEntity.ok("Seat Request Created with ID: " +generatedId );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public List<SeatRequest> getAllRequest() {
        return seatRequestRepository.findAll();
    }

    public List<SeatRequest> getPendingRequests() {
        return seatRequestRepository.findByStatus("PENDING");
    }

    public List<SeatStatus> getAllSeatStatus() {
        List<SeatStatus> statuses = new ArrayList<>();

        // Get approved/rejected single seat requests
        List<SeatRequest> finalizedRequests = seatRequestRepository.findByStatusIn(
                Arrays.asList("APPROVED", "REJECTED")
        );
        for (SeatRequest request : finalizedRequests) {
            statuses.add(new SeatStatus(
                    request.getSeatNo(),
                    request.getStatus(),
                    request.getEmployeeId()
            ));
        }

        // Get approved/rejected multiple seat requests
        List<MultipleSeatRequest> multiRequests = multipleSeatRequestRepository.findByStatusIn(
                Arrays.asList("APPROVED", "REJECTED")
        );
        for (MultipleSeatRequest request : multiRequests) {
            for (String seatNo : request.getSeatNumbers()) {
                statuses.add(new SeatStatus(
                        seatNo,
                        request.getStatus(),
                        request.getEmployeeId()
                ));
            }
        }

        return statuses;
    }

    public ResponseEntity<String> createRequest(SeatRequest request) {
        try {
            // Verify seat exists
            Seat seat = seatRepository.findById(request.getSeatNo())
                    .orElseThrow(() -> new RuntimeException("Seat not found"));

            // Get employee with authentication
            String url = AUTH_SERVICE_URL + "/getEmployee/" + request.getEmployeeId();
            HttpHeaders headers = getAuthHeaders();
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<Employee> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    Employee.class
            );

            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                return ResponseEntity.badRequest().body("Could not verify employee");
            }

            Employee employee = response.getBody();

            // Check project match for non-approved seats
            if (seat.getProjectName() != null && !seat.getProjectName().equals(employee.getProjectName())) {
                return ResponseEntity.badRequest().body("Seat not allocated to your project");
            }

            request.setStatus("PENDING");
            seatRequestRepository.save(request);
            return ResponseEntity.ok("Request Raised, Pending for Approval");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public ResponseEntity<String> createMultipleRequest(MultipleSeatRequest multipleSeatRequest) {
        try {
            if (multipleSeatRequest.getSeatNumbers() == null || multipleSeatRequest.getSeatNumbers().isEmpty()) {
                return ResponseEntity.badRequest().body("No seats selected");
            }

            // Get employee details
            String url = AUTH_SERVICE_URL + "/getEmployee/" + multipleSeatRequest.getEmployeeId();
            HttpHeaders headers = getAuthHeaders();
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<Employee> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    Employee.class
            );

            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                return ResponseEntity.badRequest().body("Could not verify employee");
            }

            Employee employee = response.getBody();

            // Verify manager role
            if (!"ROLE_MANAGER".equals(employee.getRole())) {
                return ResponseEntity.badRequest().body("Only Project Manager can raise multiple seat request");
            }

            multipleSeatRequest.setStatus("PENDING");
            multipleSeatRequestRepository.save(multipleSeatRequest);
            return ResponseEntity.ok("Request Raised, Pending for Approval");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public void updateStatusForSingleSeat(int id, String status) {
        try {
            SeatRequest seatRequest = seatRequestRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Request not found"));

            // Get employee details
            String url = AUTH_SERVICE_URL + "/getEmployee/" + seatRequest.getEmployeeId();
            HttpHeaders headers = getAuthHeaders();
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<Employee> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    Employee.class
            );

            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                throw new RuntimeException("Could not verify employee");
            }

            Employee employee = response.getBody();

            if ("APPROVED".equals(status)) {
                Seat seat = seatRepository.findById(seatRequest.getSeatNo())
                        .orElseThrow(() -> new RuntimeException("Seat not found"));

                // Update seat
                seat.setProjectName(employee.getProjectName());
                seat.setAvailable(false);
                seatRepository.save(seat);

                // Update employee
                employee.setSeatNo(seatRequest.getSeatNo());
                HttpEntity<Employee> employeeEntity = new HttpEntity<>(employee, headers);
                restTemplate.exchange(
                        AUTH_SERVICE_URL + "/updateEmployee",
                        HttpMethod.PUT,
                        employeeEntity,
                        Void.class
                );
            }

            // Update request status
            seatRequest.setStatus(status);
            seatRequestRepository.save(seatRequest);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update seat status: " + e.getMessage());
        }
    }


    public SeatRequest getRequestById(int id) {
        return seatRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found with id: " + id));
    }

    // Added to support getPendingMultipleRequests in controller
    public List<MultipleSeatRequest> getPendingMultipleRequests() {
        return multipleSeatRequestRepository.findByStatus("PENDING");
    }

    // Method name fixed to match controller call


    // Fixed parameter type to match repository

    // Fixed parameter type to match repository
    public void updateStatusForMultipleSeat(Long id, String status) {  // was int id
        try {
            MultipleSeatRequest multipleSeatRequest = multipleSeatRequestRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Request not found"));
            // ... rest of the implementation remains same
        } catch (Exception e) {
            throw new RuntimeException("Failed to update multiple seat status: " + e.getMessage());
        }
    }
    public void updateStatusForMultipleSeat(int id, String status) {
        try {
            MultipleSeatRequest multipleSeatRequest = multipleSeatRequestRepository.findById((long)id)
                    .orElseThrow(() -> new RuntimeException("Request not found"));

            // Get employee details
            String url = AUTH_SERVICE_URL + "/getEmployee/" + multipleSeatRequest.getEmployeeId();
            HttpHeaders headers = getAuthHeaders();
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<Employee> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    Employee.class
            );

            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                throw new RuntimeException("Could not verify employee");
            }

            Employee employee = response.getBody();

            if ("APPROVED".equals(status)) {
                // Update all seats
                for (String seatNo : multipleSeatRequest.getSeatNumbers()) {
                    Seat seat = seatRepository.findById(seatNo)
                            .orElseThrow(() -> new RuntimeException("Seat not found: " + seatNo));
                    seat.setProjectName(employee.getProjectName());
                    seat.setAvailable(false);
                    seatRepository.save(seat);
                }

                // Update employee with seat numbers
                employee.setSeatNo(String.join(",", multipleSeatRequest.getSeatNumbers()));
                HttpEntity<Employee> employeeEntity = new HttpEntity<>(employee, headers);
                restTemplate.exchange(
                        AUTH_SERVICE_URL + "/updateEmployee",
                        HttpMethod.PUT,
                        employeeEntity,
                        Void.class
                );
            }

            // Update request status
            multipleSeatRequest.setStatus(status);
            multipleSeatRequestRepository.save(multipleSeatRequest);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update multiple seat status: " + e.getMessage());
        }
    }



    private HttpHeaders getAuthHeaders() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpHeaders headers = new HttpHeaders();
        if (requestAttributes != null) {
            String authHeader = requestAttributes.getRequest().getHeader(HttpHeaders.AUTHORIZATION);
            if (authHeader != null) {
                headers.set(HttpHeaders.AUTHORIZATION, authHeader);
            }
        }
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    public List<MultipleSeatRequest> getAllRequestsByManger() {
        try {
            // Get all manager requests
            List<MultipleSeatRequest> requests = multipleSeatRequestRepository.findAll();
            return requests;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get manager requests: " + e.getMessage());
        }

    }
}