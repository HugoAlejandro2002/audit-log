# Audit Log & Transactions API

**Java 17 · Quarkus · REST · JPA/Hibernate · MySQL · Docker · Cloud Run**

---

## Overview

This project is a **production-ready Java backend service** built with **Quarkus** and **Java 17**, designed to demonstrate enterprise-level backend development practices.

The system exposes a REST API for managing Customers and records **auditable events** for every state-changing operation, providing full traceability, reproducibility, and operational insight — a common requirement in real-world enterprise systems.

It is designed to run locally using Docker Compose and to be deployed in production using **Google Cloud Run**.

---

## Key Features

### RESTful API
- CRUD operations for Customers
- Proper HTTP status codes (201, 404, 409, etc.)
- Consistent API response envelope
- JSON payloads with Jakarta Validation

### Audit Logging (Enterprise-Grade)
Every CREATE, UPDATE, and DELETE operation generates an audit record with:
- Entity type and entity ID
- Action performed
- Actor ID (from request headers)
- Request ID (correlation ID)
- Timestamp
- Field-level diffs
- Before and after snapshots (JSON)

This enables:
- End-to-end traceability
- Root cause analysis
- Compliance-ready audit trails
- Cross-service log correlation

### Request Traceability
A global request filter extracts:
- `X-Request-Id`
- `X-Actor-Id`

These values are:
- Stored in a request-scoped context
- Injected into structured logs (MDC)
- Persisted in audit records
- Returned in API responses

This mirrors patterns used in distributed enterprise systems.

### Partial Updates (PATCH-style)
Updates use a **Patch Applier** pattern:
- Only provided fields are applied
- Validation is centralized
- Changes are tracked deterministically
- No controller or service spaghetti logic

Equivalent to `Partial<T>` semantics in TypeScript, implemented safely in Java.

### Optimistic Locking
- Entities use `@Version`
- Concurrent updates are detected
- Conflicts return `409 CONFLICT`
- Suitable for high-concurrency scenarios

### Health Checks
Built-in health endpoints using Quarkus SmallRye Health:
- `/health`
- `/health/live`

These endpoints are Cloud Run–ready and suitable for container orchestration and monitoring.

---

## Technology Stack

- Java 17
- Quarkus
- JPA / Hibernate ORM (Panache)
- MySQL
- Maven
- Docker
- Google Cloud Run
- Swagger / OpenAPI
- SmallRye Health

---

## Project Structure (Simplified)

```
src/main/java
├── common
│   ├── errors        # Exception mappers and API errors
│   ├── logging       # Centralized logging abstraction
│   ├── patch         # Generic patch / diff utilities
│   └── request       # RequestContext and filters
│
├── customers
│   ├── api           # REST resources
│   ├── domain        # JPA entities
│   ├── dto           # Request / Response DTOs
│   ├── repositories # Persistence layer
│   └── services      # Business logic
│
├── audit
│   ├── api
│   ├── domain
│   ├── repositories
│   └── services
```

---

## Running the Application (Dev Mode)

```bash
./mvnw quarkus:dev
```

- Live coding enabled
- Dev UI available at:
  http://localhost:8080/q/dev
- Swagger UI:
  http://localhost:8080/swagger

---

## Packaging for Production

### JVM Mode (Recommended for Cloud Run)

```bash
./mvnw package
```

This produces:

```
target/quarkus-app/
├── lib/
├── app/
├── quarkus/
└── quarkus-run.jar
```

Run locally:

```bash
java -jar target/quarkus-app/quarkus-run.jar
```

---

## Docker (Production Ready)

The project includes a production-ready Dockerfile:

```
src/main/docker/Dockerfile.jvm
```

Build the image:

```bash
docker build -f src/main/docker/Dockerfile.jvm -t transactions-api .
```

Run the container:

```bash
docker run -p 8080:8080 --env-file .env transactions-api
```

---

## Environment Configuration

All configuration is injected via environment variables:

```env
DB_USER=root
DB_PASSWORD=admin123
DB_HOST=localhost
DB_PORT=3306
DB_NAME=audit_logs

HIBERNATE_SCHEMA_STRATEGY=validate
QUARKUS_PROFILE=prod
```

This follows **12-factor app principles** and works seamlessly with Cloud Run.

---

## API Documentation

Swagger UI is enabled by default:

```
http://localhost:8080/swagger
```

---

## Cloud Run Deployment

- Application listens on port `8080`
- Uses environment variables for configuration
- Includes health endpoints for container checks
- JVM mode optimized for fast startup and low memory usage

The application is fully compatible with **Google Cloud Run**.

---

## Alignment with Java Backend Developer Role (Coderoad)

This project demonstrates:

- RESTful API design using JAX-RS
- Strong JPA / Hibernate usage
- SQL-friendly schema design and indexing
- Debugging and traceability using correlation IDs
- Concurrency handling with optimistic locking
- Clean, reusable, and maintainable code
- Dockerized production deployments
- Cloud-ready configuration and health checks

It reflects real-world backend systems used in enterprise environments.

---

## Summary

This is not a tutorial project.

It is a **production-oriented backend service** designed with:
- Maintainability
- Observability
- Traceability
- Concurrency safety
- Cloud deployment in mind

It represents the level of design and implementation expected from a **mid-to-senior Java Backend Developer**.

