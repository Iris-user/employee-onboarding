package com.example.employeeonboarding.service;

import com.example.employeeonboarding.exception.EmployeeNotFoundException;
import com.example.employeeonboarding.exception.LastNameUpdateNotAllowedException;
import com.example.employeeonboarding.model.Employee;
import com.example.employeeonboarding.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository repository;

    private EmployeeService service;

    private Employee employee;

    @BeforeEach
    void setUp() {
        service = new EmployeeService(repository);
        employee = new Employee(1L, "John", "Doe", "john.doe@example.com", "Engineering");
    }

    @Test
    void shouldSaveEmployeeWithLastName() {
        when(repository.save(any(Employee.class))).thenReturn(employee);

        Employee saved = service.save(employee);

        assertThat(saved.getLastName()).isEqualTo("Doe");
        verify(repository, times(1)).save(employee);
    }

    @Test
    void getAll_returnsAllEmployeesFromRepository() {
        when(repository.findAll()).thenReturn(List.of(employee));

        List<Employee> result = service.getAll();

        assertThat(result).containsExactly(employee);
    }

    @Test
    void getById_returnsEmployeeWhenIdExists() {
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
    void shouldUpdateLastName() {
        when(repository.findById(1L)).thenReturn(Optional.of(employee));
        when(repository.save(any(Employee.class))).thenAnswer(inv -> inv.getArgument(0));

        Employee updated = service.updateLastName(1L, "Smith");

        assertThat(updated.getLastName()).isEqualTo("Smith");
        verify(repository).findById(1L);
        verify(repository).save(employee);
    }

    @Test
    void shouldThrowEmployeeNotFoundExceptionWhenEmployeeNotFoundForUpdateLastName() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.updateLastName(99L, "Smith"))
                .isInstanceOf(EmployeeNotFoundException.class);
    }

    @Test
    void getByDepartment_returnsOnlyMatchingEmployees() {
        Employee engineer = new Employee(1L, "Alice", "Smith", "alice@example.com", "Engineering");
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
    void update_appliesChangesToNameEmailAndDepartment_whenLastNameUnchanged() {
        when(repository.findById(1L)).thenReturn(Optional.of(employee));
        when(repository.save(any(Employee.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Employee update = new Employee(null, "Johnny", "Doe", "johnny.doe@example.com", "Sales");

        Employee result = service.update(1L, update);

        assertThat(result.getName()).isEqualTo("Johnny");
        assertThat(result.getEmail()).isEqualTo("johnny.doe@example.com");
        assertThat(result.getDepartment()).isEqualTo("Sales");
        assertThat(result.getLastName()).isEqualTo("Doe");
    }

    @Test
    void update_leavesLastNameUnchanged_whenRequestOmitsLastName() {
        when(repository.findById(1L)).thenReturn(Optional.of(employee));
        when(repository.save(any(Employee.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Employee update = new Employee(null, "Johnny", null, "johnny.doe@example.com", "Sales");

        Employee result = service.update(1L, update);

        assertThat(result.getLastName()).isEqualTo("Doe");
    }

    @Test
    void update_throwsLastNameUpdateNotAllowedException_whenLastNameIsChanged() {
        when(repository.findById(1L)).thenReturn(Optional.of(employee));

        Employee update = new Employee(null, "John", "Doeson", "john.doe@example.com", "Engineering");

        assertThatThrownBy(() -> service.update(1L, update))
                .isInstanceOf(LastNameUpdateNotAllowedException.class);

        verify(repository, never()).save(any(Employee.class));
    }

    @Test
    void update_throwsEmployeeNotFoundException_whenEmployeeDoesNotExist() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        Employee update = new Employee(null, "Ghost", "Employee", "ghost@example.com", "Ops");

        assertThatThrownBy(() -> service.update(99L, update))
                .isInstanceOf(EmployeeNotFoundException.class);
    }

    @Test
    void deleteById_deletesEmployee_whenIdExists() {
        when(repository.existsById(1L)).thenReturn(true);

        service.deleteById(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    void deleteById_throwsEmployeeNotFoundException_whenIdDoesNotExist() {
        when(repository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> service.deleteById(99L))
                .isInstanceOf(EmployeeNotFoundException.class);

        verify(repository, never()).deleteById(any());
    }
}
