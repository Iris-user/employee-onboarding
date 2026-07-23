# employee-onboarding
Spring Boot Employee Onboarding API

## API Endpoints

### Create Employee
`POST /employees`

Creates a new employee. Requires all fields: `name`, `lastName`, `email`, `department`, and `age`. `department` is a reference to an existing department by `id` (see [Create Department](#create-department)). `temporaryAddress`, `permanentAddress`, and `dob` are optional. Returns `400 Bad Request` if any required field is missing or blank, or `404 Not Found` if the referenced department `id` doesn't exist.

**Request Body:**
```json
{
  "name": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "department": { "id": 1 },
  "age": 30,
  "temporaryAddress": "42 Elm Street",
  "permanentAddress": "10 Downing Street",
  "dob": "1990-05-15"
}
```

### Get All Employees
`GET /employees`

Returns a list of all employees.

### Get Employee By ID
`GET /employees/{id}`

Returns an employee's details, including `dob`, by ID. Returns `404 Not Found` if the ID doesn't exist.

### Get Employees By Department
`GET /employees/department/{department}`

Returns a list of employees filtered by department.

### Get Employees By Age
`GET /employees/age/{age}`

Returns a list of employees filtered by age. Returns an empty list if no employees match.

### Get Employees By Email
`GET /employees/email/{email}`

Returns a list of employees filtered by email address. Returns an empty list if no employees match.

### Get Employee's Temporary Address
`GET /employees/{id}/temporary-address`

Returns an employee's temporary address by ID. Returns `404 Not Found` if the ID doesn't exist.

**Response Body:**
```json
{
  "temporaryAddress": "42 Elm Street"
}
```

### Get Employee's Permanent Address
`GET /employees/{id}/permanent-address`

Returns an employee's permanent address by ID. Returns `404 Not Found` if the ID doesn't exist.

**Response Body:**
```json
{
  "permanentAddress": "10 Downing Street"
}
```

### Update Employee
`PUT /employees/{id}`

Updates an existing employee's `name`, `email`, and `department`. Returns `404 Not Found` if the employee ID or the referenced department `id` doesn't exist.

### Update Employee Last Name
`PATCH /employees/{id}/last-name`

Updates the last name of an existing employee. Returns `404 Not Found` if the ID doesn't exist.

**Request Body:**
```json
{
  "lastName": "Smith"
}
```

### Delete Employee
`DELETE /employees/{id}`

Deletes an employee by ID. Returns `204 No Content` on success, or `404 Not Found` if the ID doesn't exist.

### Delete Multiple Employees
`DELETE /employees`

Deletes employees for a list of IDs passed in the request body. Returns `204 No Content` if all IDs exist and are deleted, or `404 Not Found` if any ID doesn't exist (no employees are deleted in that case).

```json
{
  "ids": [1, 2, 3]
}
```

### Create Department
`POST /departments`

Creates a new department. Requires `name`. Returns `400 Bad Request` if `name` is missing or blank.

**Request Body:**
```json
{
  "name": "Engineering"
}
```

### Get All Departments
`GET /departments`

Returns a list of all departments.

### Get Department By ID
`GET /departments/{id}`

Returns a department's details by ID. Returns `404 Not Found` if the ID doesn't exist.

### Get Department List (POST)
`POST /departments/list`

Returns a list of all departments. Equivalent to [Get All Departments](#get-all-departments), exposed as a POST endpoint for clients that require a POST-based list request.

## API Documentation

Swagger UI: `http://localhost:8080/swagger-ui/index.html`

OpenAPI spec: `http://localhost:8080/v3/api-docs` (title: "Employee Onboarding API", version: "1.0")

## Tech Stack
- Java 21
- Spring Boot 4.0.5
- Spring Data JPA
- H2 (in-memory database)
- Lombok
- JUnit 5 + Mockito (testing)

## Build & Run

```bash
mvn clean install
mvn spring-boot:run
```

## Test

```bash
mvn test
```
