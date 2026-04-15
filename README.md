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

### Update Employee Last Name
`PATCH /employees/{id}/last-name`

Updates the last name of an existing employee.

**Request Body:**
```json
{
  "lastName": "Smith"
}
```

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
