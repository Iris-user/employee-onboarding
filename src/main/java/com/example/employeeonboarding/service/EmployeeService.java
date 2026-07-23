package com.example.employeeonboarding.service;

import com.example.employeeonboarding.exception.DepartmentNotFoundException;
import com.example.employeeonboarding.exception.EmployeeNotFoundException;
import com.example.employeeonboarding.exception.InvalidDepartmentReferenceException;
import com.example.employeeonboarding.exception.LastNameUpdateNotAllowedException;
import com.example.employeeonboarding.model.Department;
import com.example.employeeonboarding.model.Employee;
import com.example.employeeonboarding.repository.DepartmentRepository;
import com.example.employeeonboarding.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class EmployeeService {

    private static final String EMPLOYEE_NOT_FOUND_MESSAGE = "Employee not found with id: ";

    private final EmployeeRepository repository;

    private final DepartmentRepository departmentRepository;

    public EmployeeService(EmployeeRepository repository, DepartmentRepository departmentRepository) {
        this.repository = repository;
        this.departmentRepository = departmentRepository;
    }

    public Employee save(Employee emp) {
        emp.setDepartment(resolveDepartment(emp.getDepartment()));
        return repository.save(emp);
    }

    private Department resolveDepartment(Department department) {
        if (department == null || department.getId() == null) {
            throw new InvalidDepartmentReferenceException("Department id is required");
        }
        return departmentRepository.findById(department.getId())
                .orElseThrow(() -> new DepartmentNotFoundException("Department not found with id: " + department.getId()));
    }

    public List<Employee> getAll() {
        return repository.findAll();
    }

    public Employee getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public String getTemporaryAddress(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(EMPLOYEE_NOT_FOUND_MESSAGE + id))
                .getTemporaryAddress();
    }

    public String getPermanentAddress(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(EMPLOYEE_NOT_FOUND_MESSAGE + id))
                .getPermanentAddress();
    }

    public Employee updateLastName(Long id, String lastName) {
        Employee employee = repository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(EMPLOYEE_NOT_FOUND_MESSAGE + id));
        employee.setLastName(lastName);
        return repository.save(employee);
    }

    public List<Employee> getByDepartment(String department) {
        return repository.findByDepartmentName(department);
    }

    public List<Employee> getByAge(Integer age) {
        return repository.findByAge(age);
    }

    public List<Employee> getByEmail(String email) {
        return repository.findByEmail(email);
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
        existingEmployee.setDepartment(resolveDepartment(updatedEmployee.getDepartment()));

        return repository.save(existingEmployee);
    }

    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new EmployeeNotFoundException(EMPLOYEE_NOT_FOUND_MESSAGE + id);
        }
        repository.deleteById(id);
    }

    public void deleteByIds(List<Long> ids) {
        List<Long> missingIds = ids.stream().filter(id -> !repository.existsById(id)).toList();
        if (!missingIds.isEmpty()) {
            throw new EmployeeNotFoundException("Employee(s) not found with id(s): " + missingIds);
        }
        repository.deleteAllById(ids);
    }
}