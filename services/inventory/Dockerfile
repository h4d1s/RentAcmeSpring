FROM maven:3.9-eclipse-temurin-21-alpine AS maven

RUN apk add --no-cache gcompat

WORKDIR /app

COPY . .

RUN --mount=type=cache,target=/root/.m2 \
    mvn -B -ntp clean package \
    -pl services/inventory \
    -am \
    -DskipTests

FROM eclipse-temurin:21-jre-alpine

ARG JAR_FILE=/app/services/inventory/target/*.jar
COPY --from=maven ${JAR_FILE} app.jar

EXPOSE 8000
EXPOSE 8080
EXPOSE 9090

ENTRYPOINT ["java","-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8000","-jar","/app.jar"]