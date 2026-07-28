# Shopping Cart API

Spring Boot REST API for managing customer shopping carts and tracking offer statistics.

The application provides functionality for:

- Creating and retrieving customer carts
- Adding items to carts
- Removing items from carts
- Evicting carts
- Validating offer prices
- Tracking offer statistics
- Providing statistics about offers sold during a specific period

## Architecture

The application follows a layered architecture:

- Controller layer - REST API endpoints
- Service layer - business logic
- Repository layer - MongoDB persistence
- Domain layer - cart, item and price models

Offer statistics are stored separately from carts to keep cart management and reporting concerns separated.


## Technology Stack

- Java 25
- Spring Boot 4
- Spring Web MVC
- Spring Data MongoDB
- MongoDB
- Testcontainers
- JUnit 5
- Mockito
- Gradle
- Docker
- JaCoCo

## Running the Application

### Prerequisites

Install:

- Java 25
- Docker

## Docker Compose

The application uses Spring Boot Docker Compose support:

```shell
spring-boot-docker-compose = { module = "org.springframework.boot:spring-boot-docker-compose", version.ref = "spring-boot" }
```

When the application starts, Spring Boot automatically:

- detects the `compose.yaml` / `docker-compose.yml` file
- starts the required containers
- creates service connections
- configures application properties automatically

No manual `docker compose up` command is required.

## Run Application

Using Gradle:

```shell
./gradlew bootRun
```

Spring Boot will automatically start MongoDB through Docker Compose.

## Postman Collection

A Postman collection is provided in the `postman` folder for testing the API endpoints.

Import the collection into Postman and configure the base URL:

- http://localhost:8080/api/v1

### Available endpoints:

Cart

| Method | Endpoint                             | Description            |
|--------|--------------------------------------|------------------------|
| GET    | `/carts/{customerId}`                | Retrieve customer cart |
| POST   | `/carts/{customerId}/items`          | Add item to cart       |
| DELETE | `/carts/{customerId}/items/{itemId}` | Remove item from cart  |
| DELETE | `/carts/{customerId}`                | Evict customer cart    |

Offer Statistics

| Method | Endpoint                       | Description                                     |
|--------|--------------------------------|-------------------------------------------------|
| GET    | `/statistics/offers/{offerId}` | Retrieve offer statistics for a specific period |

Example:

`GET /api/v1/statistics/offers/MOBILE_L?action=ADD&from=2026-01-01T00:00:00&to=2026-12-31T23:59:59`

The collection contains examples for:

- Getting customer carts
- Adding cart items
- Removing cart items
- Evicting carts
- Retrieving offer statistics

## API Documentation

Swagger UI is available at:

http://localhost:8080/swagger-ui/index.html

OpenAPI specification:

http://localhost:8080/v3/api-docs

## CI/CD

The project includes a GitHub Actions workflow that automatically:

- builds the application
- runs unit and integration tests
- generates Jacoco coverage reports

The workflow is triggered on pull requests and pushes to the main branch.