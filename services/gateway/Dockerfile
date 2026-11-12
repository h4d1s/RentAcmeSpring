FROM maven:3.9-eclipse-temurin-21-alpine AS maven

WORKDIR /app

COPY . .

RUN --mount=type=cache,target=/root/.m2 \
    mvn -B -ntp clean package \
    -pl services/gateway \
    -am \
    -DskipTests

FROM eclipse-temurin:21-jre-alpine

ARG JAR_FILE=/app/services/gateway/target/*.jar
COPY --from=maven ${JAR_FILE} app.jar

EXPOSE 5005
EXPOSE 8080

ENTRYPOINT ["java","-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005","-jar","/app.jar"]