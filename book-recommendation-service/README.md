# microservices-lab : book-service
The sandbox to play with Spring Boot Cloud and Docker.

# Build
To build an application run:
```
./mvnw clean package spring-boot:run
```

# Docker
```
docker build -t book-recommendation .   
docker run -d --name book-recommendation -p 8082:8082 book-recommendation
```

An application is accessible through the _http://localhost:8082_.