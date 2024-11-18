FROM maven:3-openjdk-17-slim AS maven

COPY ./services/inventory/pom.xml .

COPY ./libs/eventbus/target/*.jar ./libs/com/pixellabs/eventbus/eventbus/0.0.1-SNAPSHOT/

RUN mvn dependency:go-offline

COPY ./services/inventory/src ./src

RUN mvn clean install -DskipTests

FROM openjdk:17-jdk-alpine

ARG JAR_FILE=target/*.jar
COPY --from=maven ${JAR_FILE} app.jar

EXPOSE 8000
EXPOSE 8080

ENTRYPOINT ["java","-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8000","-jar","/app.jar"]