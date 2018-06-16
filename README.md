# microservices-lab
The sandbox to play with Spring Boot Cloud and Docker.

# Build
To build an application run:
```
./mvnw clean package spring-boot:run
```

# Docker
```
docker build -t microservices-lab .   
docker run -d --name microservices-lab -p 8080:8080 microservices-lab
```

An application is accessible through the _http://localhost:8080_.