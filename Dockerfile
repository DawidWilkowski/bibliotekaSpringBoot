11 lines (10 sloc)  371 Bytes

# Build stage
FROM maven:3.9.0-eclipse-temurin-17 AS build
COPY src /usr/src/app/src  
COPY pom.xml /usr/src/app  
RUN mvn -f /usr/src/app/pom.xml clean package

# Run stage
FROM openjdk:17
COPY --from=build /usr/src/app/target/library-0.0.1-SNAPSHOT.jar /usr/app/library-0.0.1-SNAPSHOT.jar  
EXPOSE 8080  
ENTRYPOINT ["java","-jar","/usr/app/library-0.0.1-SNAPSHOT.jar"]  
