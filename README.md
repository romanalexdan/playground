# playground

## Project Overview

This repository is a Java REST API testing framework for financial transaction and KYC (Know Your Customer) operations. It features comprehensive test coverage including unit tests, REST API tests using RestAssured, and integration tests against mocked APIs. The project is configured with Gradle, domain models using Lombok annotations, and Jenkins pipeline automation.

Supported features:
- Java 17+ (or configured Gradle `sourceCompatibility`)
- Gradle wrapper (`gradlew`, `gradlew.bat`)
- Domain models: KYC, Transaction, Transfer with status enums
- REST API testing with RestAssured and TestNG
- Environment-based configuration management
- Mock-server support with Docker Compose mappings
- Jenkins CI pipeline in `Jenkinsfile`
- Lombok-annotated models for reduced boilerplate

## Repository Layout

- `build.gradle` / `settings.gradle` : Gradle build definitions
- `gradle.properties` : Gradle property settings
- `src/main/java/org/example/models` : Domain models (KYC, Transaction, Transfer & related entities)
- `src/test/java/rest` : REST API tests using RestAssured
- `src/test/java/simple` : Unit tests (JUnit & TestNG)
- `src/test/java/util` : Test utilities & environment configuration (EnvConfig)
- `src/test/resources/mockserver` : Mock server configuration with Docker Compose and API mappings
- `src/test/resources/testrunner` : Test suite configuration
- `Jenkinsfile` : CI pipeline script

## Quick Start

### 1. Clone repository

```bash
git clone https://github.com/romanalexdan/playground.git
cd playground
```

### 2. Build & compile

```bash
./gradlew clean build
```

On Windows:

```powershell
./gradlew.bat clean build
```

### 3. Run tests

```bash
./gradlew test
```

Test reports:
- `build/reports/tests/test/index.html`
- `build/test-results/test/`

## Running Tests

### Running Specific Test Classes

**Unit Tests:**
- JUnit: `./gradlew test --tests org.example.simple.SimpleJunitTest`
- TestNG: `./gradlew test --tests org.example.simple.SimpleTestNGTest`

**REST API Tests:**
- Base API setup: `./gradlew test --tests rest.BaseAPITest`
- GET operations: `./gradlew test --tests rest.GetApiTest`
- POST operations: `./gradlew test --tests rest.PostApiTest`

Set environment variables before running API tests:
```bash
export API_BASE_URL=http://localhost:8080
export IS_MOCKED_API=true
```

## Jenkins Integration

`Jenkinsfile` is set up for pipeline automation and should run:
1. `checkout scm`
2. `./gradlew clean build test`
3. Publish test reports and artifacts

> Confirm Jenkins agent has Docker and Java SDK installed if running integration tests needing mockserver Compose.

## Local Docker mockserver (optional)

From `src/test/resources/mockserver`:

```bash
docker compose -f docker-compose.yml up -d
# execute your test target
./gradlew test
# clean up
docker compose -f docker-compose.yml down
```

## IDE Setup

Recommended:
- IntelliJ IDEA or VS Code with Java and Gradle extensions
- Import project as Gradle project
- Enable annotation processing for Lombok support

## Domain Models

The project includes models for KYC and transaction processing:

- **Kyc.java** - Know Your Customer entity with user ID and verification status
- **KycStatus.java** - Status enumeration for KYC verification states
- **Transaction.java** - Financial transaction details and metadata
- **TransactionResponse.java** - API response wrapper for transaction operations
- **Transfer.java** - Fund transfer entity with source and destination details
- **TransferStatus.java** - Status enumeration for transfer states

## Notes

- **Domain Models:** All models in `src/main/java/org/example/models` use Lombok annotations (`@Data`, `@Builder`, etc.) for getters, setters, and constructors.
- **REST API Testing:** The project uses RestAssured for REST API testing, with `EnvConfig` managing base URL and mock API flags.
- **Environment Configuration:** Update `EnvConfig` if adding new configuration parameters or modifying base URL, authentication, or mock settings.
- **MockServer Mappings:** For API-backed tests, update mock API mappings in `src/test/resources/mockserver/mappings/mappings.json`.
- **Source Compatibility:** Update `build.gradle` when changing Java version requirements.
- **Gradle Diagnostics:** Check `build/reports/configuration-cache` for Gradle configuration-cache issues.

## Trouble-shooting

- `./gradlew --refresh-dependencies clean build`
- Remove `~/.gradle/caches` if dependency issues persist
- For failing tests, inspect `build/test-results/test/*.xml` and `build/reports/tests/test/index.html`
