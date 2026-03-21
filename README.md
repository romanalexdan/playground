# playground

## Project Overview

This repository contains a sample Java project configured with Gradle, minimal business logic in `src/main/java`, and automated tests in `src/test/java`. The project also includes Jenkins pipeline configuration in `Jenkinsfile` and local testing infrastructure via `src/test/resources/mockserver`.

Supported features:
- Java 17+ (or configured Gradle `sourceCompatibility`)
- Gradle wrapper (`gradlew`, `gradlew.bat`)
- Sample tests and integration-style tests (via TestNG or custom runner)
- Mock-server artifact support and Docker Compose mappings
- Jenkins CI pipeline in `Jenkinsfile`

## Repository Layout

- `build.gradle` / `settings.gradle` : Gradle build definitions
- `gradle.properties` : Gradle property settings
- `src/main/java` : Java source with supporting features for defined tests
- `src/test/java` : Test classes
- `src/test/resources` : test resource config, including `mockserver` and `testrunner`
- `Jenkinsfile` : CI pipeline script

## Quick Start

### 1. Clone repository

```bash
git clone https://your.git.repo/url playground
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

## Running single tests

- JUnit: `./gradlew test --tests org.example.SimpleJunitTest`
- TestNG: `./gradlew test --tests org.example.SimpleTestNGTest`

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
- Enable annotation processing (if used by generated sources)

## Notes

- If adding new source compatibility, update `build.gradle`.
- If using API-backed tests, update mock mappings in `src/test/resources/mockserver/mappings/mappings.json`.
- Check `build/reports/configuration-cache` for Gradle configuration-cache diagnostics.

## Trouble-shooting

- `./gradlew --refresh-dependencies clean build`
- Remove `~/.gradle/caches` if dependency issues persist
- For failing tests, inspect `build/test-results/test/*.xml` and `build/reports/tests/test/index.html`
