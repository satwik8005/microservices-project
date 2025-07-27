//package com.example.casestudy.request_service.service;
//
//import com.example.casestudy.request_service.Repository.EmployeeRepository;
//import com.example.casestudy.request_service.model.Employee;
//import com.example.casestudy.request_service.model.MyUserDetails;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.Optional;
//
//@Service
//public class MyUserDetailsService implements UserDetailsService {
//
//    @Autowired
//    EmployeeRepository employeeRepository;
//    @Override
//    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
//        Optional<Employee> employee=employeeRepository.findByUserName(userName);
//        employee.orElseThrow(()->new UsernameNotFoundException("Not found: "+userName));
//        return employee.map(MyUserDetails::new).get();
//    }
//}
