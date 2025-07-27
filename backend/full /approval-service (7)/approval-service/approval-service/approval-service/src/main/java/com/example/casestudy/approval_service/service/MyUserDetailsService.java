//package com.example.casestudy.approval_service.service;
//
//import com.example.casestudy.approval_service.feign.RequestInterface;
//import com.example.casestudy.approval_service.model.Employee;
//
//import com.example.casestudy.approval_service.model.MyUserDetails;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.User;
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
//    private RequestInterface requestInterface;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        // Fetch employee details from Request Service
//        Optional<Employee> employee = requestInterface.getEmployeeByUsername(username);
//        if (employee == null) {
//            throw new UsernameNotFoundException("User not found with username: " + username);
//        }
//
//        // Return UserDetails object
//        return employee.map(MyUserDetails::new).get();
//    }
//}
