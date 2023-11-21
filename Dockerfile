FROM jelastic/maven:3.9.1-openjdk-20.0.1 AS build
COPY . .
RUN mvn install -DskipTests
RUN mvn clean package -DskipTests

EXPOSE 8080
COPY target/stockServer-0.0.1-SNAPSHOT.jar stockServer-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "/stockServer-0.0.1-SNAPSHOT.jar"]