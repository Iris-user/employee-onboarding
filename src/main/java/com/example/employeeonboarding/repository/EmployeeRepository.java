package com.example.employeeonboarding.repository;

import com.example.employeeonboarding.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}