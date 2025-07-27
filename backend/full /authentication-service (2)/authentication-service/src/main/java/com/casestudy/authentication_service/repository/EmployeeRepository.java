package com.casestudy.authentication_service.repository;


import com.casestudy.authentication_service.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee,Integer> {

    Optional<Employee> findByUserName(String userName);


}
