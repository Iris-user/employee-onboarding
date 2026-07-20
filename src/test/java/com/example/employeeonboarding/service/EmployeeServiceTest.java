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
    void save_delegatesToRepositoryAndReturnsSavedEmployee() {
        Employee toSave = new Employee(null, "Alice", "alice@example.com", "Engineering");
        Employee saved = new Employee(1L, "Alice", "alice@example.com", "Engineering");
        when(repository.save(toSave)).thenReturn(saved);

        Employee result = service.save(toSave);

        assertThat(result).isEqualTo(saved);
    }

    @Test
    void getAll_returnsAllEmployeesFromRepository() {
        Employee employee = new Employee(1L, "Alice", "alice@example.com", "Engineering");
        when(repository.findAll()).thenReturn(List.of(employee));

        List<Employee> result = service.getAll();

        assertThat(result).containsExactly(employee);
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
