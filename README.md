# Tennis Reservation System API

A REST backend service for managing tennis courts, surface types, and player reservations. 

**Author:** Nikol Otáhalová

## Technologies
* **Core:** Java, Spring Boot 4.x / Spring Framework 7.x
* **Security:** Spring Security (JWT, Basic Auth)
* **Persistence:** Spring Data JPA, Hibernate, H2 Database (In-Memory)
* **Database Migrations:** Liquibase
* **Testing:** JUnit, Mockito (90% Coverage)

## How to start

The application utilizes an in-memory H2 database, requiring no external database installation. The schema and initial data are automatically seeded on startup via Liquibase.
To run the application, you need Maven and JDK.
To start, you cna use these scripts or start using IDE. (in tennis-reservation-system)

`mvn spring-boot:run `

or

`.\mvnw spring-boot:run` (Windows) 

`./mvnw spring-boot:run` (Linux/macOS)
## API Documentation

Documentation of the API is available as Postman Collection, which can be imported into postman.

## API Calls

API calls require authorization with JWT token, so first login with Basic authorization, save the token and use it.

The application database is seeded with two user profiles:
* **ADMIN** – Providing access to all features (username: `admin`, password: `admin`)
* **USER** – Having limited access to features per assignment (username: `user`, password: `user`)

## External configuration

In [application.properties](tennis-reservation-system/src/main/resources/application.properties) you  will find external configuration of JWT:
* JWT Secret Key (`jwt.secret`)
* JWT Token expiration (`jwt.access-token-expiration`)
* JWT Session expiration (`jwt.refresh-token-expiration`)

To seed database per assignment (2 Surface Types, 4 Courts), run (in tennis-reservation-system):

`mvn spring-boot:run "-Dspring-boot.run.arguments=--spring.liquibase.contexts=seed"`

or

`.\mvnw spring-boot:run "-Dspring-boot.run.arguments=--spring.liquibase.contexts=seed"`

## Artificial Intelligence

Artificial intelligence was used as assistant in creation of this project. Chat available in prompts.md (due to size, debugging prompts were omitted)
