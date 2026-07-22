package com.example.employeeonboarding.controller;

import com.example.employeeonboarding.model.Employee;
import com.example.employeeonboarding.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService service;

    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    @PostMapping
    public Employee createEmployee(@Valid @RequestBody Employee emp) {
        return service.save(emp);
    }

    @GetMapping
    public List<Employee> getAllEmployees() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        Employee employee = service.getById(id);
        return employee != null ? ResponseEntity.ok(employee) : ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}/last-name")
    public Employee updateLastName(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return service.updateLastName(id, body.get("lastName"));
    }

    @GetMapping("/department/{department}")
    public List<Employee> getEmployeesByDepartment(@PathVariable String department) {
        return service.getByDepartment(department);
    }

    @GetMapping("/age/{age}")
    public List<Employee> getEmployeesByAge(@PathVariable Integer age) {
        return service.getByAge(age);
    }

    @GetMapping("/email/{email}")
    public List<Employee> getEmployeesByEmail(@PathVariable String email) {
        return service.getByEmail(email);
    }

    @PutMapping("/{id}")
    public Employee updateEmployee(@PathVariable Long id, @RequestBody Employee emp) {
        return service.update(id, emp);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEmployee(@PathVariable Long id) {
        service.deleteById(id);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEmployees(@RequestBody Map<String, List<Long>> body) {
        service.deleteByIds(body.get("ids"));
    }
}
