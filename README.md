# Employee Onboarding API

Spring Boot REST API for managing employee onboarding.

## Tech Stack

- Java 21
- Spring Boot 4.x
- Spring Data JPA + H2 (in-memory)
- Lombok
- JUnit 5 + Mockito
- Maven

## API Endpoints

| Method | Endpoint            | Description                        |
|--------|---------------------|------------------------------------|
| POST   | `/employees`        | Create a new employee              |
| GET    | `/employees`        | List all employees                 |
| PUT    | `/employees/{id}`   | Update an existing employee by ID  |

## Employee Model

```json
{
  "id": 1,
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "department": "Engineering"
}
```

## Build & Run

```bash
mvn clean install
mvn spring-boot:run
```

## Run Tests

```bash
mvn test
```

## Features

- **KAN-8**: Employee Update — Added `firstName`/`lastName` fields and `PUT /employees/{id}` endpoint for updating employee details
