# employee-onboarding
Spring Boot Employee Onboarding API

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/employees` | Create a new employee (supports `lastName`) |
| GET | `/employees` | List all employees |
| PUT | `/employees/{id}` | Update an existing employee |

## Employee Model

```json
{
  "id": 1,
  "name": "Jane",
  "lastName": "Doe",
  "email": "jane@example.com",
  "department": "HR"
}
```

## Features

- **Employee Creation**: Create an employee with `name`, `lastName`, `email`, and `department`.
- **Employee Update**: Update any field of an existing employee, including `lastName`, via `PUT /employees/{id}`.

## Build & Run

```bash
mvn clean install
mvn spring-boot:run
```

## Testing

```bash
mvn test
```

## Tech Stack
- Java 21
- Spring Boot 4.x
- H2 in-memory database
- JUnit 5 / Mockito
- JaCoCo (code coverage)
