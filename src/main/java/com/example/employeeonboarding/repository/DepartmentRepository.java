package com.example.employeeonboarding.repository;

import com.example.employeeonboarding.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
