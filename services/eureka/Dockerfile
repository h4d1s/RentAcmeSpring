FROM maven:3-openjdk-17-slim AS maven

COPY ./services/eureka/pom.xml .
RUN mvn dependency:go-offline

COPY ./services/eureka/src ./src

RUN mvn clean install -DskipTests

FROM openjdk:17-jdk-alpine

ARG JAR_FILE=target/*.jar
COPY --from=maven ${JAR_FILE} app.jar

EXPOSE 8761

ENTRYPOINT ["java","-jar","/app.jar"]