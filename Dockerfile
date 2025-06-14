FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw .
COPY pom.xml .
COPY src ./src

RUN chmod +x mvnw && ./mvnw package -DskipTests

EXPOSE 8080

CMD ["java", "-jar", "target/*.jar"]
