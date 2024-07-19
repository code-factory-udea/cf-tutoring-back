FROM  maven:3.9.8-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests -Pprod 

FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=build /app/target/codefact-api.jar codefact-api.jar
EXPOSE 8443
CMD ["java", "-jar", "codefact-api.jar"]