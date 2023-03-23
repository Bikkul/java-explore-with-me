FROM amazoncorretto:11-alpine-jdk
COPY target/*.jar ewm-stat-server.jar
ENTRYPOINT ["java", "-jar", "/ewm-stat-server.jar"]