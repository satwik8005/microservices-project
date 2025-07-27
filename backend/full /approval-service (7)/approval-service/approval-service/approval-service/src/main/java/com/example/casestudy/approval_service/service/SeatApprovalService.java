package com.example.casestudy.approval_service.service;

import com.example.casestudy.approval_service.feign.RequestInterface;
import com.example.casestudy.approval_service.model.Employee;
import com.example.casestudy.approval_service.model.SeatApproval;
import com.example.casestudy.approval_service.model.SeatRequest;
import com.example.casestudy.approval_service.repository.SeatApprovalRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Collections;
import java.util.List;

@Service
public class SeatApprovalService {
    @Autowired
    private SeatApprovalRepository seatApprovalRepository;
    private static final String GATEWAY_URL = "http://localhost:8080";

    @Autowired
    private RequestInterface requestServiceClient;

    @Autowired
    private RestTemplate restTemplate;

    private static final String REQUEST_SERVICE_URL = "http://localhost:8080";
    private static final String AUTH_SERVICE_URL = "http://localhost:8082";

    public List<SeatRequest> getPendingRequests() {
        try {
            HttpHeaders headers = getAuthHeaders();
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<List<SeatRequest>> response = restTemplate.exchange(
                    GATEWAY_URL + "/api/seat-request/allRequestByDeveloper",
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<List<SeatRequest>>() {}
            );

            return response.getBody() != null ? response.getBody() : Collections.emptyList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Transactional
    public String approveRequest(int requestId, String status) {
        try {
            // Create approval record first
            SeatApproval approval = new SeatApproval();
            approval.setRequestId(requestId);
            approval.setApprovalStatus(status);
            seatApprovalRepository.save(approval);

            // Then update request status through gateway
            HttpHeaders headers = getAuthHeaders();
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    GATEWAY_URL + "/api/seat-request/update-status/" + requestId + "?status=" + status,
                    HttpMethod.PUT,
                    entity,
                    String.class
            );

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("Failed to update request status");
            }

            return "Request " + status.toLowerCase();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to approve request: " + e.getMessage());
        }
    }

    private HttpHeaders getAuthHeaders() {
        ServletRequestAttributes requestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
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
}