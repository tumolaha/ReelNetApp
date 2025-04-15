# ReelNet Backend

Spring Boot backend for the ReelNet application.

## Environment Variables

The application uses environment variables for configuration. Environment variables can be set in multiple ways:

### Environment Files

There are two main environment files:

1. `.env.development` - Used for development environments
2. `.env.production` - Used for production environments

The application automatically selects the appropriate file based on the active profile.

### Loading Environment Variables

Environment variables are loaded in the following order of precedence:

1. System environment variables (set by your OS)
2. Command-line arguments
3. Environment variables from `.env.production` or `.env.development` (based on active profile)
4. Default values in application.yml files

### Setting the Active Profile

You can set the active profile in several ways:

```bash
# Option 1: As a JVM system property
java -Dspring.profiles.active=prod -jar app.jar

# Option 2: As an environment variable
export SPRING_PROFILES_ACTIVE=prod
java -jar app.jar

# Option 3: Using Gradle
./gradlew bootRun --args='--spring.profiles.active=prod'
```

### Accessing Environment Variables in Code

Use the `EnvironmentVariables` utility class to access environment variables in your code:

```java
@Autowired
private EnvironmentVariables env;

// Get a required variable (throws exception if not found)
String apiKey = env.getRequired("API_KEY");

// Get with default value
String serverPort = env.get("SERVER_PORT", "8080");

// Get as boolean
boolean enableFeature = env.getBoolean("ENABLE_FEATURE", false);

// Get as integer
int maxConnections = env.getInt("MAX_CONNECTIONS", 10);
```

## Database Configuration

The database is configured using the following environment variables:

```
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/reelnet
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=postgres
SPRING_JPA_HIBERNATE_DDL_AUTO=update
```

## Running the Application

To run the application locally:

```bash
# Development mode
./gradlew bootRun

# Production mode
./gradlew bootRun --args='--spring.profiles.active=prod'
```

## Building for Production

```bash
./gradlew clean build
```

This will create a JAR file in `build/libs/` that can be deployed to a server. 