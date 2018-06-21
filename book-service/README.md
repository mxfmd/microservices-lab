# microservices-lab : book-service
The sandbox to play with Spring Boot Cloud and Docker.

# Build
To build an application run:
```
./mvnw clean package spring-boot:run
```

# Docker
```
docker build -t book-service .   
docker run -d --name book-service -p 8080:8080 book-service
```

An application is accessible through the _http://localhost:8080_.