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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EmployeeService service;

    @Autowired
    private ObjectMapper objectMapper;

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
