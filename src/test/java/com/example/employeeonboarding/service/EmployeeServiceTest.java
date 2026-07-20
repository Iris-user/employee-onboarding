package com.example.employeeonboarding.service;

import com.example.employeeonboarding.model.Employee;
import com.example.employeeonboarding.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
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
    void getById_returnsEmployeeWhenIdExists() {
        Employee employee = new Employee(1L, "Alice", "alice@example.com", "Engineering");
        when(repository.findById(1L)).thenReturn(Optional.of(employee));

        Employee result = service.getById(1L);

        assertThat(result).isEqualTo(employee);
    }

    @Test
    void getById_returnsNullWhenIdDoesNotExist() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        Employee result = service.getById(99L);

        assertThat(result).isNull();
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

    @Test
    void update_updatesAndReturnsEmployeeWhenIdExists() {
        Employee existing = new Employee(1L, "Alice", "alice@example.com", "Engineering");
        Employee changes = new Employee(null, "Alice Smith", "alice.smith@example.com", "Sales");
        Employee saved = new Employee(1L, "Alice Smith", "alice.smith@example.com", "Sales");
        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(saved);

        Employee result = service.update(1L, changes);

        assertThat(result).isEqualTo(saved);
    }

    @Test
    void update_returnsNullWhenIdDoesNotExist() {
        Employee changes = new Employee(null, "Alice Smith", "alice.smith@example.com", "Sales");
        when(repository.findById(99L)).thenReturn(Optional.empty());

        Employee result = service.update(99L, changes);

        assertThat(result).isNull();
        verify(repository, never()).save(org.mockito.ArgumentMatchers.any());
    }
}
