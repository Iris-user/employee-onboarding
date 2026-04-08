package com.example.employeeonboarding.service;

import com.example.employeeonboarding.model.Employee;
import com.example.employeeonboarding.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository repository;

    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }

    public Employee save(Employee emp) {
        return repository.save(emp);
    }

    public List<Employee> getAll() {
        return repository.findAll();
    }

    public Employee update(Long id, Employee updates) {
        Employee existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
        existing.setName(updates.getName());
        existing.setLastName(updates.getLastName());
        existing.setEmail(updates.getEmail());
        existing.setDepartment(updates.getDepartment());
        return repository.save(existing);
    }
}