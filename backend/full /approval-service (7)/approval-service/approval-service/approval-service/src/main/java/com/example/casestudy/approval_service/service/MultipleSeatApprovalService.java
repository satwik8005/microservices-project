package com.example.casestudy.approval_service.service;

import com.example.casestudy.approval_service.feign.RequestInterface;
import com.example.casestudy.approval_service.model.MultipleSeatApproval;
import com.example.casestudy.approval_service.model.MultipleSeatRequest;
import com.example.casestudy.approval_service.repository.MultipleSeatApprovalRepository;
import jakarta.servlet.http.HttpServletRequest;
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
public class MultipleSeatApprovalService {
   @Autowired
   MultipleSeatApprovalRepository multipleSeatApprovalRepository;

    @Autowired
    private RestTemplate restTemplate;

    private static final String REQUEST_SERVICE_URL = "http://localhost:8080";

    public List<MultipleSeatRequest> getPendingManagerRequests() {
        try {
            ServletRequestAttributes requestAttributes =
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (requestAttributes == null) {
                return Collections.emptyList();
            }

            String authHeader = requestAttributes.getRequest().getHeader(HttpHeaders.AUTHORIZATION);
            if (authHeader == null) {
                return Collections.emptyList();
            }

            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.AUTHORIZATION, authHeader);
            headers.set("Content-Type", "application/json");

            HttpEntity<String> entity = new HttpEntity<>(headers);

            System.out.println("Making request with auth header: " + authHeader);  // Debug log

            ResponseEntity<List<MultipleSeatRequest>> response = restTemplate.exchange(
                    REQUEST_SERVICE_URL + "/api/seat-request/allRequestByManager",
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<List<MultipleSeatRequest>>() {}
            );

            if (response.getBody() != null) {
                return response.getBody();
            }

            return Collections.emptyList();
        } catch (Exception e) {
            System.out.println("Error in getPendingManagerRequests: " + e.getMessage());  // Debug log
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Transactional
    public String approveManagerRequest(int requestId, String status) {
        MultipleSeatApproval approval = new MultipleSeatApproval();
        approval.setRequestId(requestId);
        approval.setApprovalStatus(status);
        multipleSeatApprovalRepository.save(approval);

        try {
            ServletRequestAttributes requestAttributes =
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            String authHeader = requestAttributes.getRequest().getHeader(HttpHeaders.AUTHORIZATION);

            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.AUTHORIZATION, authHeader);

            HttpEntity<String> entity = new HttpEntity<>(headers);

            restTemplate.exchange(
                    REQUEST_SERVICE_URL + "/api/seat-request/update-status-for-mass-seat/" + requestId + "?status=" + status,
                    HttpMethod.PUT,
                    entity,
                    Void.class
            );

            return "Request " + status.toLowerCase();
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to update request status";
        }
    }
}