# ReelNet Common Package

This package contains common utilities, configurations, and base components used throughout the ReelNet application. The package is organized into several subpackages, each with a specific responsibility.

## Package Structure

```
com.learning.reelnet.common
├── api                    # API-related classes (controllers, requests, responses)
├── application            # Application layer components (CQRS, DTOs, services)
├── config                 # Application-wide configurations
├── domain                 # Domain-related base classes and interfaces
├── exception              # Exception handling and custom exceptions
├── infrastructure         # Infrastructure components
│   ├── actuator           # Spring Boot Actuator customizations
│   ├── cache              # Caching configuration
│   ├── events             # Event handling infrastructure
│   ├── metrics            # Metrics collection and reporting
│   └── security           # Security infrastructure
└── util                   # Common utility classes
```

## Key Components

### API Package

- **advice**: Contains controller advice classes for global exception handling and request/response logging
- **adapter**: Adapter classes for backward compatibility between different API response formats
- **controller**: Base controller interfaces and common controller functionality
- **response**: Standardized API response objects for consistent API responses

### Application Package

- **cqrs**: Command Query Responsibility Segregation pattern implementation
  - **Command**: Represents a request to change the application state
  - **CommandHandler**: Processes commands and produces results
  - **Query**: Represents a request for data
  - **QueryHandler**: Processes queries and returns results
- **dto**: Data Transfer Objects for communication between layers
- **service**: Common service interfaces and base implementations

### Config Package

- **SecurityConfig**: Spring Security configuration
- **WebMvcConfig**: Spring MVC configuration
- **OpenApiConfig**: Swagger/OpenAPI documentation configuration
- **Other configs**: Various application configurations

### Domain Package

- **entity**: Base entity interfaces and classes
- **repository**: Base repository interfaces
- **valueobject**: Value object base classes and common implementations

### Exception Package

- **ApiException**: Base exception for API errors
- **ProblemDetail**: RFC 7807 Problem Details implementation for standardized error responses
- **GlobalExceptionHandler**: Central exception handling for the application

### Infrastructure Package

- **actuator**: Custom actuator endpoints and health indicators
  - **CustomActuatorEndpoint**: Application-specific status information
  - **CustomInfoContributor**: Custom application info for the /info endpoint
  - **SystemResourcesEndpoint**: System resource information endpoint
- **cache**: Caching configuration and utilities
  - **RedisConfig**: Redis configuration for caching
- **events**: Event handling infrastructure
  - **EventMetadata**: Metadata container for domain events
  - **DomainEvent**: Base interface for domain events
  - **EventPublisher**: Interface for publishing domain events
- **metrics**: Metrics collection and monitoring
  - **MetricsConfig**: Configuration for application metrics
  - **HttpMetricsFilter**: Filter for collecting HTTP request metrics
- **security**: Security infrastructure
  - **helper**: Security helper classes (JWTHelper, etc.)
  - **utils**: Security utility classes
  - **validator**: Validators for security tokens

### Util Package

- **DateUtils**: Date and time manipulation utilities
- **StringUtils**: String manipulation utilities
- **Other utilities**: Various commonly used utility functions

## Usage Guidelines

### Exception Handling

Always use the standard exception hierarchy. For API errors, throw `ApiException` or its subclasses:

```java
// Example of throwing a business rule exception
if (user.isLocked()) {
    throw new BusinessRuleException("User is locked", "USER_LOCKED");
}
```

### API Responses

Use the standardized `ApiResponse` class for all controller responses:

```java
@GetMapping("/{id}")
public ResponseEntity<ApiResponse<UserDto>> getUser(@PathVariable Long id) {
    UserDto user = userService.findById(id);
    return ResponseEntity.ok(ApiResponse.success(user));
}
```

### CQRS Pattern

For complex business operations, use the CQRS pattern:

```java
// Command example
@Service
public class CreateUserCommandHandler implements CommandHandler<CreateUserCommand, UserDto> {
    @Override
    public UserDto handle(CreateUserCommand command) {
        // Implementation
    }
}

// Query example
@Service
public class GetUserQueryHandler implements QueryHandler<GetUserQuery, UserDto> {
    @Override
    public UserDto handle(GetUserQuery query) {
        // Implementation
    }
}
```

### Security

For security-related operations, use the provided utility classes:

```java
// Check if current user is authenticated
if (SecurityUtils.isAuthenticated()) {
    // Do something
}

// Get current username
String username = SecurityUtils.getCurrentUsername();
```

## Base Classes and Interfaces

### Base Entity

All domain entities should extend the `BaseEntity` class which provides:
- ID field
- Creation and update timestamps
- Version for optimistic locking
- Standard equality and hashCode based on identity

### Base Repository

Repositories should extend the appropriate base repository interface:
- `BaseRepository<T, ID>`: Base repository interface with common CRUD operations
- `ReadOnlyRepository<T, ID>`: Repository interface for read-only entities

### Base Value Objects

Value objects should implement the `ValueObject` interface which ensures:
- Immutability
- Value-based equality
- HashCode consistent with equals

## Metrics and Monitoring

The application automatically collects metrics for:
- HTTP requests and responses
- API errors
- Application status
- System resources

Custom metrics can be added using the `MeterRegistry`:

```java
@Autowired
private MeterRegistry meterRegistry;

public void doSomething() {
    Counter.builder("my.custom.counter")
           .description("My custom counter")
           .register(meterRegistry)
           .increment();
}
```

## Actuator Endpoints

Custom actuator endpoints are available:
- `/actuator/system-resources`: System resource information
- `/actuator/application-status`: Application status information

## Utility Classes

Common utility classes provide various helper methods:
- `StringUtils`: String manipulation utilities
- `DateUtils`: Date manipulation utilities
- `SecurityUtils`: Security-related utilities

Always use these utility classes instead of reimplementing the same functionality.
