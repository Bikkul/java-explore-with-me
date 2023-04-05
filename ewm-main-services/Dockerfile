FROM amazoncorretto:11-alpine-jdk
COPY target/*.jar main-services.jar
ENTRYPOINT ["java","-jar","/main-services.jar"]