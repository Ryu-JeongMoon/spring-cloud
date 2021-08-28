FROM openjdk:17-ea-11-jdk-slim
VOLUME /tmp
COPY apiEncryptionKey.jks apiEncryptionKey.jks
COPY build/libs/config-service-1.0.jar configservice.jar
ENTRYPOINT ["java", "-jar", "configservice.jar"]