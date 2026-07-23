package com.example.employeeonboarding;

import com.example.employeeonboarding.model.Department;
import com.example.employeeonboarding.model.Employee;
import com.example.employeeonboarding.repository.DepartmentRepository;
import com.example.employeeonboarding.repository.EmployeeRepository;
import com.example.employeeonboarding.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class EmployeeIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @BeforeEach
    void setUp() {
        employeeRepository.deleteAll();
        departmentRepository.deleteAll();
    }

    @Test
    void shouldCreateAndRetrieveEmployeeWithLastName() throws Exception {
        Department hr = departmentRepository.save(new Department(null, "HR"));

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Alice\",\"lastName\":\"Johnson\",\"email\":\"alice@example.com\",\"department\":{\"id\":" + hr.getId() + "},\"age\":28}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alice"))
                .andExpect(jsonPath("$.lastName").value("Johnson"));
    }

    @Test
    void shouldReturnBadRequestWhenCreatingEmployeeWithMissingFields() throws Exception {
        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Alice\",\"lastName\":\"Johnson\"}"))
                .andExpect(status().isBadRequest());

        assertThat(employeeRepository.findAll()).isEmpty();
    }

    @Test
    void shouldGetAllEmployees() throws Exception {
        Department finance = departmentRepository.save(new Department(null, "Finance"));
        employeeService.save(new Employee(null, "Bob", "Brown", "bob@example.com", finance, 30, null, null));

        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].lastName").value("Brown"));
    }

    @Test
    void shouldUpdateLastNameViaEndpoint() throws Exception {
        Department it = departmentRepository.save(new Department(null, "IT"));
        Employee saved = employeeService.save(new Employee(null, "Carol", "White", "carol@example.com", it, 30, null, null));

        mockMvc.perform(patch("/employees/" + saved.getId() + "/last-name")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"lastName\":\"Black\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName").value("Black"));
    }

    @Test
    void shouldUpdateLastNameViaService() {
        Department legal = departmentRepository.save(new Department(null, "Legal"));
        Employee saved = employeeService.save(new Employee(null, "Dan", "Green", "dan@example.com", legal, 30, null, null));

        Employee updated = employeeService.updateLastName(saved.getId(), "Grey");

        assertThat(updated.getLastName()).isEqualTo("Grey");
    }

    @Test
    void shouldThrowWhenUpdatingLastNameForNonExistentEmployee() {
        assertThrows(RuntimeException.class, () -> employeeService.updateLastName(9999L, "Nobody"));
    }

    @Test
    void shouldDeleteEmployeeViaEndpoint() throws Exception {
        Department support = departmentRepository.save(new Department(null, "Support"));
        Employee saved = employeeService.save(new Employee(null, "Eve", "Black", "eve@example.com", support, 30, null, null));

        mockMvc.perform(delete("/employees/" + saved.getId()))
                .andExpect(status().isNoContent());

        assertThat(employeeRepository.findById(saved.getId())).isEmpty();
    }

    @Test
    void shouldReturnNotFoundWhenDeletingNonExistentEmployee() throws Exception {
        mockMvc.perform(delete("/employees/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteMultipleEmployeesViaEndpoint() throws Exception {
        Department sales = departmentRepository.save(new Department(null, "Sales"));
        Department engineering = departmentRepository.save(new Department(null, "Engineering"));
        Employee first = employeeService.save(new Employee(null, "Frank", "Miller", "frank@example.com", sales, 40, null, null));
        Employee second = employeeService.save(new Employee(null, "Grace", "Hopper", "grace@example.com", engineering, 45, null, null));

        mockMvc.perform(delete("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"ids\":[" + first.getId() + "," + second.getId() + "]}"))
                .andExpect(status().isNoContent());

        assertThat(employeeRepository.findById(first.getId())).isEmpty();
        assertThat(employeeRepository.findById(second.getId())).isEmpty();
    }

    @Test
    void shouldReturnNotFoundWhenDeletingMultipleEmployeesWithUnknownId() throws Exception {
        Department engineering = departmentRepository.save(new Department(null, "Engineering"));
        Employee saved = employeeService.save(new Employee(null, "Henry", "Ford", "henry@example.com", engineering, 50, null, null));

        mockMvc.perform(delete("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"ids\":[" + saved.getId() + ",9999]}"))
                .andExpect(status().isNotFound());

        assertThat(employeeRepository.findById(saved.getId())).isPresent();
    }

    @Test
    void shouldGetTemporaryAddressViaEndpoint() throws Exception {
        Department engineering = departmentRepository.save(new Department(null, "Engineering"));
        Employee saved = employeeService.save(
                new Employee(null, "Ivy", "Stone", "ivy@example.com", engineering, 26, "42 Elm Street", null));

        mockMvc.perform(get("/employees/" + saved.getId() + "/temporary-address"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.temporaryAddress").value("42 Elm Street"));
    }

    @Test
    void shouldReturnNotFoundWhenGettingTemporaryAddressForNonExistentEmployee() throws Exception {
        mockMvc.perform(get("/employees/9999/temporary-address"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldGetPermanentAddressViaEndpoint() throws Exception {
        Department engineering = departmentRepository.save(new Department(null, "Engineering"));
        Employee saved = employeeService.save(
                new Employee(null, "Ivy", "Stone", "ivy@example.com", engineering, 26, null, "10 Downing Street"));

        mockMvc.perform(get("/employees/" + saved.getId() + "/permanent-address"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.permanentAddress").value("10 Downing Street"));
    }

    @Test
    void shouldReturnNotFoundWhenGettingPermanentAddressForNonExistentEmployee() throws Exception {
        mockMvc.perform(get("/employees/9999/permanent-address"))
                .andExpect(status().isNotFound());
    }
}
