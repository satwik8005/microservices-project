package com.casestudy.authentication_service.service;


import com.casestudy.authentication_service.model.Employee;
import com.casestudy.authentication_service.model.MyUserDetails;
import com.casestudy.authentication_service.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Employee> employee = employeeRepository.findByUserName(username);
        if (!employee.isPresent()) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new MyUserDetails(employee.get());
    }
}
