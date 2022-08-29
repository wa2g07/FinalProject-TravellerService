FROM gradle:jdk17 AS build-stage

COPY ./ ./
RUN  ./gradlew build --exclude-task test 

FROM openjdk:20

ENV SERVICE_PORT=8081
COPY --from=build-stage /home/gradle/build/libs/TravelerService-0.0.1-SNAPSHOT.jar /app.jar

ENTRYPOINT ["java","-jar","app.jar"]
