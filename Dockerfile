FROM jelastic/maven:3.9.1-openjdk-20.0.1 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM debian:11-slim
EXPOSE 8080
COPY --from=build target/stockServer-0.0.1-SNAPSHOT.jar stockServer-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "/stockServer-0.0.1-SNAPSHOT.jar"]