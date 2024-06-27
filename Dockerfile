FROM maven:3.9.7-eclipse-temurin-17-alpine AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests -Pprod 

FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/codefact-api.jar codefact-api.jar
EXPOSE 8092
CMD ["java", "-jar", "codefact-api.jar"]