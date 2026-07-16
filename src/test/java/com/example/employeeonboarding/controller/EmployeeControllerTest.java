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
    void createEmployee_returnsCreatedEmployee() throws Exception {
        Employee toCreate = new Employee(null, "Alice Smith", "alice.smith@example.com", "Sales");
        Employee created = new Employee(1L, "Alice Smith", "alice.smith@example.com", "Sales");
        when(service.save(toCreate)).thenReturn(created);

        mockMvc.perform(post("/employees")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(toCreate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Alice Smith"));
    }

    @Test
    void getAllEmployees_returnsListOfEmployees() throws Exception {
        Employee employee = new Employee(1L, "Alice Smith", "alice.smith@example.com", "Sales");
        when(service.getAll()).thenReturn(List.of(employee));

        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Alice Smith"));
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
