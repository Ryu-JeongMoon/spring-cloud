FROM openjdk:17-ea-11-jdk-slim
VOLUME /tmp
COPY build/libs/user-service-1.0.jar userservice.jar
ENTRYPOINT ["java", "-jar", "userservice.jar"]