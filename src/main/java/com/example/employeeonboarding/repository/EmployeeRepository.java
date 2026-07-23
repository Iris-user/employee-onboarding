package com.example.employeeonboarding.repository;

import com.example.employeeonboarding.model.Employee;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Override
    @EntityGraph(attributePaths = "department")
    List<Employee> findAll();

    @EntityGraph(attributePaths = "department")
    List<Employee> findByDepartmentName(String departmentName);

    @EntityGraph(attributePaths = "department")
    List<Employee> findByAge(Integer age);

    @EntityGraph(attributePaths = "department")
    List<Employee> findByEmail(String email);
}
