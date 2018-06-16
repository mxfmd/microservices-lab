FROM openjdk:8
EXPOSE 8080
CMD java -jar /microservices-lab.jar
ADD target/microservices-lab-0.0.1-SNAPSHOT.jar /microservices-lab.jar