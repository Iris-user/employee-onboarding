package com.example.employeeonboarding.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "name is required")
    private String name;

    @NotBlank(message = "lastName is required")
    private String lastName;

    @NotBlank(message = "email is required")
    private String email;

    @NotBlank(message = "department is required")
    private String department;

    @NotNull(message = "age is required")
    private Integer age;
}
