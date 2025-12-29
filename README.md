# CaseLine

CaseLine is a Spring Boot 3.5 case management application with Thymeleaf views, JWT-based authentication, and role-aware dashboards for organizations, administrators, and employees. Data is stored in MySQL and secured with Spring Security.

## Prerequisites
- Java 21
- Maven 3.9+
- MySQL 8.x

## Configuration
Create `src/main/resources/application.properties` with your database and JWT settings before running the application:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/caseline
spring.datasource.username=YOUR_DB_USER
spring.datasource.password=YOUR_DB_PASSWORD
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

jwt.secret=change_me_to_a_long_random_secret_key
```

## Run the application
```bash
mvn spring-boot:run
```

## Build a package
```bash
mvn clean package
```
The runnable jar is produced in `target/`.

## Tests
```bash
mvn test
```
Tests require a Java 21 toolchain; ensure your environment provides JDK 21 to avoid `release version 21 not supported` errors.

## Key features
- Organization onboarding with separate admin and employee flows
- Case creation, editing, and team member management
- Timeline posts and dashboards rendered with Thymeleaf under the `/CaseLine` path

