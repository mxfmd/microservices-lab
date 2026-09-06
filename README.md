# microservices-lab

A sandbox for playing with Spring Boot, Spring Cloud and Docker. Five Spring Boot
services wired together with Eureka service discovery, a Spring Cloud Config
server, a Spring Cloud Gateway and a Feign-based client library.

Live: https://microservices-lab.dolia.me/api/book-recommendation

```json
{"id":3,"name":"Elon Musk: Tesla, SpaceX, and the Quest for a Fantastic Future","author":"Ashlee Vance"}
```

## Stack

Java 21, Spring Boot 3.5.5, Spring Cloud 2025.0.0, Maven multi-module, Docker.

## Services

| Service | Port | Role |
|---|---|---|
| config-service | 8888 | Spring Cloud Config Server, serves `config-data/` from this repo |
| discovery-service | 8761 | Eureka registry |
| gateway | 8080 | Spring Cloud Gateway, the only externally exposed entry point |
| book-service | 8081 | Owns book data (JPA + H2 + Liquibase), exposed via Spring Data REST |
| book-recommendation-service | 8082 | Returns a random book by calling book-service |

`GET /api/book-recommendation` hits the gateway, which routes to
book-recommendation-service, which calls book-service through a load-balanced
Feign client resolved via Eureka. A Resilience4j circuit breaker returns a
`DEFAULT` book if book-service is unavailable.

## Build

The Maven wrapper at the repo root drives the whole reactor.

```bash
./mvnw clean package                  # build and test everything
./mvnw -pl gateway -am package        # one module and its dependencies
./mvnw -pl gateway test               # one module's tests
./mvnw -Pcoverage verify              # JaCoCo coverage report
```

`config-service`'s test clones this repository from GitHub, so it needs network
access.

## Run

```bash
docker compose up -d                       # prod: pulls signed images from GHCR
docker compose -f compose.dev.yml up -d    # dev: builds every image from source
```

Smoke test:

```bash
curl 'http://localhost:8080/api/book-recommendation'
```

Only the dev compose file publishes a host port (the gateway on 8080); reach the
other services through the gateway or `docker exec`. Startup order is enforced
with healthchecks: config -> discovery -> gateway/book-service ->
book-recommendation.

A single service can also run outside Docker:

```bash
./mvnw -pl discovery-service spring-boot:run
```

## Deploy

Images are built in GitHub Actions on every push to `master`, pushed to
`ghcr.io/mxfmd/microservices-lab/<service>:latest` and signed with cosign.
Nothing is compiled on the host: `compose.yml` only pulls images.

The stack runs on a VPS behind Traefik. `compose.yml` attaches the gateway to an
external `traefik-network` and routes it by Host rule; the other services stay on
the internal network and publish no ports. Deployment itself is manual.

## Monitoring

Every service exposes `/actuator/health`, `/actuator/info` and
`/actuator/prometheus`. The Docker healthchecks use `/actuator/health`.

Uptime: [UptimeRobot monitor](https://dashboard.uptimerobot.com/monitors/801394336)

## Configuration

Service config lives in `config-data/` in this repository and is served over HTTP
by config-service at runtime rather than packaged into the jars. Changing
`maxBookId` means editing `config-data/book-recommendation-service.yml` and
pushing. Which branch config-service reads is set by
`spring.cloud.config.server.git.default-label`; that setting currently names a
branch that does not exist and falls back to `master`
(see [#47](https://github.com/mxfmd/microservices-lab/issues/47)).

## License

MIT. See [LICENSE](LICENSE).
