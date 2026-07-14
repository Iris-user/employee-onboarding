package com.example.employeeonboarding.service;

import com.example.employeeonboarding.model.Employee;
import com.example.employeeonboarding.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository repository;

    @InjectMocks
    private EmployeeService service;

    @Test
    void getAll_returnsAllEmployees() {
        Employee emp = new Employee(1L, "Alice", "alice@example.com", "Engineering");
        when(repository.findAll()).thenReturn(List.of(emp));

        List<Employee> result = service.getAll();

        assertThat(result).containsExactly(emp);
    }

    @Test
    void save_persistsEmployee() {
        Employee emp = new Employee(null, "Bob", "bob@example.com", "Sales");
        Employee saved = new Employee(1L, "Bob", "bob@example.com", "Sales");
        when(repository.save(emp)).thenReturn(saved);

        Employee result = service.save(emp);

        assertThat(result).isEqualTo(saved);
    }

    @Test
    void getByDepartment_returnsOnlyMatchingEmployees() {
        Employee engineer = new Employee(1L, "Alice", "alice@example.com", "Engineering");
        when(repository.findByDepartment("Engineering")).thenReturn(List.of(engineer));

        List<Employee> result = service.getByDepartment("Engineering");

        assertThat(result).containsExactly(engineer);
    }

    @Test
    void getByDepartment_returnsEmptyListWhenNoMatch() {
        when(repository.findByDepartment("Marketing")).thenReturn(List.of());

        List<Employee> result = service.getByDepartment("Marketing");

        assertThat(result).isEmpty();
    }
}
