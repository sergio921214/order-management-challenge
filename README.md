# Order Management System

## Description
This application is a challenge implementation for managing the lifecycle of orders, including state transitions and exception handling. It uses Java 21, Spring Boot, and Spring State Machine to ensure a robust, modular, and testable architecture.

## Features
- **State Management**: Orders transition through states (`CREATED`, `PAID`, `IN_FULFILLMENT`, `CLOSED`) managed by a state machine.
- **RESTful API**:
    - **Create Order**: `/orders` (POST)
    - **Confirm Payment**: `/orders/{id}/pay` (POST)
    - **Fulfill Order**: `/orders/{id}/fulfill` (POST)
- **Modular Design**: Each feature is isolated into its own module:
    - `api`: OpenAPI specification and model generation.
    - `db`: Database initialization scripts.
    - `common`: Shared utilities like exception handling.
    - `orders`: Main feature module, subdivided into `create`, `payment`, `fulfillment`, `statemachine`.
- **Robust Testing**: Includes unit, integration, and functional tests.

## Prerequisites
- Java 21
- Maven
- PostgreSQL

## Installation
1. Clone the repository.
2. Set up the database:
    - Create the `order_management` database manually in your local PostgreSQL instance:
      ```sql
      CREATE DATABASE order_management;
      ```
    - Flyway will automatically run the migration scripts in the `db` module (`src/main/resources/db/migration`) to initialize the schema and seed data.
3. Run `mvn clean install` to build all modules.
4. Start each module as needed. For example:
    - To run the `orders/create` module:
      ```bash
      mvn spring-boot:run -pl orders/create
      ```
    - To run the `orders/payment` module:
      ```bash
      mvn spring-boot:run -pl orders/payment
      ```
    - Repeat for other modules as required.

## Endpoints
### Create Order
```http
POST /orders
Content-Type: application/json
Body: {
  "items": [
    {"itemId": 1, "quantity": 2},
    {"itemId": 2, "quantity": 3}
  ]
}
Response: {
  "id": 1,
  "state": "CREATED",
  "items": [
    {"id": 1, "quantity": 2},
    {"id": 2, "quantity": 3}
  ],
  "createdAt": "2024-12-10T12:00:00",
  "updatedAt": "2024-12-10T12:00:00"
}
```
### Confirm Payment
```http
POST /orders/{id}/pay
Response: 200 OK
```
### Fulfill Order
```http
POST /orders/{id}/fulfill
Response: 200 OK
```

## Notes

- Fulfillment results are randomized (success or failure) to simulate real-world processing.
- State management persists through a PostgreSQL database for resilience.
- The common module centralizes exception handling and utility functions.
- Each module operates independently, reflecting a microservices-like architecture.
- Ensure the order_management database is manually created locally when running with the local profile.


