# ReelNetApp Backend

This document provides detailed information about the common components, utilities, configurations, and base classes in the ReelNetApp backend application.

## Table of Contents

- [Project Structure](#project-structure)
- [Common Components](#common-components)
  - [Utilities](#utilities)
  - [Model Classes](#model-classes)
  - [Exception Handling](#exception-handling)
  - [API Response Structures](#api-response-structures)
  - [CQRS Pattern](#cqrs-pattern)
- [Configuration](#configuration)
  - [Security Configuration](#security-configuration)
  - [CORS Configuration](#cors-configuration)
  - [Environment Configuration](#environment-configuration)
- [Monitoring & Actuator](#monitoring--actuator)
- [Constants & Enums](#constants--enums)
- [Setup Instructions](#setup-instructions)

## Project Structure

The backend follows a clean architecture with these key packages:

```
com.learning.reelnet.common
│
├── api                 # API related classes (response structures, advisors)
├── application         # Application layer (CQRS patterns)
├── config              # Configuration classes
├── exception           # Exception handling
├── infrastructure      # Infrastructure concerns
│   ├── actuator        # Actuator endpoints
│   ├── config          # Infrastructure configuration
│   ├── events          # Domain events
│   ├── metrics         # Metrics collection
│   └── security        # Security components
├── model               # Domain model classes
│   ├── base            # Base entity classes
│   ├── criteria        # Search criteria classes
│   └── enums           # Enumeration classes
└── util                # Utility classes
```

## Common Components

### Utilities

#### ValidationUtils

A comprehensive utility class for validating various data inputs:

- `isValidEmail(String email)`: Validates email addresses
- `isValidPassword(String password)`: Validates passwords (8+ chars, numbers, uppercase, lowercase, special chars)
- `isValidPhoneNumber(String phoneNumber)`: Validates phone numbers
- `isValidUsername(String username)`: Validates usernames (3-20 alphanumeric chars, underscores, hyphens)
- `isValidZipCode(String zipCode)`: Validates US zip codes
- `isValidUrl(String url)`: Validates URLs
- `hasMinLength(String str, int minLength)`: Checks string minimum length
- `hasMaxLength(String str, int maxLength)`: Checks string maximum length
- `isLengthBetween(String str, int minLength, int maxLength)`: Validates string length range
- `isInRange(int/double value, int/double min, int/double max)`: Validates numeric range
- `isAlpha/isAlphaSpace/isAlphanumeric/isNumeric`: Character type validation methods

**Usage Example:**
```java
if (!ValidationUtils.isValidEmail(userEmail)) {
    throw new ValidationException("Invalid email format");
}

if (!ValidationUtils.isValidPassword(password)) {
    throw new ValidationException("Password doesn't meet security requirements");
}
```

#### StringUtils

Utility for string manipulation operations:

- `isEmpty(String str)` / `isNotEmpty(String str)`: Check if a string is empty
- `defaultIfNull(String str)` / `defaultIfEmpty(String str, String defaultValue)`: Return fallback values
- `randomAlphanumeric(int length)` / `randomUuid()`: Generate random strings
- `truncate(String str, int maxLength)`: Truncate strings with ellipsis
- `toSlug(String input)`: Convert strings to URL-friendly slugs
- `join(List<String> list, String delimiter)` / `split(String str, String delimiter)`: Join/split operations
- `capitalizeWords(String str)`: Capitalize first letter of each word
- `mask(String str, int start, int end)`: Mask parts of strings with asterisks
- `extractDomainFromEmail(String email)`: Extract domain from email address

**Usage Example:**
```java
String slug = StringUtils.toSlug("Hello World Example"); // "hello-world-example"
String masked = StringUtils.mask("1234567890", 4, 8);    // "1234****90"
```

### Model Classes

#### Base Classes

##### BaseValueObject

Abstract base class for immutable value objects in Domain-Driven Design:

- `validate()`: Validates the value object's state
- `equals(Object o)` / `hashCode()`: Compare by value, not identity
- `copy()`: Creates modified copies while maintaining immutability
- `sameValueAs(BaseValueObject other)`: Type-safe equality check

**Usage Example:**
```java
public final class Money extends BaseValueObject {
    private final BigDecimal amount;
    private final Currency currency;
    
    // Constructor with validation
    public Money(BigDecimal amount, Currency currency) {
        this.amount = amount;
        this.currency = currency;
        validate();
    }
    
    @Override
    protected void validate() {
        if (amount == null || currency == null) {
            throw new IllegalArgumentException("Amount and currency are required");
        }
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
    }
    
    // Remaining methods...
}
```

##### BaseAggregateRoot

Base class for aggregate roots in Domain-Driven Design:

- `registerEvent(DomainEvent event)`: Register domain events to be published
- `clearEvents()` / `getDomainEvents()`: Manage the domain events
- `validateInvariants()`: Ensure aggregate consistency
- `createSnapshot()` / `applySnapshot(Serializable snapshot)`: Support for event sourcing

**Usage Example:**
```java
public class User extends BaseAggregateRoot<UUID> {
    private String username;
    private String email;
    
    public void changeEmail(String newEmail) {
        if (!ValidationUtils.isValidEmail(newEmail)) {
            throw new IllegalArgumentException("Invalid email format");
        }
        
        String oldEmail = this.email;
        this.email = newEmail;
        
        // Register a domain event
        registerEvent(new UserEmailChangedEvent(this.getId(), oldEmail, newEmail));
        
        validateInvariants();
    }
    
    @Override
    protected void validateInvariants() {
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(email)) {
            throw new IllegalStateException("User must have both username and email");
        }
    }
    
    // Other methods...
}
```

#### Search Criteria

##### SearchCriteria

Class for building dynamic queries with flexible search operations:

- Contains `key`, `operation`, `value`, and `combinator` fields
- Helper methods like `isEquals()`, `isNotEquals()`, `isLike()`, etc.
- Supports various comparison operations (=, !=, >, <, >=, <=, LIKE, IN, etc.)
- Supports combining multiple criteria with AND/OR

**Usage Example:**
```java
// Find users with name containing "john" AND age >= 21
List<SearchCriteria> criteria = new ArrayList<>();
criteria.add(new SearchCriteria("name", "LIKE", "%john%"));
criteria.add(new SearchCriteria("age", ">=", 21));

// Use with a specification pattern or criteria builder
List<User> users = userRepository.findAll(new UserSpecification(criteria));
```

##### FilterOperation

Enum defining standardized filter operations:

- Basic comparisons: EQUALS, NOT_EQUALS, GREATER_THAN, LESS_THAN, etc.
- Collection operations: IN, NOT_IN, BETWEEN
- String operations: LIKE, CONTAINS, STARTS_WITH, ENDS_WITH
- Null checks: IS_NULL, IS_NOT_NULL
- Each operation has a code (for API use) and SQL operator mapping

**Usage Example:**
```java
// In a REST controller
@GetMapping("/users")
public List<User> searchUsers(@RequestParam String nameOp, @RequestParam String name) {
    FilterOperation operation = FilterOperation.fromCode(nameOp);
    // Apply filter based on operation
}
```

### Exception Handling

#### ApiException

Base exception class for all API exceptions with:
- Error code
- HTTP status
- Additional parameters

#### ResourceNotFoundException

Exception for when a requested resource doesn't exist:
- Returns a 404 status code
- Factory method `forEntity(String entityName, Object id)` for consistent messages

**Usage Example:**
```java
User user = userRepository.findById(id)
    .orElseThrow(() -> ResourceNotFoundException.forEntity("User", id));
```

#### BusinessException

Exception for business rule violations:
- Returns a 422 (Unprocessable Entity) status code
- Support for naming the violated business rule

**Usage Example:**
```java
if (user.getBalance().compareTo(amount) < 0) {
    throw new BusinessException("Insufficient funds for withdrawal");
}
```

#### GlobalExceptionHandler

Centralized exception handling for consistent API responses:
- Handles all custom exceptions (ApiException, ValidationException, etc.)
- Converts Spring validation errors (MethodArgumentNotValidException, ConstraintViolationException)
- Handles common errors (AccessDeniedException, HttpMessageNotReadableException)
- Returns standardized ErrorResponse objects

### API Response Structures

#### ApiResponse

Generic wrapper for all API responses:
- Generic type for the data payload
- Consistent structure with status, message, and timestamp
- Static factory methods for success and error responses

**Usage Example:**
```java
@GetMapping("/{id}")
public ApiResponse<UserDto> getUser(@PathVariable Long id) {
    User user = userService.findById(id);
    return ApiResponse.success(mapper.toDto(user), "User retrieved successfully");
}
```

#### PagedResponse

Extension of ApiResponse for paginated data:
- Contains page metadata (number, size, totalPages, totalElements, etc.)
- Static factory methods to create from Spring Page objects
- Support for additional metadata

**Usage Example:**
```java
@GetMapping
public PagedResponse<UserDto> listUsers(Pageable pageable) {
    Page<User> userPage = userRepository.findAll(pageable);
    Page<UserDto> dtoPage = userPage.map(mapper::toDto);
    return PagedResponse.from(dtoPage, "Users retrieved successfully");
}
```

#### ErrorResponse

Standardized error response structure:
- Follows RFC 7807 Problem Details specification
- Includes error code, message, timestamp, and path
- Support for validation error details

### CQRS Pattern

#### Command Interface

Interface for objects representing state-changing operations:
- Each command has a unique ID and name
- Commands are immutable data carriers

**Usage Example:**
```java
public class CreateUserCommand implements Command {
    private final String commandId = UUID.randomUUID().toString();
    private final String email;
    private final String username;
    private final String password;
    
    // Constructor, getters, etc.
    
    @Override
    public String getCommandId() {
        return commandId;
    }
}
```

## Configuration

### Security Configuration

#### SecurityConfig

Spring Security configuration with JWT authentication:
- Development profile: permits all requests
- Production profile: JWT authentication with Auth0
- Protected and public endpoints configuration
- CORS filter integration

**Usage Example:**
```
# In application.properties
spring.profiles.active=dev  # For development (no authentication)
spring.profiles.active=prod  # For production (JWT authentication)
```

### CORS Configuration

#### CorsConfig

Configurable CORS settings via properties:
- Allowed origins, methods, headers
- Exposed headers for client access
- Max age for preflight requests
- Credentials allowance

**Usage Example:**
```
# In application.properties
cors.allowed-origins=http://localhost:3000,https://app.reelnet.com
cors.allowed-methods=GET,POST,PUT,DELETE,OPTIONS
cors.allowed-headers=*
cors.exposed-headers=Authorization,Content-Disposition
```

### Environment Configuration

#### EnvConfig

Loading environment variables from profile-specific .env files:
- Supports .env.development, .env.production, fallback to .env
- Logs active profiles on startup

#### EnvironmentFileSetup

Copies the appropriate .env file based on active profiles:
- For production profile: uses .env.production
- For other profiles: uses .env.development

#### EnvironmentVariables

Utility for accessing environment variables:
- `getRequired(String key)`: Get required variable (throws exception if missing)
- `get(String key, String defaultValue)`: Get with default fallback
- `getBoolean(String key, boolean defaultValue)`: Get boolean value
- `getInt(String key, int defaultValue)` / `getLong(String key, long defaultValue)`: Get numeric values

**Usage Example:**
```java
@Service
public class EmailService {
    private final String smtpHost;
    private final int smtpPort;
    
    public EmailService(EnvironmentVariables env) {
        this.smtpHost = env.getRequired("SMTP_HOST");
        this.smtpPort = env.getInt("SMTP_PORT", 25);
    }
}
```

## Monitoring & Actuator

### MetricsConfig

Configuration for application metrics collection:
- API error counter and request timer
- Active users and pending jobs gauges
- Methods to update metric values

**Usage Example:**
```java
@Service
public class UserService {
    private final MetricsConfig metricsConfig;
    
    // Constructor injection
    
    public void login(String username, String password) {
        // Authentication logic
        metricsConfig.incrementActiveUsers();
    }
    
    public void logout() {
        metricsConfig.decrementActiveUsers();
    }
}
```

### Custom Actuator Endpoints

#### SystemResourcesEndpoint

Custom actuator endpoint providing system resource information:
- Memory usage (heap and non-heap)
- CPU information (processors, system load)
- Thread statistics
- Runtime information (uptime)

**Access URL:** `/actuator/system-resources`

#### CustomInfoContributor

Enhanced information for the `/actuator/info` endpoint:
- Runtime details (memory, Java version)
- Database information (product, version)
- System information (OS, architecture)

## Constants & Enums

### ApplicationConstants

Central location for application-wide constants:
- Security constants (tokens, roles)
- Profile names (development, production, test)
- API paths and versions
- Pagination defaults

**Usage Example:**
```java
@RestController
@RequestMapping(ApplicationConstants.Api.API_BASE_URL + "/users")
public class UserController {
    // Controller methods
}
```

### Status Enum

Enumeration of common entity statuses:
- ACTIVE, INACTIVE, SUSPENDED, PENDING, etc.
- Each status has a code and description
- Helper methods to check status type (isActive, isProcessing, etc.)

**Usage Example:**
```java
if (user.getStatus().isInactiveOrDeleted()) {
    throw new BusinessException("User account is not active");
}
```

### SortDirection Enum

Enumeration for sorting directions:
- ASCENDING, DESCENDING
- Methods to convert between API codes and SQL direction
- Helper to extract actual field name from prefixed sort fields ("-fieldName")

**Usage Example:**
```java
// Handle sorting like "-createdAt" (descending) or "name" (ascending)
String sortField = "createdAt";
if (request.getParameter("sort") != null) {
    sortField = request.getParameter("sort");
}
SortDirection direction = SortDirection.fromFieldName(sortField);
String actualField = SortDirection.getActualFieldName(sortField);

Sort sort = Sort.by(direction.toSpringDirection(), actualField);
```

### Gender Enum

Enumeration of gender options:
- MALE, FEMALE, OTHER
- Each with code and description
- Helper methods (isMale, isFemale)

**Usage Example:**
```java
if (user.getGender().isMale()) {
    // Gender-specific logic
}
```

## Setup Instructions

### Prerequisites

- Java 17+
- Maven 3.8+
- PostgreSQL 13+

### Environment Setup

1. Create environment files in the project root:

   **.env.development**
   ```
   # Database
   DB_URL=jdbc:postgresql://localhost:5432/reelnet_dev
   DB_USERNAME=postgres
   DB_PASSWORD=postgres
   
   # JWT Auth (Auth0)
   AUTH0_DOMAIN=your-dev-tenant.auth0.com
   AUTH0_AUDIENCE=https://api.reelnet.com
   
   # Application
   APP_URL=http://localhost:8080
   FRONTEND_URL=http://localhost:3000
   ```

   **.env.production**
   ```
   # Database
   DB_URL=jdbc:postgresql://db-host:5432/reelnet_prod
   DB_USERNAME=reelnet_user
   DB_PASSWORD=strong_password
   
   # JWT Auth (Auth0)
   AUTH0_DOMAIN=your-prod-tenant.auth0.com
   AUTH0_AUDIENCE=https://api.reelnet.com
   
   # Application
   APP_URL=https://api.reelnet.com
   FRONTEND_URL=https://app.reelnet.com
   ```

2. Create application-{profile}.properties files:

   **application-dev.properties**
   ```
   # DB
   spring.datasource.url=${DB_URL}
   spring.datasource.username=${DB_USERNAME}
   spring.datasource.password=${DB_PASSWORD}
   
   # JPA
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true
   
   # Logging
   logging.level.com.learning.reelnet=DEBUG
   
   # CORS
   cors.allowed-origins=${FRONTEND_URL}
   ```

   **application-prod.properties**
   ```
   # DB
   spring.datasource.url=${DB_URL}
   spring.datasource.username=${DB_USERNAME}
   spring.datasource.password=${DB_PASSWORD}
   
   # JPA
   spring.jpa.hibernate.ddl-auto=none
   spring.jpa.show-sql=false
   
   # Logging
   logging.level.com.learning.reelnet=INFO
   
   # CORS
   cors.allowed-origins=${FRONTEND_URL}
   ```

### Running the Application

**Development Mode:**
```bash
mvn spring-boot:run -Dspring.profiles.active=dev
```

**Production Mode:**
```bash
mvn spring-boot:run -Dspring.profiles.active=prod
```

### Building for Deployment

```bash
mvn clean package -DskipTests
```

This will generate a JAR file in the `target` directory, which can be deployed to your server.

### Running with Docker

```bash
# Build the image
docker build -t reelnet-backend .

# Run the container
docker run -p 8080:8080 -e "SPRING_PROFILES_ACTIVE=prod" reelnet-backend
```