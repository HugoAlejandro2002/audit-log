# Audit Log & Transactions API
<p align="center">Production-ready auditing and customer management API with Java 17 + Quarkus.</p>

![Swagger](https://img.shields.io/badge/Swagger-OpenAPI%203.0-85EA2D?logo=swagger&logoColor=white) ![OpenAPI](https://img.shields.io/badge/OpenAPI-3.0-6BA539?logo=openapi-initiative&logoColor=white) ![Java](https://img.shields.io/badge/Java-17-007396?logo=openjdk&logoColor=white) ![Quarkus](https://img.shields.io/badge/Quarkus-3.x-4695EB?logo=quarkus&logoColor=white) ![Hibernate](https://img.shields.io/badge/Hibernate-ORM-59666C?logo=hibernate&logoColor=white) ![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?logo=mysql&logoColor=white) ![Maven](https://img.shields.io/badge/Maven-3.9%2B-C71A36?logo=apachemaven&logoColor=white) ![Docker](https://img.shields.io/badge/Docker-Production-2496ED?logo=docker&logoColor=white) ![Cloud%20Run](https://img.shields.io/badge/Google%20Cloud-Run-4285F4?logo=googlecloud&logoColor=white) ![SmallRye Health](https://img.shields.io/badge/SmallRye-Health-5E5E5E?logo=quarkus&logoColor=white)


## Table of Contents

- [Overview](#overview)
- [Key Features](#key-features)
  - [RESTful API](#restful-api)
  - [Audit Logging (Enterprise-Grade)](#audit-logging-enterprise-grade)
  - [Request Traceability](#request-traceability)
  - [Partial Updates (PATCH-style)](#partial-updates-patch-style)
  - [Optimistic Locking](#optimistic-locking)
  - [Health Checks](#health-checks)
- [Technology Stack](#technology-stack)
- [Project Structure (Simplified)](#project-structure-simplified)
- [Running the Application (Dev Mode)](#running-the-application-dev-mode)
- [Packaging for Production](#packaging-for-production)
  - [JVM Mode (Recommended for Cloud Run)](#jvm-mode-recommended-for-cloud-run)
- [Docker (Production Ready)](#docker-production-ready)
- [Environment Configuration](#environment-configuration)
- [API Documentation](#api-documentation)
  - [Swagger UI Configuration](#swagger-ui-configuration)
  - [Documentation Access & Security (Basic Auth)](#documentation-access--security-basic-auth)
  - [Users & Roles (Properties Files)](#users--roles-properties-files)
  - [Request Formats (JSON & multipart/form-data)](#request-formats-json--multipartform-data)
- [Cloud Run Deployment](#cloud-run-deployment)
- [Code References](#code-references)
- [License](#license)
- [Authors](#authors)

## Overview

This project is a **production-ready Java backend service** built with **Quarkus** and **Java 17**, designed to demonstrate enterprise-level backend development practices.

The system exposes a REST API for managing Customers and records **auditable events** for every state-changing operation, providing full traceability, reproducibility, and operational insight — a common requirement in real-world enterprise systems.

It is designed to run locally using Docker Compose and to be deployed in production using **Google Cloud Run**.

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

## Running the Application (Dev Mode)

```bash
./mvnw quarkus:dev
```

- Live coding enabled
- Dev UI available at:
  http://localhost:8080/q/dev
- Swagger UI:
  http://localhost:8080/swagger

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

## API Documentation
 
 Swagger UI and OpenAPI are enabled and documented using annotations on resources and DTOs.
 
 - Swagger UI:
   - http://localhost:8080/swagger
   - Also available under Quarkus non-application root path: http://localhost:8080/q/swagger
 - OpenAPI JSON:
   - http://localhost:8080/openapi
   - http://localhost:8080/q/openapi
 
 ### Swagger UI Configuration
 
 In `application.properties`:
 
 ```properties
 quarkus.swagger-ui.enabled=true
 quarkus.swagger-ui.always-include=true
 quarkus.swagger-ui.path=/swagger
 quarkus.smallrye-openapi.path=/openapi
 quarkus.swagger-ui.title=Audit Log API
 quarkus.swagger-ui.theme=material
 quarkus.swagger-ui.footer=Audit Log - v1
 quarkus.swagger-ui.deep-linking=true
 quarkus.swagger-ui.display-request-duration=true
 quarkus.swagger-ui.doc-expansion=list
 quarkus.swagger-ui.filter=true
 quarkus.swagger-ui.tags-sorter=alpha
 quarkus.swagger-ui.operations-sorter=alpha
 ```
 
 ### Documentation Access & Security (Basic Auth)
 
 Protect Swagger UI and the OpenAPI document with Basic Auth using Elytron (file-based users/roles).
 
 Dependency:
 
 ```xml
 <dependency>
   <groupId>io.quarkus</groupId>
   <artifactId>quarkus-elytron-security-properties-file</artifactId>
 </dependency>
 ```
 
 Configuration:
 
 ```properties
 quarkus.http.auth.basic=true
 quarkus.security.users.file.enabled=true
 quarkus.security.users.file.plain-text=true
 
 # Users/Roles files (can be externalized via environment variables)
 quarkus.security.users.file.users=${USERS_FILE_PATH:users.properties}
 quarkus.security.users.file.roles=${ROLES_FILE_PATH:roles.properties}
 
 # Route protections (with and without trailing slash, and /q/* variants)
 quarkus.http.auth.permission.swagger.paths=/swagger/*
 quarkus.http.auth.permission.swagger.policy=authenticated
 quarkus.http.auth.permission.swagger2.paths=/swagger
 quarkus.http.auth.permission.swagger2.policy=authenticated
 
 quarkus.http.auth.permission.openapi.paths=/openapi
 quarkus.http.auth.permission.openapi.policy=authenticated
 quarkus.http.auth.permission.openapi2.paths=/openapi/*
 quarkus.http.auth.permission.openapi2.policy=authenticated
 
 quarkus.http.auth.permission.swaggerq.paths=/q/swagger/*
 quarkus.http.auth.permission.swaggerq.policy=authenticated
 quarkus.http.auth.permission.swaggerq2.paths=/q/swagger
 quarkus.http.auth.permission.swaggerq2.policy=authenticated
 
 quarkus.http.auth.permission.openapiq.paths=/q/openapi
 quarkus.http.auth.permission.openapiq.policy=authenticated
 quarkus.http.auth.permission.openapiq2.paths=/q/openapi/*
 quarkus.http.auth.permission.openapiq2.policy=authenticated
 ```
 
 Credential files (by default under `src/main/resources`):
 
 - `users.properties`
 
   ```
   docadmin=admin123
   ```
 
 - `roles.properties`
 
   ```
   docadmin=admin
   ```
 
 Example credentials:
 
 - Usuario: `docadmin`
 - Password: `admin123`
 
 For production, use external files and environment variables:
 
 - `USERS_FILE_PATH=C:\secure\users.properties`
 - `ROLES_FILE_PATH=C:\secure\roles.properties`
 
 If you prefer to require a specific role to access documentation, change the policy:
 
 ```properties
 quarkus.http.auth.permission.swagger.policy=roles
 quarkus.http.auth.permission.swagger.roles=admin,doc
 quarkus.http.auth.permission.openapi.policy=roles
 quarkus.http.auth.permission.openapi.roles=admin,doc
 ```
 
 #### Users & Roles (Properties Files)
 
 Quarkus Elytron loads users and roles from `.properties` files:
 
- `users.properties`: username=password (per line)
- `roles.properties`: username=role1,role2 (per line)
 
 Basic example (included in the project):
 
 - `src/main/resources/users.properties`
 
   ```properties
   docadmin=admin123
   ```
 
 - `src/main/resources/roles.properties`
 
   ```properties
   docadmin=admin
   ```
 
 Notes:
 - With `quarkus.security.users.file.plain-text=true` passwords are plain text. For hashes, set `plain-text=false` and use supported formats.
 - You can add more users/roles by adding lines to each file.
 - Recommended for production: external files and environment variables
   - `USERS_FILE_PATH=C:\secure\users.properties`
   - `ROLES_FILE_PATH=C:\secure\roles.properties`
 - If you want to require a specific role to access documentation, change the policy to `roles` and define allowed roles:
 
   ```properties
   quarkus.http.auth.permission.swagger.policy=roles
   quarkus.http.auth.permission.swagger.roles=admin,doc
   quarkus.http.auth.permission.openapi.policy=roles
   quarkus.http.auth.permission.openapi.roles=admin,doc
   ```
 
 Troubleshooting:
- If the login loops, ensure the URL matches the protected paths (with/without trailing slash and `/q/*` variants).
- Clear the browser cache and restart the app after changing users/roles.
- Ensure `USERS_FILE_PATH` and `ROLES_FILE_PATH` point to existing files.
 
 ### Documentation Coverage
 
- Resources and DTOs annotated with descriptions, examples, and parameters:
  - Customers: create, list, get, update, delete with response and error examples (400/404/409/500).
  - Audit: list and get with examples (200/400/500).
- Models:
  - ApiResponse, ApiError, ApiErrorDetail, Meta, Customer, AuditEvent with `@Schema` and examples.
- Global definition:
  - `OpenApiConfig` with `@OpenAPIDefinition` (Info, Servers, Tags).
 
 ## Request Formats (JSON & multipart/form-data)
 
 Customer endpoints accept both JSON and multipart/form-data where applicable:
 
- Create customer
  - JSON: `POST /api/v1/customers` with `CreateCustomerRequest`
  - multipart: `POST /api/v1/customers` with `CreateCustomerForm`
- Update customer
  - JSON: `PUT /api/v1/customers/{id}` with `UpdateCustomerRequest`
  - multipart: `PUT /api/v1/customers/{id}` with `UpdateCustomerForm`
 
 This facilitates form-based integrations while maintaining compatibility with JSON clients.

## Cloud Run Deployment

- Application listens on port `8080`
- Uses environment variables for configuration
- Includes health endpoints for container checks
- JVM mode optimized for fast startup and low memory usage

The application is fully compatible with **Google Cloud Run**.

## Code References

- Customer REST resources: [CustomerResource.java](src/main/java/org/hugo/customers/api/CustomerResource.java)
- Audit REST resources: [AuditResource.java](src/main/java/org/hugo/audit/api/AuditResource.java)
- Request context filter: [RequestContextFilter.java](src/main/java/org/hugo/common/request/RequestContextFilter.java)
- API response envelope: [ApiResponse.java](src/main/java/org/hugo/common/response/ApiResponse.java)
- Application configuration: [application.properties](src/main/resources/application.properties)
- Dockerfile (JVM): [Dockerfile.jvm](src/main/docker/Dockerfile.jvm)

## License

This project is licensed under the MIT License. See details in the [LICENSE](LICENSE) file.

## Authors

<table align="center">
  <tr>
    <!-- Hugo -->
    <td align="center" style="padding:20px;">
      <a href="https://github.com/HugoAlejandro2002">
        <img src="https://avatars.githubusercontent.com/u/97768733?v=4"
             width="90"
             alt="HugoAlejandro"
             style="border-radius:50%" />
        <br />
        <sub><b>Hugo Alejandro</b></sub>
      </a>
      <br />
      <span style="font-size:13px; font-weight:600;">
        Author
      </span>
      <br />
      <span style="font-size:13px;">
        Backend & AI Developer · Automation Developer · Cloud Engineer
      </span>
      <br /><br />
      <a href="https://www.linkedin.com/in/alejandro-apaza2002/">
        <img src="https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white" />
      </a>
      <a href="https://github.com/HugoAlejandro2002">
        <img src="https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white" />
      </a>
    </td>
    <!-- Joseph -->
    <td align="center" style="padding:20px;">
      <a href="https://github.com/ElJoamy">
        <img src="https://avatars.githubusercontent.com/u/68487005?v=4"
             width="90"
             alt="ElJoamy"
             style="border-radius:50%" />
        <br />
        <sub><b>Joseph Meneses (ElJoamy)</b></sub>
      </a>
      <br />
      <span style="font-size:13px; font-weight:600;">
        Contributor
      </span>
      <br />
      <span style="font-size:13px;">
        Backend & AI Developer · Cybersecurity Engineer · DBA
      </span>
      <br /><br />
      <a href="https://linkedin.com/in/joamy5902">
        <img src="https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white" />
      </a>
      <a href="https://github.com/ElJoamy">
        <img src="https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white" />
      </a>
    </td>
  </tr>
</table>
