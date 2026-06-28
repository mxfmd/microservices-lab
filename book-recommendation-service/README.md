# microservices-lab : book-recommendation-service
The sandbox to play with Spring Boot Cloud and Docker.

# Build
From the repository root:
```
./mvnw -pl book-recommendation-service -am clean package
./mvnw -pl book-recommendation-service spring-boot:run
```

# Docker
The Dockerfile needs the repository root as the build context (it copies sibling module poms), so build from the root with `-f`:
```
docker build -f book-recommendation-service/Dockerfile -t book-recommendation .
docker run -d --name book-recommendation -p 8082:8082 book-recommendation
```

An application is accessible through the _http://localhost:8082_.
