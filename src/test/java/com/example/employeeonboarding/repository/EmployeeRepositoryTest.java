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
        repository.save(new Employee(null, "Alice", "Smith", "alice@example.com", "Engineering", 27));
        repository.save(new Employee(null, "Bob", "Jones", "bob@example.com", "Sales", 30));
        repository.save(new Employee(null, "Carol", "White", "carol@example.com", "Engineering", 35));

        List<Employee> result = repository.findByDepartment("Engineering");

        assertThat(result).extracting(Employee::getName).containsExactlyInAnyOrder("Alice", "Carol");
    }

    @Test
    void findByDepartment_returnsEmptyListWhenNoEmployeesMatch() {
        repository.save(new Employee(null, "Alice", "Smith", "alice@example.com", "Engineering", 27));

        List<Employee> result = repository.findByDepartment("Marketing");

        assertThat(result).isEmpty();
    }

    @Test
    void findByAge_returnsOnlyEmployeesWithThatAge() {
        repository.save(new Employee(null, "Alice", "Smith", "alice@example.com", "Engineering", 30));
        repository.save(new Employee(null, "Bob", "Jones", "bob@example.com", "Sales", 40));
        repository.save(new Employee(null, "Carol", "White", "carol@example.com", "Engineering", 30));

        List<Employee> result = repository.findByAge(30);

        assertThat(result).extracting(Employee::getName).containsExactlyInAnyOrder("Alice", "Carol");
    }

    @Test
    void findByAge_returnsEmptyListWhenNoEmployeesMatch() {
        repository.save(new Employee(null, "Alice", "Smith", "alice@example.com", "Engineering", 30));

        List<Employee> result = repository.findByAge(99);

        assertThat(result).isEmpty();
    }

    @Test
    void findByEmail_returnsOnlyEmployeesWithThatEmail() {
        repository.save(new Employee(null, "Alice", "Smith", "alice@example.com", "Engineering", 30));
        repository.save(new Employee(null, "Bob", "Jones", "bob@example.com", "Sales", 40));

        List<Employee> result = repository.findByEmail("alice@example.com");

        assertThat(result).extracting(Employee::getName).containsExactly("Alice");
    }

    @Test
    void findByEmail_returnsEmptyListWhenNoEmployeesMatch() {
        repository.save(new Employee(null, "Alice", "Smith", "alice@example.com", "Engineering", 30));

        List<Employee> result = repository.findByEmail("nobody@example.com");

        assertThat(result).isEmpty();
    }
}
