package com.example.employeeonboarding.service;

import com.example.employeeonboarding.exception.EmployeeNotFoundException;
import com.example.employeeonboarding.exception.LastNameUpdateNotAllowedException;
import com.example.employeeonboarding.model.Employee;
import com.example.employeeonboarding.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class EmployeeService {

    private static final String EMPLOYEE_NOT_FOUND_MESSAGE = "Employee not found with id: ";

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

    public Employee getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Employee updateLastName(Long id, String lastName) {
        Employee employee = repository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(EMPLOYEE_NOT_FOUND_MESSAGE + id));
        employee.setLastName(lastName);
        return repository.save(employee);
    }

    public List<Employee> getByDepartment(String department) {
        return repository.findByDepartment(department);
    }

    public Employee update(Long id, Employee updatedEmployee) {
        Employee existingEmployee = repository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(EMPLOYEE_NOT_FOUND_MESSAGE + id));

        if (updatedEmployee.getLastName() != null
                && !Objects.equals(updatedEmployee.getLastName(), existingEmployee.getLastName())) {
            throw new LastNameUpdateNotAllowedException("Last name cannot be updated");
        }

        existingEmployee.setName(updatedEmployee.getName());
        existingEmployee.setEmail(updatedEmployee.getEmail());
        existingEmployee.setDepartment(updatedEmployee.getDepartment());

        return repository.save(existingEmployee);
    }

    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new EmployeeNotFoundException(EMPLOYEE_NOT_FOUND_MESSAGE + id);
        }
        repository.deleteById(id);
    }
}