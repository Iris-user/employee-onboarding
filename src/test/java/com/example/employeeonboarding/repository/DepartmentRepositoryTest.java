package com.example.employeeonboarding.repository;

import com.example.employeeonboarding.model.Department;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class DepartmentRepositoryTest {

    @Autowired
    private DepartmentRepository repository;

    @Test
    void save_persistsDepartmentAndGeneratesId() {
        Department saved = repository.save(new Department(null, "Engineering"));

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Engineering");
    }

    @Test
    void findById_returnsDepartmentWhenIdExists() {
        Department saved = repository.save(new Department(null, "Engineering"));

        Optional<Department> result = repository.findById(saved.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Engineering");
    }

    @Test
    void findById_returnsEmptyWhenIdDoesNotExist() {
        Optional<Department> result = repository.findById(9999L);

        assertThat(result).isEmpty();
    }
}
