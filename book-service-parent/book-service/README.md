# microservices-lab : book-service
The sandbox to play with Spring Boot Cloud and Docker.

# Build
From the repository root:
```
./mvnw -pl book-service-parent/book-service -am clean package
./mvnw -pl book-service-parent/book-service spring-boot:run
```

# Docker
The Dockerfile needs the repository root as the build context (it copies sibling module poms), so build from the root with `-f`:
```
docker build -f book-service-parent/book-service/Dockerfile -t book-service .
docker run -d --name book-service -p 8081:8081 book-service
```

An application is accessible through the _http://localhost:8081_.
