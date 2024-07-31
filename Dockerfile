## For production

FROM gradle:8.9.0-jdk21 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN --mount=type=cache,target=/home/gradle/.gradle/caches gradle ztor:buildFatJar --no-daemon

FROM eclipse-temurin:21.0.3_9-jre
EXPOSE 8080:8080
RUN mkdir /app
COPY --from=build /home/gradle/src/ztor/build/libs/*.jar /app/ztor.jar
ENTRYPOINT ["java","-jar","/app/ztor.jar"]
