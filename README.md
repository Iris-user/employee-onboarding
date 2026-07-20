# employee-onboarding
Spring Boot Employee Onboarding API

## API

| Method | Path             | Description                                                                 |
|--------|------------------|------------------------------------------------------------------------------|
| POST   | `/employees`     | Create an employee (`name`, `lastName`, `email`, `department`)               |
| GET    | `/employees`     | List all employees                                                          |
| GET    | `/employees/{id}`| Return an employee's details by ID. Unknown `id` returns `404 Not Found`.   |
| PUT    | `/employees/{id}`| Update an employee's `name`, `email`, `department`. Attempting to change `lastName` returns `400 Bad Request`. Unknown `id` returns `404 Not Found`. |

## API Documentation

Swagger UI: `http://localhost:8080/swagger-ui/index.html`

OpenAPI spec: `http://localhost:8080/v3/api-docs`
