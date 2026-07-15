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

    public Employee update(Long id, Employee changes) {
        return repository.findById(id)
                .map(existing -> {
                    existing.setName(changes.getName());
                    existing.setEmail(changes.getEmail());
                    existing.setDepartment(changes.getDepartment());
                    return repository.save(existing);
                })
                .orElse(null);
    }
}