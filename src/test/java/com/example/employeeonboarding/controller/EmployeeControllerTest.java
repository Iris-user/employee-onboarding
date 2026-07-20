package com.example.employeeonboarding.controller;

import com.example.employeeonboarding.exception.EmployeeNotFoundException;
import com.example.employeeonboarding.exception.LastNameUpdateNotAllowedException;
import com.example.employeeonboarding.model.Employee;
import com.example.employeeonboarding.service.EmployeeService;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EmployeeService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createEmployee_returnsCreatedEmployee() throws Exception {
        Employee toCreate = new Employee(null, "Alice", "Smith", "alice.smith@example.com", "Sales");
        Employee created = new Employee(1L, "Alice", "Smith", "alice.smith@example.com", "Sales");
        when(service.save(toCreate)).thenReturn(created);

        mockMvc.perform(post("/employees")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(toCreate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Alice"));
    }

    @Test
    void getAllEmployees_returnsListOfEmployees() throws Exception {
        Employee employee = new Employee(1L, "Alice", "Smith", "alice.smith@example.com", "Sales");
        when(service.getAll()).thenReturn(List.of(employee));

        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Alice"));
    }

    @Test
    void getEmployeeById_returnsEmployeeWhenIdExists() throws Exception {
        Employee employee = new Employee(1L, "Alice", "Smith", "alice.smith@example.com", "Sales");
        when(service.getById(1L)).thenReturn(employee);

        mockMvc.perform(get("/employees/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Alice"));
    }

    @Test
    void getEmployeeById_returns404WhenIdDoesNotExist() throws Exception {
        when(service.getById(99L)).thenReturn(null);

        mockMvc.perform(get("/employees/{id}", 99L))
                .andExpect(status().isNotFound());
    }

    @Test
    void getEmployeesByDepartment_returnsFilteredList() throws Exception {
        Employee emp = new Employee(1L, "Alice", "Smith", "alice.smith@example.com", "Engineering");
        when(service.getByDepartment("Engineering")).thenReturn(List.of(emp));

        mockMvc.perform(get("/employees/department/{department}", "Engineering"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Alice"))
                .andExpect(jsonPath("$[0].department").value("Engineering"));
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
    void updateEmployee_returnsOk_whenLastNameUnchanged() throws Exception {
        Employee updated = new Employee(1L, "Johnny", "Doe", "johnny.doe@example.com", "Sales");
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
        Employee attempted = new Employee(1L, "John", "Doeson", "john.doe@example.com", "Engineering");
        when(service.update(eq(1L), any(Employee.class)))
                .thenThrow(new LastNameUpdateNotAllowedException("Last name cannot be updated"));

        mockMvc.perform(put("/employees/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(attempted)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateEmployee_returnsNotFound_whenEmployeeDoesNotExist() throws Exception {
        Employee attempted = new Employee(99L, "Ghost", "Employee", "ghost@example.com", "Ops");
        when(service.update(eq(99L), any(Employee.class)))
                .thenThrow(new EmployeeNotFoundException("Employee not found"));

        mockMvc.perform(put("/employees/99")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(attempted)))
                .andExpect(status().isNotFound());
    }
}
