package com.casestudy.authentication_service.controller;

import com.casestudy.authentication_service.dto.AuthRequest;
import com.casestudy.authentication_service.model.Employee;
import com.casestudy.authentication_service.repository.EmployeeRepository;
import com.casestudy.authentication_service.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    @Autowired
    private AuthService service;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private EmployeeRepository employeeRepository;

    @PostMapping("/register")
    public ResponseEntity<String> addNewUser(@RequestBody Employee employee) {
        try {
            return ResponseEntity.ok(service.saveUser(employee));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticate(@RequestBody AuthRequest request) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            if (auth.isAuthenticated()) {
                Employee employee = employeeRepository.findByUserName(request.getUsername())
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
                return ResponseEntity.ok(employee.getRole());
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Authentication failed: " + e.getMessage());
        }
    }

    @GetMapping("/getEmployeeByUsername/{username}")
    public ResponseEntity<Employee> getEmployeeByUsername(@PathVariable String username) {
        try {
            Employee employee = employeeRepository.findByUserName(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            return ResponseEntity.ok(employee);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/getEmployee/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable int id) {
        try {
            Employee employee = service.getEmployee(id);
            return ResponseEntity.ok(employee);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/updateEmployee")
    public ResponseEntity<String> updateEmployee(@RequestBody Employee employee) {
        try {
            Employee existingEmployee = service.getEmployee(employee.getId());

            // Update only the allowed fields
            existingEmployee.setSeatNo(employee.getSeatNo());
            existingEmployee.setProjectName(employee.getProjectName());
            existingEmployee.setActive(employee.isActive());

            service.updateEmployee(existingEmployee);
            return ResponseEntity.ok("Employee updated successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Failed to update employee: " + e.getMessage());
        }
    }

    @GetMapping("/validateToken")
    public ResponseEntity<Employee> validateToken(@RequestHeader("Authorization") String token) {
        try {
            // Remove "Basic " prefix if present
            if (token.startsWith("Basic ")) {
                token = token.substring(6);
            }

            // Get username from token (assuming Basic auth with username:password format)
            String username = new String(java.util.Base64.getDecoder().decode(token))
                    .split(":")[0];

            Employee employee = employeeRepository.findByUserName(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            return ResponseEntity.ok(employee);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PutMapping("/updateSeat/{employeeId}")
    public ResponseEntity<String> updateEmployeeSeat(
            @PathVariable int employeeId,
            @RequestParam String seatNo) {
        try {
            Employee employee = service.getEmployee(employeeId);
            employee.setSeatNo(seatNo);
            service.updateEmployee(employee);
            return ResponseEntity.ok("Seat updated successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Failed to update seat: " + e.getMessage());
        }
    }

    @PutMapping("/updateProject/{employeeId}")
    public ResponseEntity<String> updateEmployeeProject(
            @PathVariable int employeeId,
            @RequestParam String projectName) {
        try {
            Employee employee = service.getEmployee(employeeId);
            employee.setProjectName(projectName);
            service.updateEmployee(employee);
            return ResponseEntity.ok("Project updated successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Failed to update project: " + e.getMessage());
        }
    }
}

