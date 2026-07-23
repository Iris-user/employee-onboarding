package com.example.employeeonboarding.controller;

import com.example.employeeonboarding.exception.EmployeeNotFoundException;
import com.example.employeeonboarding.exception.LastNameUpdateNotAllowedException;
import com.example.employeeonboarding.model.Department;
import com.example.employeeonboarding.model.Employee;
import com.example.employeeonboarding.service.EmployeeService;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EmployeeService service;

    @Autowired
    private ObjectMapper objectMapper;

    private static final Department ENGINEERING = new Department(10L, "Engineering");

    private static final Department SALES = new Department(20L, "Sales");

    @Test
    void shouldCreateEmployeeWithLastName() throws Exception {
        Employee emp = new Employee(1L, "John", "Doe", "john.doe@example.com", ENGINEERING, 30, null);
        when(service.save(any(Employee.class))).thenReturn(emp);

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John\",\"lastName\":\"Doe\",\"email\":\"john.doe@example.com\",\"department\":{\"id\":10,\"name\":\"Engineering\"},\"age\":30}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.name").value("John"));
    }

    @Test
    void createEmployee_returnsBadRequest_whenRequiredFieldsMissing() throws Exception {
        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John\",\"lastName\":\"Doe\"}"))
                .andExpect(status().isBadRequest());

        verify(service, never()).save(any(Employee.class));
    }

    @Test
    void shouldGetAllEmployees() throws Exception {
        Employee emp = new Employee(1L, "John", "Doe", "john.doe@example.com", ENGINEERING, 30, null);
        when(service.getAll()).thenReturn(List.of(emp));

        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].lastName").value("Doe"));
    }

    @Test
    void getEmployeeById_returnsEmployeeWhenIdExists() throws Exception {
        Employee employee = new Employee(1L, "John", "Doe", "john.doe@example.com", ENGINEERING, 30, null);
        when(service.getById(1L)).thenReturn(employee);

        mockMvc.perform(get("/employees/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("John"));
    }

    @Test
    void getEmployeeById_returns404WhenIdDoesNotExist() throws Exception {
        when(service.getById(99L)).thenReturn(null);

        mockMvc.perform(get("/employees/{id}", 99L))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldUpdateLastName() throws Exception {
        Employee updated = new Employee(1L, "John", "Smith", "john.doe@example.com", ENGINEERING, 30, null);
        when(service.updateLastName(1L, "Smith")).thenReturn(updated);

        mockMvc.perform(patch("/employees/1/last-name")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"lastName\":\"Smith\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName").value("Smith"));
    }

    @Test
    void updateLastName_returnsNotFound_whenEmployeeDoesNotExist() throws Exception {
        when(service.updateLastName(99L, "Smith"))
                .thenThrow(new EmployeeNotFoundException("Employee not found with id: 99"));

        mockMvc.perform(patch("/employees/99/last-name")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"lastName\":\"Smith\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getEmployeesByDepartment_returnsFilteredList() throws Exception {
        Employee emp = new Employee(1L, "Alice", "Smith", "alice.smith@example.com", ENGINEERING, 27, null);
        when(service.getByDepartment("Engineering")).thenReturn(List.of(emp));

        mockMvc.perform(get("/employees/department/{department}", "Engineering"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Alice"))
                .andExpect(jsonPath("$[0].department.name").value("Engineering"));
    }

    @Test
    void getEmployeesByDepartment_returnsEmptyArrayWhenNoMatch() throws Exception {
        when(service.getByDepartment("Marketing")).thenReturn(List.of());

        mockMvc.perform(get("/employees/department/{department}", "Marketing"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getEmployeesByAge_returnsFilteredList() throws Exception {
        Employee emp = new Employee(1L, "Alice", "Smith", "alice.smith@example.com", ENGINEERING, 30, null);
        when(service.getByAge(30)).thenReturn(List.of(emp));

        mockMvc.perform(get("/employees/age/{age}", 30))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Alice"))
                .andExpect(jsonPath("$[0].age").value(30));
    }

    @Test
    void getEmployeesByAge_returnsEmptyArrayWhenNoMatch() throws Exception {
        when(service.getByAge(99)).thenReturn(List.of());

        mockMvc.perform(get("/employees/age/{age}", 99))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getEmployeesByEmail_returnsFilteredList() throws Exception {
        Employee emp = new Employee(1L, "Alice", "Smith", "alice.smith@example.com", ENGINEERING, 30, null);
        when(service.getByEmail("alice.smith@example.com")).thenReturn(List.of(emp));

        mockMvc.perform(get("/employees/email/{email}", "alice.smith@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Alice"))
                .andExpect(jsonPath("$[0].email").value("alice.smith@example.com"));
    }

    @Test
    void getEmployeesByEmail_returnsEmptyArrayWhenNoMatch() throws Exception {
        when(service.getByEmail("nobody@example.com")).thenReturn(List.of());

        mockMvc.perform(get("/employees/email/{email}", "nobody@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void updateEmployee_returnsOk_whenLastNameUnchanged() throws Exception {
        Employee updated = new Employee(1L, "Johnny", "Doe", "johnny.doe@example.com", SALES, 30, null);
        when(service.update(eq(1L), any(Employee.class))).thenReturn(updated);

        mockMvc.perform(put("/employees/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Johnny"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    void updateEmployee_returnsBadRequest_whenLastNameChanged() throws Exception {
        Employee attempted = new Employee(1L, "John", "Doeson", "john.doe@example.com", ENGINEERING, 30, null);
        when(service.update(eq(1L), any(Employee.class)))
                .thenThrow(new LastNameUpdateNotAllowedException("Last name cannot be updated"));

        mockMvc.perform(put("/employees/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(attempted)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateEmployee_returnsNotFound_whenEmployeeDoesNotExist() throws Exception {
        Employee attempted = new Employee(99L, "Ghost", "Employee", "ghost@example.com", ENGINEERING, 40, null);
        when(service.update(eq(99L), any(Employee.class)))
                .thenThrow(new EmployeeNotFoundException("Employee not found"));

        mockMvc.perform(put("/employees/99")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(attempted)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteEmployee_returnsNoContent_whenIdExists() throws Exception {
        mockMvc.perform(delete("/employees/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteEmployee_returnsNotFound_whenIdDoesNotExist() throws Exception {
        org.mockito.Mockito.doThrow(new EmployeeNotFoundException("Employee not found with id: 99"))
                .when(service).deleteById(99L);

        mockMvc.perform(delete("/employees/{id}", 99L))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteEmployees_returnsNoContent_whenAllIdsExist() throws Exception {
        mockMvc.perform(delete("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"ids\":[1,2]}"))
                .andExpect(status().isNoContent());

        verify(service).deleteByIds(List.of(1L, 2L));
    }

    @Test
    void deleteEmployees_returnsNotFound_whenAnyIdDoesNotExist() throws Exception {
        org.mockito.Mockito.doThrow(new EmployeeNotFoundException("Employee(s) not found with id(s): [99]"))
                .when(service).deleteByIds(List.of(1L, 99L));

        mockMvc.perform(delete("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"ids\":[1,99]}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getTemporaryAddress_returnsAddress_whenEmployeeExists() throws Exception {
        when(service.getTemporaryAddress(1L)).thenReturn("221B Baker Street");

        mockMvc.perform(get("/employees/{id}/temporary-address", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.temporaryAddress").value("221B Baker Street"));
    }

    @Test
    void getTemporaryAddress_returnsNotFound_whenEmployeeDoesNotExist() throws Exception {
        when(service.getTemporaryAddress(99L))
                .thenThrow(new EmployeeNotFoundException("Employee not found with id: 99"));

        mockMvc.perform(get("/employees/{id}/temporary-address", 99L))
                .andExpect(status().isNotFound());
    }
}
