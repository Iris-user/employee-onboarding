# employee-onboarding
Spring Boot Employee Onboarding API

## API Endpoints

### Create Employee
`POST /employees`

Creates a new employee. Supports `name`, `lastName`, `email`, and `department` fields.

**Request Body:**
```json
{
  "name": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "department": "Engineering"
}
```

### Get All Employees
`GET /employees`

Returns a list of all employees.

### Get Employee By ID
`GET /employees/{id}`

Returns an employee's details by ID. Returns `404 Not Found` if the ID doesn't exist.

### Get Employees By Department
`GET /employees/department/{department}`

Returns a list of employees filtered by department.

### Update Employee
`PUT /employees/{id}`

Updates an existing employee's `name`, `email`, and `department`. Returns `404 Not Found` if the ID doesn't exist.

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

## API Documentation

Swagger UI: `http://localhost:8080/swagger-ui/index.html`

OpenAPI spec: `http://localhost:8080/v3/api-docs`

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
