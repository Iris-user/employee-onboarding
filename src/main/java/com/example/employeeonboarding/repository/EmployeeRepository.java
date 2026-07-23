package com.example.employeeonboarding.repository;

import com.example.employeeonboarding.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    List<Employee> findByDepartmentName(String departmentName);

    List<Employee> findByAge(Integer age);

    List<Employee> findByEmail(String email);
}
