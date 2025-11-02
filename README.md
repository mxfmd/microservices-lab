# microservices-lab

The sandbox to play with Spring Boot Cloud and Docker.

# Getting Started

Prod:
```
docker-compose up -d
```
Dev / Local:
```
docker-compose -f compose.dev.yml up -d
```
Test:
```
curl 'http://localhost:8080/api/book-recommendation'
```
