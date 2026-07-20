package com.example.employeeonboarding.controller;

import com.example.employeeonboarding.model.Employee;
import com.example.employeeonboarding.service.EmployeeService;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

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
    void getAllEmployees_returnsList() throws Exception {
        Employee emp = new Employee(1L, "Alice", "alice@example.com", "Engineering");
        when(service.getAll()).thenReturn(List.of(emp));

        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Alice"));
    }

    @Test
    void createEmployee_persistsAndReturnsEmployee() throws Exception {
        Employee input = new Employee(null, "Bob", "bob@example.com", "Sales");
        Employee saved = new Employee(1L, "Bob", "bob@example.com", "Sales");
        when(service.save(input)).thenReturn(saved);

        mockMvc.perform(post("/employees")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void getEmployeeById_returnsEmployeeWhenIdExists() throws Exception {
        Employee employee = new Employee(1L, "Alice Smith", "alice.smith@example.com", "Sales");
        when(service.getById(1L)).thenReturn(employee);

        mockMvc.perform(get("/employees/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Alice Smith"));
    }

    @Test
    void getEmployeeById_returns404WhenIdDoesNotExist() throws Exception {
        when(service.getById(99L)).thenReturn(null);

        mockMvc.perform(get("/employees/{id}", 99L))
                .andExpect(status().isNotFound());
    }

    @Test
    void getEmployeesByDepartment_returnsFilteredList() throws Exception {
        Employee emp = new Employee(1L, "Alice", "alice@example.com", "Engineering");
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
    void updateEmployee_returnsUpdatedEmployeeWhenIdExists() throws Exception {
        Employee changes = new Employee(null, "Alice Smith", "alice.smith@example.com", "Sales");
        Employee updated = new Employee(1L, "Alice Smith", "alice.smith@example.com", "Sales");
        when(service.update(1L, changes)).thenReturn(updated);

        mockMvc.perform(put("/employees/{id}", 1L)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(changes)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alice Smith"))
                .andExpect(jsonPath("$.department").value("Sales"));
    }

    @Test
    void updateEmployee_returns404WhenIdDoesNotExist() throws Exception {
        Employee changes = new Employee(null, "Alice Smith", "alice.smith@example.com", "Sales");
        when(service.update(99L, changes)).thenReturn(null);

        mockMvc.perform(put("/employees/{id}", 99L)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(changes)))
                .andExpect(status().isNotFound());
    }
}
