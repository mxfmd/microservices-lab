# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Overview

A Spring Boot / Spring Cloud microservices sandbox. Five Spring Boot apps wired together with Eureka service discovery, a Spring Cloud Config server, a Spring Cloud Gateway, and a Feign-based service client. Java 21, Maven multi-module build, Docker for deployment.

## Build & test

Maven wrapper at the repo root drives the whole reactor.

```bash
./mvnw clean package              # build + test all modules
./mvnw -pl <module> -am package   # build one module and its dependencies
./mvnw -pl book-service-parent/book-service -am package
./mvnw test                       # run all tests
./mvnw -pl gateway test           # tests for a single module
./mvnw -Dtest=BookServiceClientTest -pl book-service-parent/book-service-client test   # single test class
./mvnw -Pcoverage verify          # JaCoCo coverage report (coverage profile)
```

Notes:
- The root `surefire` config injects a Mockito javaagent via `argLine` (`-javaagent:${org.mockito:mockito-core:jar}`); the `maven-dependency-plugin properties` goal resolves that path. Leave both in place.
- Run a single Spring Boot app locally: `./mvnw -pl <module> spring-boot:run`.

## Running the stack

Two Docker Compose files:

```bash
docker-compose up -d                       # prod: pulls signed images from ghcr.io/mxfmd/microservices-lab/*
docker-compose -f compose.dev.yml up -d    # dev: builds every image locally from each Dockerfile
```

Smoke test the whole chain through the gateway:

```bash
curl 'http://localhost:8080/api/book-recommendation'
```

Differences that matter:
- `compose.dev.yml` builds from source and only the gateway publishes a host port (`8080:8080`); reach other services through the gateway or `docker exec`.
- `compose.yml` (prod) exposes nothing to the host directly; the gateway is fronted by an external Traefik network (`traefik-network`) and routed by Host rule. It pulls `:latest` images from GHCR.

## Services and ports

| Service | Port | Role |
|---|---|---|
| config-service | 8888 | Spring Cloud Config Server |
| discovery-service | 8761 | Eureka registry |
| gateway | 8080 | Spring Cloud Gateway, single entry point |
| book-service | 8081 | Owns book data (JPA + H2), exposed via Spring Data REST |
| book-recommendation-service | 8082 | Picks a random book by calling book-service |

Startup order is enforced by Docker healthchecks (`depends_on: condition: service_healthy`): config -> discovery -> gateway/book-service -> book-recommendation.

## Architecture

**Maven layout is two levels deep.** Root `pom.xml` (`me.dolia.lab.microservices:microservices-lab`) inherits from `spring-boot-starter-parent` and imports `spring-cloud-dependencies`. Its modules are the four standalone services plus `book-service-parent`, which is itself a `pom` aggregator over three sub-modules:
- `book-service` - the runnable service.
- `book-service-client` - a reusable Feign client (`BookServiceClient`) that other services depend on to call book-service.
- `book-service-client-common` - shared DTOs (`BookResponse`) used by both sides.

**Config is self-referential.** config-service is a Spring Cloud Config Server that reads YAML from *this same repo* (`spring.cloud.config.server.git.uri = https://github.com/mxfmd/microservices-lab.git`, `search-paths: config-data`, label `main`). So `config-data/book-recommendation-service.yml` (which sets `maxBookId`) is served to book-recommendation-service at runtime over HTTP, not packaged into the jar. Changing service config means editing `config-data/` and pushing to `main`.

**Bootstrap phase.** Services that consume config (config-service itself, book-service, book-recommendation-service) include `spring-cloud-starter-bootstrap` and a `bootstrap.yml` that only sets `spring.application.name`. The application name is the key both for Config Server lookup and Eureka registration.

**Service-to-service calls go through the client library, not raw HTTP.** book-recommendation-service depends on `book-service-client` and injects `BookServiceClient` (a `@FeignClient(name = "book-service")`). The client is wired in automatically: `book-service-client` ships a `META-INF/spring.factories` pointing at `BookServiceClientAutoConfiguration`, which `@EnableFeignClients(clients = BookServiceClient.class)`. Consumers do not need to declare the client themselves. Calls resolve `book-service` via Eureka and are load-balanced (`lb://`). Note `book-service` itself depends on `book-service-client` only in **test** scope.

**Resilience.** `BookRecommendationController.recommendBook()` is wrapped in a Resilience4j `@CircuitBreaker(name = "bookService", fallbackMethod = "reliable")`; the fallback returns a `DEFAULT` book when book-service is unavailable.

**Gateway routing.** Only `book-recommendation-service` is exposed externally. The route maps `/api/book-recommendation` -> `lb://BOOK-RECOMMENDATION-SERVICE` with `StripPrefix=1` (so `/api/x` -> `/x` downstream).

**book-service persistence.** JPA `Book` entity backed by in-memory H2, schema and seed data managed by Liquibase (`src/main/resources/db/changelog/db.changelog-master.yaml` seeds three books). The repository is a `@RepositoryRestResource` `CrudRepository`, so CRUD is auto-exposed as REST at `/books` (Spring Data REST) - there is no hand-written controller.

## CI/CD

`.github/workflows/maven.yml` runs on push/PR to `master`:
1. `maven-build` - JDK 21, `mvn -B package`, uploads jars as artifacts.
2. `docker-build` - only on push to `master`. Matrix over the five services; each builds its Dockerfile (context is repo root, not the module dir), pushes to `ghcr.io/<repo>/<service>`, tags `latest` on default branch, and signs the image with cosign.

Each Dockerfile is multi-stage (deps -> package -> layered extract -> JRE runtime) and runs as a non-root `appuser`. The build context is always the repo root because modules reference sibling poms.

## Conventions

- Java package roots are inconsistent across modules: book-service and the client live under `me.dolia.lab.microserviceslab.*`, while the other services use `me.dolia.lab.microservices.*`. Match the package of the module you are editing.
- Lombok is used (`@Data`, etc.); `lombok.config` is at the repo root.
