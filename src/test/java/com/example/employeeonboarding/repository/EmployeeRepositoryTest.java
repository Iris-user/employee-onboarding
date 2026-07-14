package com.example.employeeonboarding.repository;

import com.example.employeeonboarding.model.Employee;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository repository;

    @Test
    void findByDepartment_returnsOnlyEmployeesInThatDepartment() {
        repository.save(new Employee(null, "Alice", "alice@example.com", "Engineering"));
        repository.save(new Employee(null, "Bob", "bob@example.com", "Sales"));
        repository.save(new Employee(null, "Carol", "carol@example.com", "Engineering"));

        List<Employee> result = repository.findByDepartment("Engineering");

        assertThat(result).extracting(Employee::getName).containsExactlyInAnyOrder("Alice", "Carol");
    }

    @Test
    void findByDepartment_returnsEmptyListWhenNoEmployeesMatch() {
        repository.save(new Employee(null, "Alice", "alice@example.com", "Engineering"));

        List<Employee> result = repository.findByDepartment("Marketing");

        assertThat(result).isEmpty();
    }
}
