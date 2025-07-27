package com.casestudy.authentication_service.service;


import com.casestudy.authentication_service.model.Employee;
import com.casestudy.authentication_service.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String saveUser(Employee employee) {
        try {
            employee.setPassword(passwordEncoder.encode(employee.getPassword()));
            employeeRepository.save(employee);
            return "User registered successfully";
        } catch (Exception e) {
            e.printStackTrace(); // For debugging
            throw new RuntimeException("Registration failed: " + e.getMessage());
        }
    }

    public Employee getEmployee(int id) {
        return employeeRepository.findById(id).get();
    }

    public void updateEmployee(Employee employee) {
        employeeRepository.save(employee);
    }
}