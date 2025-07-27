//package com.example.casestudy.request_service.controller;
//
//import com.example.casestudy.request_service.Repository.EmployeeRepository;
//import com.example.casestudy.request_service.model.Employee;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Optional;
//
//@Controller
//@RequestMapping("api/seat-request")
//public class EmployeeController {
//
//    @Autowired
//    EmployeeRepository employeeRepository;
//
//    @GetMapping("/find-by-username")
//    public Optional<Employee> getEmployeeByUsername(@RequestParam String userName){
//        return employeeRepository.findByUserName(userName);
//    }
//
//
//    @PostMapping("/register")
//    @ResponseBody
//    public String registerEmployee(@ModelAttribute Employee employee){
//        employeeRepository.save(employee);
//        return "redirect:/registration-success";
//    }
//}
